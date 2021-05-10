package red.honey.tinyurl.server.application.service;

/**
 * @author yangzhijie
 */
public interface CustomTinyUrlStrategy {

    /**
     * 当url已存在时 是否支持自定义短码
     *
     * @return 是否支持
     */
    boolean shouldSupportCustomOnUrlExist();


    /**
     * 是否支持对同一个url进行多次自定义短码
     *
     * @return 是否支持
     */
    boolean shouldSupportMultiCustom();
}
