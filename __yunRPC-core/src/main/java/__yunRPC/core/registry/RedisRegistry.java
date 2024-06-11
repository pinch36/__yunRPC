package __yunRPC.core.registry;

import __yunRPC.core.config.RegistryConfig;
import __yunRPC.core.model.ServiceMetaInfo;
import __yunRPC.core.redis.JedisDBPool;
import __yunRPC.core.redis.RedisQuery;
import __yunRPC.core.serializer.JsonSerializer;
import cn.hutool.core.util.StrUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import lombok.Getter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: __yun
 * @Date: 2024/06/08/15:22
 * @Description:
 */
public class RedisRegistry implements Registry {
    private static final String REDIS_ROOT_PATH = "rpc:";
    private static final Set<String> registryServiceNodeKey = new HashSet<>();
    @Getter
    private RegistryServiceCache registryServiceCache = new RegistryServiceCache();

    @Override
    public void init(RegistryConfig registryConfig) {
        JedisDBPool.initDefault();
        heartBeat();
    }

    @Override
    public void register(ServiceMetaInfo serviceMetaInfo) throws Exception {
        String key = REDIS_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();
        String value = JsonSerializer.getGson().toJson(serviceMetaInfo);
        RedisQuery.setex(key, value, 30);
        registryServiceNodeKey.add(key);
    }

    @Override
    public void unRegister(ServiceMetaInfo serviceMetaInfo) {
        String key = REDIS_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();
        RedisQuery.deleteByKey(key);
        registryServiceNodeKey.remove(key);
    }

    @Override
    public void destroy() {
        for (String serviceNodeKey : registryServiceNodeKey) {
            RedisQuery.deleteByKey(serviceNodeKey);
        }
        JedisPool jedisPool = JedisDBPool.getJedisPool();
        if (jedisPool != null) {
            jedisPool.destroy();
        }
    }

    @Override
    public List<ServiceMetaInfo> serviceDiscovery(String serviceKey) {
        List<ServiceMetaInfo> cache = registryServiceCache.readCache(serviceKey);
        if (cache != null){
            return cache;
        }
        Set<String> keys = RedisQuery.getByKeys(REDIS_ROOT_PATH + serviceKey);
        List<ServiceMetaInfo> metaInfos = keys.stream().map(key -> {
            watch(key);
            return JsonSerializer.getGson().fromJson(key, ServiceMetaInfo.class);
        }).collect(Collectors.toList());
        registryServiceCache.writeCache(serviceKey,metaInfos);
        return metaInfos;
    }

    @Override
    public void heartBeat() {
        CronUtil.schedule("*/10 * * * * *", new Task() {
            @Override
            public void execute() {
                for (String key : registryServiceNodeKey) {
                    try {
                        String value = RedisQuery.getByKey(key);
                        if (StrUtil.isBlank(value)) {
                            continue;
                        }
                        ServiceMetaInfo serviceMetaInfo = JsonSerializer.getGson().fromJson(value, ServiceMetaInfo.class);
                        register(serviceMetaInfo);
                    } catch (Exception e) {
                        throw new RuntimeException(key + "续签失败", e);
                    }
                }
            }
        });
        CronUtil.setMatchSecond(true);
        CronUtil.start();
    }

    @Override
    public void watch(String serviceNodeKey) {
        Jedis jedis = JedisDBPool.getConnectJedis();
        new Thread(() -> {
            jedis.psubscribe(new JedisPubSub(){
                @Override
                public void onPSubscribe(String pattern, int subscribedChannels) {
                    System.out.println(serviceNodeKey + "订阅事件-> "
                            + pattern + " " + subscribedChannels);
                }
                @Override
                public void onPMessage(String pattern, String channel, String key) {
                    registryServiceCache.clearCache();
                }
            }, "__keyevent@0__:del");
        }).start();
    }
}
