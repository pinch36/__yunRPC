package __yunRPC.core.serializer;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: __yun
 * @Date: 2024/06/06/19:43
 * @Description:
 */
public interface Serializer {
    <T> byte[] serialize(T object) throws IOException;
    <T> T deSerialize(byte[] bytes,Class<T> type) throws IOException;
}
