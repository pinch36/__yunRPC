package __yunRPC.server.registry;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: __yun
 * @Date: 2024/06/06/17:01
 * @Description:
 */
@Slf4j
public class LocalRegistry {
    private static final Map<String, Class<?>> MAP = new ConcurrentHashMap<String, Class<?>>();

    static public Class<?> get(String serviceName) {
        return MAP.get(serviceName);
    }

    static public void remove(String serviceName) {
        MAP.remove(serviceName);
    }

    static public void registry(String serviceName, Class<?> implClass) {
        log.info("{}注册",serviceName);
        MAP.put(serviceName, implClass);
    }
}
