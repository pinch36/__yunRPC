package __yunRPC.core.factory;

import __yunRPC.core.registry.EtcdRegistry;
import __yunRPC.core.registry.Registry;
import __yunRPC.core.registry.RegistryKeys;
import __yunRPC.core.spi.SpiLoader;

/**
 * 注册中心工厂，获取注册中心对象
 *
 * @Author: __yun
 * @Date: 2024/06/08/16:14
 * @Description:
 */

public class RegistryFactory {
    static {
        SpiLoader.load(Registry.class);
    }

    /**
     * 默认注册中心
     */
    public static final Registry DEFAULT_REGISTRY = new EtcdRegistry();

    public static Registry getInstance(String key){
        return SpiLoader.getInstance(Registry.class,key);
    }
}
