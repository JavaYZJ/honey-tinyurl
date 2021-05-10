package red.honey.tinyurl.server.application.service;

import red.honey.tinyurl.exception.CustomTinyUrlException;

/**
 * @author yangzhijie
 * @date 2021/4/27 14:04
 */
public interface CustomTinyUrl extends AbstractTinyUrl {

    /**
     * 通过长链接获取短链接
     *
     * @param url     长链接
     * @param tinyUrl 自定义短链接
     * @return 是否成功
     * @throws CustomTinyUrlException 自定义短链接异常
     */
    boolean customTinyUrl(String url, String tinyUrl) throws CustomTinyUrlException;


}
