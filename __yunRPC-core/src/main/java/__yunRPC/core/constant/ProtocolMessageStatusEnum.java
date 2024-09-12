package __yunRPC.core.constant;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: __yun
 * @Date: 2024/09/12/8:56
 * @Description:
 */
public enum ProtocolMessageStatusEnum {
    OK("ok",20);
    private final String text;
    private final int value;

    ProtocolMessageStatusEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }
    public static ProtocolMessageStatusEnum getEnumByValue(int value){
        for (ProtocolMessageStatusEnum protocolMessageStatusEnum : ProtocolMessageStatusEnum.values()) {
            if (protocolMessageStatusEnum.value == value){
                return protocolMessageStatusEnum;
            }
        }
        return null;
    }
}
