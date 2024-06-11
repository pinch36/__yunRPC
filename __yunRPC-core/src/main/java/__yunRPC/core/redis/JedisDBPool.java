package __yunRPC.core.redis;

import __yunRPC.core.RpcApplication;
import __yunRPC.core.util.CommonUtils;
import lombok.Getter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Redis连接池
 */
public class JedisDBPool {

    @Getter
    private static JedisPool jedisPool;

    public static void init() {
        try {
            Properties properties = new Properties();
            InputStream in = CommonUtils.readResourceFile("application.properties");
            properties.load(in);
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            jedisPoolConfig.setMaxTotal(Integer.valueOf(properties.getProperty("maxTotal")));
            jedisPoolConfig.setMaxIdle(Integer.valueOf(properties.getProperty("maxIdle")));
            jedisPoolConfig.setMinIdle(Integer.valueOf(properties.getProperty("minIdle")));
            jedisPoolConfig.setMaxWaitMillis(Integer.valueOf(properties.getProperty("maxWaitMillis")));
            jedisPoolConfig.setTestOnBorrow(Boolean.valueOf(properties.getProperty("testOnBorrow")));
            jedisPoolConfig.setTestOnReturn(Boolean.valueOf(properties.getProperty("testOnReturn")));
            jedisPoolConfig.setTimeBetweenEvictionRunsMillis(Integer.valueOf(properties.getProperty("timeBetweenEvictionRunsMillis")));
            jedisPoolConfig.setMinEvictableIdleTimeMillis(Integer.valueOf(properties.getProperty("minEvictableIdleTimeMillis")));
            jedisPoolConfig.setNumTestsPerEvictionRun(Integer.valueOf(properties.getProperty("numTestsPerEvictionRun")));
            jedisPool = new JedisPool(jedisPoolConfig, properties.getProperty("host"), Integer.valueOf(properties.getProperty("port")), 2000, RpcApplication.getRpcConfig().getRegistryConfig().getPassword());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void initDefault() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPool = new JedisPool(jedisPoolConfig, "localhost", 6379);
    }

    public static Jedis getConnectJedis() {
        return jedisPool.getResource();
    }
}
