package red.honey.tinyurl.server.application.service.impl;

import red.honey.tinyurl.server.application.service.CustomTinyUrlStrategy;

/**
 * @author yangzhijie
 */
public class DefaultCustomTinyUrlStrategy implements CustomTinyUrlStrategy {

    /**
     * 当url已存在时 是否支持自定义短码
     *
     * @return 是否支持
     */
    @Override
    public boolean shouldSupportCustomOnUrlExist() {
        return true;
    }

    /**
     * 是否支持对同一个url进行多次自定义短码
     *
     * @return 是否支持
     */
    @Override
    public boolean shouldSupportMultiCustom() {
        return true;
    }
}
