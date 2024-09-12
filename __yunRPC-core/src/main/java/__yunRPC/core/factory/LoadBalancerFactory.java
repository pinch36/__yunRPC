package __yunRPC.core.factory;

import __yunRPC.core.loadbalancer.LoadBalancer;
import __yunRPC.core.loadbalancer.impl.RoundRobinLoadBalancer;
import __yunRPC.core.registry.EtcdRegistry;
import __yunRPC.core.registry.Registry;
import __yunRPC.core.spi.SpiLoader;


/**
 * @author ylw16
 */
public class LoadBalancerFactory {
    static {
        SpiLoader.load(LoadBalancer.class);
    }

    /**
     * 默认注册中心
     */
    public static final LoadBalancer DEFAULT_LOAD_BALANCER = new RoundRobinLoadBalancer();

    public static LoadBalancer getInstance(String key){
        return SpiLoader.getInstance(LoadBalancer.class,key);
    }
}
