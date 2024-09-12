package __yunRPC.core.factory;

import __yunRPC.core.registry.EtcdRegistry;
import __yunRPC.core.registry.Registry;
import __yunRPC.core.serializer.JsonSerializer;
import __yunRPC.core.serializer.Serializer;
import __yunRPC.core.spi.SpiLoader;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: __yun
 * @Date: 2024/09/12/8:03
 * @Description:
 */
public class SerializerFactory {
    static {
        SpiLoader.load(Serializer.class);
    }

    /**
     * 默认注册中心
     */
    public static final Serializer DEFAULT_SERIALIZER = new JsonSerializer();

    public static Serializer getInstance(String key){
        return SpiLoader.getInstance(Serializer.class,key);
    }
}
