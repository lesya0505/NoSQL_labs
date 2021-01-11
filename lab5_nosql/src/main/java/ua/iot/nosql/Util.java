package ua.iot.nosql;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class Util {

    public BufferedReader getHttpConnection(String url) throws IOException {

        URL data = new URL(url);
        HttpURLConnection con = (HttpURLConnection) data.openConnection();
        return new BufferedReader(new InputStreamReader(con.getInputStream()));
    }

    public Jedis getJedisConnection(String hostname, String key, boolean ssl, int port) {
        JedisShardInfo shardInfo = new JedisShardInfo(hostname, port, ssl);
        shardInfo.setPassword(key);
        return new Jedis(shardInfo);
    }

    public boolean checkIfExist(Jedis jedis, String mapName, String file) {
        Map<String, String> redisData = jedis.hgetAll(mapName);
        String filename = redisData.get("File");
        String status = redisData.get("Status");
        String timestamp = redisData.get("Timestamp");

        if (!file.equals(filename)) {
            jedis.hset(mapName, "File", file);
        } else
             {
                jedis.hset(mapName, "Message", "Retry to write this file");
                System.out.println("Result: The file " + "'" + file + "'" + " already exists with Timestamp: " + timestamp);
                jedis.close();
                return false;
            }
        return true;
    }
}