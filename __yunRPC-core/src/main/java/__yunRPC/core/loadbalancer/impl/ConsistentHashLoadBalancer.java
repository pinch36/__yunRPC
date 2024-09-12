package __yunRPC.core.loadbalancer.impl;

import __yunRPC.core.loadbalancer.LoadBalancer;
import __yunRPC.core.model.service.ServiceMetaInfo;

import java.util.IllegalFormatCodePointException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: __yun
 * @Date: 2024/09/13/7:12
 * @Description:
 */
public class ConsistentHashLoadBalancer implements LoadBalancer {
    private final TreeMap<Integer,ServiceMetaInfo> virtualNodes = new TreeMap<>();
    private final static int VIRTUAL_NODE_SIZE = 100;
    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfos) {
        if (serviceMetaInfos.isEmpty()) {
            return null;
        }
        for (ServiceMetaInfo serviceMetaInfo : serviceMetaInfos) {
            for (int i = 0; i < VIRTUAL_NODE_SIZE; i++) {
                int hash = getHash(serviceMetaInfo.getServiceAddress() + "#" + i);
                virtualNodes.put(hash,serviceMetaInfo);
            }
        }
        int hash = getHash(requestParams);
        Map.Entry<Integer, ServiceMetaInfo> entry = virtualNodes.ceilingEntry(hash);
        if (entry == null){
            entry = virtualNodes.firstEntry();
        }
        return entry.getValue();
    }

    private int getHash(Object key) {
        return key.hashCode();
    }
}
