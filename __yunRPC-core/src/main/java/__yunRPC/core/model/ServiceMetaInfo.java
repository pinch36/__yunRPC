package __yunRPC.core.model;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
/**
 * Created with IntelliJ IDEA.
 *
 * @Author: __yun
 * @Date: 2024/06/08/15:04
 * @Description:
 */
@Data
public class ServiceMetaInfo {
    private String serviceName;
    private String serviceVersion = "1.0";
    private String serviceHost;
    private Integer servicePort;
    private String serviceGroup = "default";

    /**
     * 获取注册服务键名
     *
     * @return
     */
    public String getServiceKey(){
        return String.format("%s:%s",serviceName,serviceVersion);
    }
    /**
     * 获取注册服务节点键名
     *
     * @return
     */
    public String getServiceNodeKey(){
        return String.format("%s:%s:%s",getServiceKey(),serviceHost,servicePort);
    }
    public String getServiceAddress(){
        if (!StrUtil.contains(serviceHost,"http")){
            return String.format("http://%s:%s",serviceHost,servicePort);
        }
        return String.format("%s:%s",serviceHost,servicePort);
    }
}
