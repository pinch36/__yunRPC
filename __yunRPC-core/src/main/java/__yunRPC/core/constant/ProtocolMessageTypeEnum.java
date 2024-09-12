package __yunRPC.core.constant;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: __yun
 * @Date: 2024/09/12/8:56
 * @Description:
 */
public enum ProtocolMessageTypeEnum {
    REQUEST(0),
    RESPONSE(1),
    HEART_BEAT(2),
    OTHERS(3);


    private final int value;

    ProtocolMessageTypeEnum(int value) {
        this.value = value;
    }
    public static ProtocolMessageTypeEnum getEnumByValue(int value){
        for (ProtocolMessageTypeEnum protocolMessageTypeEnum : ProtocolMessageTypeEnum.values()) {
            if (protocolMessageTypeEnum.value == value){
                return protocolMessageTypeEnum;
            }
        }
        return null;
    }
}
