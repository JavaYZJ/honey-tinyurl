package red.honey.tinyurl.server.application.service;

import red.honey.tinyurl.server.application.constant.TinyUrlType;
import red.honey.tinyurl.server.application.entity.po.TinyUrlPo;

/**
 * @author yangzhijie
 * @date 2021/4/27 14:06
 */
public interface AbstractTinyUrl {

    /**
     * 添加长短链接映射关系
     *
     * @param tinyUrl 长短链接映射关系
     */
    void insertUrl(TinyUrlPo tinyUrl);

    /**
     * 更新长短链接映射关系
     *
     * @param tinyUrl 长短链接映射关系
     */
    void updateUrl(TinyUrlPo tinyUrl);

    /**
     * 获取长链接
     *
     * @param keyword 短链接
     * @param type    短链接类型
     * @return 长链接
     */
    String obtainUrl(String keyword, TinyUrlType type);


    /**
     * 校验长链接是否已经是自定义过的
     *
     * @param url 长链接
     * @return 是否存在
     */
    boolean customMappingHasExist(String url);

    /**
     * 通过长链接获取短链接
     *
     * @param url 长链接
     * @return 短链接
     */
    String obtainTinyUrl(String url);
}


