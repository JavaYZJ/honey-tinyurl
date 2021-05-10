package red.honey.tinyurl.server.application.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import red.honey.tinyurl.server.application.constant.TinyUrlType;
import red.honey.tinyurl.server.application.entity.po.TinyUrlPo;

/**
 * @author yangzhijie
 */
@Mapper
public interface UrlMappingMapper {


    /**
     * 添加长短链接映射关系
     *
     * @param tinyUrl 长短链接映射关系
     * @return 是否成功
     */
    boolean insertUrl(@Param("tinyUrl") TinyUrlPo tinyUrl);

    /**
     * 更新长短链接映射关系
     *
     * @param tinyUrl 长短链接映射关系
     * @return 是否成功
     */
    boolean updateUrl(@Param("tinyUrl") TinyUrlPo tinyUrl);


    /**
     * 获取长链接
     *
     * @param keyword 短链接
     * @param type    短链接类型
     * @return 长链接
     */
    String obtainUrl(@Param("keyword") String keyword, @Param("type") TinyUrlType type);


    /**
     * 校验长链接是否已经是自定义过的
     *
     * @param url 长链接
     * @return 是否存在
     */
    boolean customMappingHasExist(@Param("url") String url);

    /**
     * 通过长链接获取短链接
     *
     * @param url 长链接
     * @return 短链接
     */
    String obtainTinyUrl(@Param("url") String url);
}
