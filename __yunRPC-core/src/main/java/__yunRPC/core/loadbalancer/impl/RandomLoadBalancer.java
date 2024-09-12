package __yunRPC.core.loadbalancer.impl;

import __yunRPC.core.loadbalancer.LoadBalancer;
import __yunRPC.core.model.service.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: __yun
 * @Date: 2024/09/13/7:07
 * @Description:
 */
public class RandomLoadBalancer implements LoadBalancer {
    private final Random random = new Random();
    @Override
    public ServiceMetaInfo select(Map<String,Object> requestParams, List<ServiceMetaInfo> serviceMetaInfos) {
        if (serviceMetaInfos.isEmpty()){
            return null;
        }
        //无需上锁，存在线程问题只是让新注册服务无法被使用并无安全问题
        return serviceMetaInfos.get(random.nextInt(serviceMetaInfos.size()));
    }
}
