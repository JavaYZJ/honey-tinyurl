package red.honey.tinyurl.server.application.constant;

/**
 * @author yangzhijie
 * @date 2021/4/27 15:04
 */
public enum TinyUrlType {

    /**
     * 系统默认
     */
    DEFAULT(0),

    /**
     * 自定义
     */
    CUSTOM(1);

    private int code;

    TinyUrlType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
