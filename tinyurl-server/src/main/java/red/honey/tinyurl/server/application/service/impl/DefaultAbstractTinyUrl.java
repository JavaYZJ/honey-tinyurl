package red.honey.tinyurl.server.application.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import red.honey.tinyurl.exception.CustomTinyUrlLengthOver;
import red.honey.tinyurl.exception.TinyUrlHasExistException;
import red.honey.tinyurl.server.application.constant.TinyUrlType;
import red.honey.tinyurl.server.application.dao.UrlMappingMapper;
import red.honey.tinyurl.server.application.entity.po.TinyUrlPo;
import red.honey.tinyurl.server.application.service.AbstractTinyUrl;

/**
 * @author yangzhijie
 */
@Component
public class DefaultAbstractTinyUrl extends DefaultUrlMapping implements AbstractTinyUrl {


    @Autowired
    public RedisProperties redisProperties;

    @Autowired
    private UrlMappingMapper mapper;

    /**
     * 添加长短链接映射关系
     *
     * @param tinyUrl 长短链接映射关系
     */
    @Override
    public void insertUrl(TinyUrlPo tinyUrl) {
        try {
            mapper.insertUrl(tinyUrl);
        } catch (DuplicateKeyException ex) {
            throw new TinyUrlHasExistException("custom tiny url [ " + tinyUrl.getKeyword() + "] has exist.", ex);
        } catch (DataIntegrityViolationException e) {
            throw new CustomTinyUrlLengthOver("custom tiny url length more than 7 bit.", e);
        }
    }

    /**
     * 更新长短链接映射关系
     *
     * @param tinyUrl 长短链接映射关系
     */
    @Override
    public void updateUrl(TinyUrlPo tinyUrl) {
        mapper.updateUrl(tinyUrl);
    }

    /**
     * 获取长链接
     *
     * @param keyword 短链接
     * @param type    短链接类型
     * @return 长链接
     */
    @Override
    public String obtainUrl(String keyword, TinyUrlType type) {
        return mapper.obtainUrl(keyword, type);
    }

    /**
     * 校验长链接是否已经是自定义过的
     *
     * @param url 长链接
     * @return 是否存在
     */
    @Override
    public boolean customMappingHasExist(String url) {
        return mapper.customMappingHasExist(url);
    }


    /**
     * 通过长链接获取短链接
     *
     * @param url 长链接
     * @return 短链接
     */
    @Override
    public String obtainTinyUrl(String url) {
        return mapper.obtainTinyUrl(url);
    }


}
