package __yunRPC.core.util;

import java.io.InputStream;

public class CommonUtils {

public static InputStream readResourceFile(String fileName) {
return CommonUtils.class.getClassLoader().getResourceAsStream(fileName);
}
}