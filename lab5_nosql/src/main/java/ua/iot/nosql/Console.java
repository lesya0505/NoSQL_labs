package ua.iot.nosql;

import org.json.JSONArray;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;
import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

public class Console implements DisplayStrategy {
    private static final String URL = "https://data.cityofnewyork.us/resource/erm2-nwe9.json";
    private static final boolean USE_SSL = true;
    private static final int PORT = 6380;
    private static final String CACHE_HOSTNAME = "dblab.redis.cache.windows.net";
    private static final String CACHE_KEY = "mYuDj9PeLR8OYx0rz1aq+H209kRAS1wt8qtDlAG4NzA=";
    private static final String REDIS = "Console";
    private static final String FILE = "FileConsoleRedis";
    private int start = 1;
    private int end = 0;
    private int batch = 100;
    private LocalDateTime dateTime = LocalDateTime.now();

    public void display() {
        Util util = new Util();
        Jedis jedis = util.getJedisConnection(CACHE_HOSTNAME, CACHE_KEY, USE_SSL, PORT);

        try {
            System.out.println("Start sending to Redis ...");
            Map<String, String> redisData = jedis.hgetAll(REDIS);
            JSONArray jsonArray;
            BufferedReader br = util.getHttpConnection(URL);
            String inputLine;
            StringBuilder response = new StringBuilder();
            int count = 1;
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
                if (count % 100 == 0) {
                    jsonArray = new JSONArray(response.toString() + ']');
                    if (util.checkIfExist(jedis, REDIS, FILE)) {
                        writeDataToRedis(jsonArray, jedis, redisData);
                    }
                }
                count += 1;
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeDataToRedis(JSONArray jsonArray, Jedis jedis, Map<String, String> redisData) {
        jedis.hset(REDIS, "File", FILE);
        jedis.hset(REDIS, "Timestamp", dateTime.toString());
        int limit = 1000;

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
            System.out.println("Record: " + i + " " + jsonObject);
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