package __yunRPC.core.redis;

import org.junit.Before;
import org.junit.Test;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: __yun
 * @Date: 2024/06/11/10:09
 * @Description:
 */
public class RedisQueryTest {
    String key = "hello:so:";
    String value = "redis";

    @Before
    public void init() {
        JedisDBPool.initDefault();
    }

    @Test
    public void setex() {
        RedisQuery.setex(key, value, 30);
    }

    @Test
    public void getByKey() {
        String byKey = RedisQuery.getByKey(key);
        System.out.println(byKey);
    }

    @Test
    public void getByKeys() {
        Set<String> byKeys = RedisQuery.getByKeys("hello");
        byKeys.forEach(System.out::println);
    }
}