package __yunRPC.core.loadbalancer;

import __yunRPC.core.model.service.ServiceMetaInfo;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: __yun
 * @Date: 2024/09/13/7:00
 * @Description:
 */
public interface LoadBalancer {
    ServiceMetaInfo select(Map<String,Object> requestParams, List<ServiceMetaInfo> serviceMetaInfos);
}
