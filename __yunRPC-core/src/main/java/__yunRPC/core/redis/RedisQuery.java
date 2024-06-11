package __yunRPC.core.redis;


import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Redis数据库操作方法封装
 */
public class RedisQuery {

    /**
     * 添加数据,相同key会覆盖数据，所以也相当于改
     */
    public static void set(String key, String value) {
        Jedis jedis = JedisDBPool.getConnectJedis();
        jedis.set(key, value);
        jedis.close();
    }

    /**
     * 添加数据，带超时时间，超时自动销毁
     */
    public static void setex(String key, String value, int seconds) {
        Jedis jedis = JedisDBPool.getConnectJedis();
        jedis.setex(key, seconds, value);
        jedis.close();
    }

    /**
     * 根据key删除数据
     */
    public static void deleteByKey(String key) {
        Jedis jedis = JedisDBPool.getConnectJedis();
        jedis.del(key);
        jedis.close();
    }

    /**
     * 根据key查询
     */
    public static String getByKey(String key) {
        Jedis jedis = JedisDBPool.getConnectJedis();
        String value = jedis.get(key);
        jedis.close();
        return value;
    }

    public static Set<String> getByKeys(String query) {
        Jedis jedis = JedisDBPool.getConnectJedis();
        Set<String> keys = jedis.keys(query + ":**");
        jedis.close();
        return keys.stream().map(RedisQuery::getByKey).collect(Collectors.toSet());
    }

    /**
     * 查询某条数据是否存在
     */
    public static boolean isExist(String key) {
        Jedis jedis = JedisDBPool.getConnectJedis();
        boolean exist = jedis.exists(key);
        jedis.close();
        return exist;
    }
}
