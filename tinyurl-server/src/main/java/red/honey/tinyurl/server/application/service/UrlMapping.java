package red.honey.tinyurl.server.application.service;

/**
 * @author yangzhijie
 */
public interface UrlMapping {


    /**
     * 长链接映射成短链接
     *
     * @param url 长链接
     * @return 短链接
     */
    String mapping(String url);


    /**
     * 长链接是否存在
     *
     * @param url 长链接
     * @return 是否存在
     */
    boolean isExists(String url);
}
