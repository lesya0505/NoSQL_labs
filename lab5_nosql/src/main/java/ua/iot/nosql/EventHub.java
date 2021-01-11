package ua.iot.nosql;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microsoft.azure.eventhubs.ConnectionStringBuilder;
import com.microsoft.azure.eventhubs.EventData;
import com.microsoft.azure.eventhubs.EventHubClient;
import com.microsoft.azure.eventhubs.EventHubException;
import org.json.JSONArray;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class EventHub implements DisplayStrategy {
    private static final String URL = "https://data.cityofnewyork.us/resource/erm2-nwe9.json";
    private static final boolean USE_SSL = true;
    private static final int PORT = 6380;
    private static final String CACHE_HOSTNAME = "dblab.redis.cache.windows.net";
    private static final String CACHE_KEY = "mYuDj9PeLR8OYx0rz1aq+H209kRAS1wt8qtDlAG4NzA=";
    private static final String NAMESPACE_NAME = "dblab5";
    private static final String EVENT_HUB_NAME = "dblabs";
    private static final String SAS_KEY_NAME = "Endpoint=sb://dblab5.servicebus.windows.net/;SharedAccessKeyName=dblab;SharedAccessKey=6uKewITGvifHM0D5lnVDfPPiZOzt2jaR4h89lWY2xdQ=";
    private static final String SAS_KEY = "6uKewITGvifHM0D5lnVDfPPiZOzt2jaR4h89lWY2xdQ=";
    private static final String REDIS = "EventHub";
    private static final String FILE = "FileEventHub";
    private int start = 1;
    private int end = 0;
    private int batch = 100;
    private LocalDateTime dateTime = LocalDateTime.now();

    public void display() throws IOException, EventHubException {
        Util util = new Util();
        Jedis jedis = util.getJedisConnection(CACHE_HOSTNAME, CACHE_KEY, USE_SSL, PORT);

        final ConnectionStringBuilder eventHubConnectionString = new ConnectionStringBuilder()
                .setNamespaceName(NAMESPACE_NAME)
                .setEventHubName(EVENT_HUB_NAME)
                .setSasKeyName(SAS_KEY_NAME)
                .setSasKey(SAS_KEY);

        final Gson gson = new GsonBuilder().create();
        final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(4);
        final EventHubClient eventHubClient = EventHubClient.createSync(eventHubConnectionString.toString(), executorService);

        try {
            Map<String, String> redisData = jedis.hgetAll(REDIS);
            JSONArray jsonArray;
            System.out.println("Start sending to Event Hub ...");
            BufferedReader br = util.getHttpConnection(URL);
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }

            br.close();

            jsonArray = new JSONArray(response.toString());

            if (util.checkIfExist(jedis, REDIS, FILE)) {
                writeDataToEventHub(jsonArray, jedis, redisData, gson, eventHubClient);

            }
            System.out.println(Instant.now() + ": Sending Completed ...");
        } finally {
            eventHubClient.closeSync();
            executorService.shutdown();
        }
    }

    public void writeDataToEventHub(JSONArray jsonArray, Jedis jedis, Map<String,
            String> redisData, Gson gson, EventHubClient eventHubClient) throws EventHubException {
        jedis.hset(REDIS, "File", FILE);
        jedis.hset(REDIS, "Timestamp", dateTime.toString());
        int limit = 1000;
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
//            System.out.println("Record: " + i + " " + jsonObject);
            String payload = "Message: " + i + " " + jsonObject;
            System.out.println(payload);
            // Serialize the event into bytes
            byte[] payloadBytes = gson.toJson(payload).getBytes(Charset.defaultCharset());
            // Use the bytes to construct an {@link EventData} object
            EventData sendEvent = EventData.create(payloadBytes);
            // Transmits the event to event hub without a partition key
            // If a partition key is not set, then we will round-robin to all topic partitions
            eventHubClient.sendSync(sendEvent);

            if (i == batch) {
                end = i;
                jedis.hset(REDIS, "Records", start + ":" + end);
                jedis.hset(REDIS, "Status", "In progress");
                showDataInConsole(jedis);

                batch = batch + 100;
            }
            if (i == 999) {
                jedis.hset(REDIS, "Records", start + ":" + limit);
                showDataInConsole(jedis);
                jedis.hset(REDIS, "Records", Integer.toString(limit));
                jedis.hset(REDIS, "Status", "Completed");
                jedis.hset(REDIS, "Message", "First load");
                showDataInConsole(jedis);
                System.out.println("MESSAGE: " + redisData.get("Message"));
                System.out.println("TIMESTAMP: " + redisData.get("Timestamp"));

                jedis.close();
            }
            start = end + 1;
        }
    }

    public void showDataInConsole(Jedis jedis) {
        Map<String, String> map = jedis.hgetAll(REDIS);
        System.out.println("Records from file " + "'" + FILE + "': " + map.get("Records"));
        System.out.println("STATUS: " + map.get("Status"));
    }
}