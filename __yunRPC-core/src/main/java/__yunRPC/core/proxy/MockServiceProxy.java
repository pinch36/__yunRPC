package __yunRPC.core.proxy;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: __yun
 * @Date: 2024/06/08/12:35
 * @Description:
 */
@Slf4j
public class MockServiceProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> returnType = method.getReturnType();
        log.info("mock invoke {}",method.getName());
        return getDefaultObject(returnType);
    }

    private Object getDefaultObject(Class<?> returnType) {
        if (returnType.isPrimitive()){
            if (returnType == int.class){
                return 0;
            } else if (returnType == short.class) {
                return (short)0;
            } else if (returnType == boolean.class){
                return false;
            } else if (returnType == long.class) {
                return 1L;
            }
        }
        return null;
    }
}
