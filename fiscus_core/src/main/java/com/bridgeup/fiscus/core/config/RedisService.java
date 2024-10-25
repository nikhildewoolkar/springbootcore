package com.bridgeup.fiscus.core.config;


import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisService {

    private static JedisPool jedisPool = null;
    private static Jedis jedis = null;

    public static enum Queues {
        ETL_MASTER,
        ETL_SLAVE,
        FISCUS_CATEGORIZATION
    }

    private RedisService() {}

    private static synchronized Jedis getInstance() {
        if (jedisPool == null) {
            jedisPool = new JedisPool("localhost", 6379);
        }

        if (jedis == null) {
            jedis = jedisPool.getResource();
        }

        return jedis;
    }


    public static void lPush(String queue, String task) {
        getInstance().lpush(queue, task);
    }

    public static String lPop(String queue) {
        return getInstance().lpop(queue);
    }

}
