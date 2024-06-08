package __yunRPC.core.serializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: __yun
 * @Date: 2024/06/08/13:49
 * @Description:
 */
public class JsonSerializer implements Serializer{
 private static volatile Gson gson;
    public static Gson getGson(){
        if (gson == null){
            synchronized (Gson.class){
                if (gson == null){
                    gson = new GsonBuilder().registerTypeAdapter(Class.class, new ClassCodec()).create();
                }
            }
        }
        return gson;
    }

    @Override
    public <T> byte[] serialize(T object) throws IOException {
//        Gson gson = new GsonBuilder().registerTypeAdapter(Class.class, new ClassCodec()).create();
        return null;
    }

    @Override
    public <T> T deSerialize(byte[] bytes, Class<T> type) throws IOException {
        return null;
    }
}
