package __yunRPC.core.spi;
import cn.hutool.core.io.resource.ResourceUtil;
import lombok.extern.slf4j.Slf4j;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: __yun
 * @Date: 2024/06/08/16:25
 * @Description:
 */
@Slf4j
public class SpiLoader {
    /**
     * 存储已加载的类信息：k:接口名  v:(k:某个标识，例如redis，或者别的用户自定义标识  v:实现类)
     */
    public static Map<String, Map<String, Class<?>>> loadMap = new ConcurrentHashMap<>();
    /**
     * 实例对象缓存： k:类路径  v:对象实例
     */
    public static Map<String, Object> instanceCache = new ConcurrentHashMap<>();
    private static final String RPC_SYSTEM_SPI_DIR = "META-INF/rpc/system/";
    private static final String RPC_CUSTOM_SPI_DIR = "META-INF/rpc/custom/";
    public static String[] SCAN_DIRS = new String[]{RPC_SYSTEM_SPI_DIR,RPC_CUSTOM_SPI_DIR};

    public static final List<Class<?>> LOAD_CLASS_LIST = Arrays.asList();

    public static void loadAll() {
        log.info("加载所有SPI");
        for (Class<?> aClass : LOAD_CLASS_LIST) {
            load(aClass);
        }
    }

    public static Map<String, Class<?>> load(Class<?> loadClass) {
        log.info("加载路径为 {} 的SPI", loadClass.getName());
        //用户自定义SPI优先级更高,后遍历用户则会覆盖前面系统若有的
        HashMap<String, Class<?>> keyClassMap = new HashMap<>();
        for (String scanDir : SCAN_DIRS) {
            List<URL> resources = ResourceUtil.getResources(scanDir + loadClass.getName());
            for (URL resource : resources) {
                try {
                    InputStreamReader inputStreamReader = new InputStreamReader(resource.openStream());
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String line;
                    while ((line = bufferedReader.readLine()) != null){
                        String[] split = line.split("=");
                        if (split.length > 1){
                            String key = split[0];
                            String className = split[1];
                            keyClassMap.put(key,Class.forName(className));
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    log.error("SPI load error",e);
                    throw new RuntimeException(e);
                }
            }
        }
        loadMap.put(loadClass.getName(),keyClassMap);
        return keyClassMap;
    }

    public static <T> T getInstance(Class<?> tClass, String key) {
        String tClassName = tClass.getName();
        Map<String, Class<?>> keyClassMap = loadMap.get(tClassName);
        if (keyClassMap == null) {
            throw new RuntimeException(String.format("SpiLoader未加载 %s 类型", tClassName));
        }
        if (!keyClassMap.containsKey(key)) {
            throw new RuntimeException(String.format("SpiLoader的 %s 不存在 key=%s 的类型", tClassName, key));
        }
        Class<?> implClass = keyClassMap.get(key);
        String implClassName = implClass.getName();
        if (!instanceCache.containsKey(implClassName)) {
            try {
                instanceCache.put(implClassName, implClass.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(String.format("%s 类实例化失败", implClassName), e);
            }
        }
        return (T) instanceCache.get(implClassName);
    }
}
