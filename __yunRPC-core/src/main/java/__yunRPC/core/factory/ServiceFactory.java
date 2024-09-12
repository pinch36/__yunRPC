package __yunRPC.core.factory;

import __yunRPC.core.RpcApplication;
import __yunRPC.core.proxy.MockServiceProxy;
import __yunRPC.core.proxy.HttpServiceProxy;
import __yunRPC.core.proxy.TcpServiceProxy;

import java.lang.reflect.Proxy;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: __yun
 * @Date: 2024/06/06/21:04
 * @Description:
 */
public class ServiceFactory {
    public static <T> T getTcpProxy(Class<T> serviceClass) {
        if (RpcApplication.getRpcConfig().isMock()){
            return getMockProxy(serviceClass);
        }
        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class[]{serviceClass},
                new TcpServiceProxy());
    }
    public static <T> T getHttpProxy(Class<T> serviceClass) {
        if (RpcApplication.getRpcConfig().isMock()){
            return getMockProxy(serviceClass);
        }
        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class[]{serviceClass},
                new HttpServiceProxy());
    }

    private static <T> T getMockProxy(Class<T> serviceClass) {
        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class[]{serviceClass},
                new MockServiceProxy());
    }
}
