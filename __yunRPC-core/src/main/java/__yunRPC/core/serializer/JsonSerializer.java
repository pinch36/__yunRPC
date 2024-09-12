package __yunRPC.core.serializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: __yun
 * @Date: 2024/06/08/13:49
 * @Description:
 */
public class JsonSerializer implements Serializer{
 private static volatile Gson gson = new GsonBuilder().registerTypeAdapter(Class.class, new ClassCodec()).create();
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
        String json = gson.toJson(object);
        return json.getBytes();
    }

    @Override
    public <T> T deSerialize(byte[] bytes, Class<T> type) throws IOException {
        String s = new String(bytes, StandardCharsets.UTF_8);
        T object = gson.fromJson(s, type);
        return object;
    }
}
