package red.honey.tinyurl.server.application.service.impl;

import org.springframework.stereotype.Component;
import red.honey.redis.component.HoneyRedisLock;
import red.honey.tinyurl.exception.CustomMappingHasExistException;
import red.honey.tinyurl.exception.CustomTinyUrlException;
import red.honey.tinyurl.server.application.constant.TinyUrlType;
import red.honey.tinyurl.server.application.entity.po.TinyUrlPo;
import red.honey.tinyurl.server.application.service.AbstractTinyUrl;
import red.honey.tinyurl.server.application.service.CustomTinyUrl;
import red.honey.tinyurl.server.application.service.CustomTinyUrlStrategy;
import red.honey.tinyurl.server.application.service.UrlMapping;

import static red.honey.tinyurl.server.application.constant.BloomFilterConstant.DEFAULT_BLOOM_FILTER_NAME;
import static red.honey.tinyurl.server.application.constant.LockKey.CUSTOM_TINY_URL_LOCK;

/**
 * @author yangzhijie
 */
@Component
public class VipTinyUrl extends DefaultAbstractTinyUrl implements CustomTinyUrl {

    private HoneyRedisLock redisLock;
    private UrlMapping urlMapping;
    private CustomTinyUrlStrategy strategy = new DefaultCustomTinyUrlStrategy();
    private AbstractTinyUrl tinyUrlService;

    public VipTinyUrl() {
    }

    public VipTinyUrl(UrlMapping urlMapping, AbstractTinyUrl tinyUrlService) {
        this.urlMapping = urlMapping;
        this.tinyUrlService = tinyUrlService;
    }

    public VipTinyUrl(UrlMapping urlMapping, CustomTinyUrlStrategy strategy, AbstractTinyUrl tinyUrlService) {
        this.urlMapping = urlMapping;
        this.strategy = strategy;
        this.tinyUrlService = tinyUrlService;
    }

    public void setUrlMapping(UrlMapping urlMapping) {
        this.urlMapping = urlMapping;
    }

    public void setStrategy(CustomTinyUrlStrategy strategy) {
        this.strategy = strategy;
    }

    public void setTinyUrlService(AbstractTinyUrl tinyUrlService) {
        this.tinyUrlService = tinyUrlService;
    }

    public void setRedisLock(HoneyRedisLock redisLock) {
        this.redisLock = redisLock;
    }


    /**
     * ??????????????????????????????
     *
     * @param url ?????????
     * @return ?????????
     */
    @Override
    public String obtainTinyUrl(String url) {
        return super.mapping(url);
    }

    /**
     * ??????????????????????????????
     *
     * @param url     ?????????
     * @param tinyUrl ??????????????????
     * @return ????????????
     */
    @Override
    public boolean customTinyUrl(String url, String tinyUrl) {
        boolean lock = redisLock.tryLock(CUSTOM_TINY_URL_LOCK, 2);
        if (lock) {
            TinyUrlPo tinyUrlPo = TinyUrlPo.builder().url(url).keyword(tinyUrl).type(TinyUrlType.CUSTOM).build();
            try {
                if (!this.urlMapping.isExists(url)) {
                    tinyUrlService.insertUrl(tinyUrlPo);
                    return this.createBloomFilter(DEFAULT_BLOOM_FILTER_NAME, Integer.MAX_VALUE, 0.01).add(url);
                }
                // ???????????????????????????????????????????????????????????????
                // ????????????????????????????????????
                if (this.strategy.shouldSupportCustomOnUrlExist()) {
                    if (this.strategy.shouldSupportMultiCustom()) {
                        tinyUrlService.insertUrl(tinyUrlPo);
                        return true;
                    }
                    if (!tinyUrlService.customMappingHasExist(url)) {
                        tinyUrlService.insertUrl(tinyUrlPo);
                        return true;
                    }
                    throw new CustomMappingHasExistException("This url has been custom tiny url.");
                }
            } catch (Exception e) {
                throw new CustomTinyUrlException(e);
            } finally {
                redisLock.unlock(CUSTOM_TINY_URL_LOCK);
            }
        }else {
            redisLock.unlock(CUSTOM_TINY_URL_LOCK);
        }
        return false;
    }

}
