package red.honey.tinyurl.server.application.service.impl;

import lombok.extern.slf4j.Slf4j;
import orestes.bloomfilter.BloomFilter;
import orestes.bloomfilter.FilterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import red.honey.redis.component.HoneyRedisLock;
import red.honey.tinyurl.exception.CustomMappingHasExistException;
import red.honey.tinyurl.server.application.constant.TinyUrlType;
import red.honey.tinyurl.server.application.entity.po.TinyUrlPo;
import red.honey.tinyurl.server.application.service.AbstractTinyUrl;
import red.honey.tinyurl.server.application.service.UrlMapping;
import red.honey.tinyurl.server.util.ConversionUtil;

import javax.annotation.Resource;
import java.util.SplittableRandom;

import static java.lang.Math.pow;
import static red.honey.tinyurl.server.application.constant.BloomFilterConstant.DEFAULT_BLOOM_FILTER_NAME;
import static red.honey.tinyurl.server.application.constant.LockKey.URL_LOCK;

/**
 * @author yangzhijie
 */
@Service
@Slf4j
public class DefaultUrlMapping implements UrlMapping<String> {

    public volatile BloomFilter<String> bloomFilter;
    @Autowired
    private RedisProperties redisProperties;
    @Resource(name = "defaultAbstractTinyUrl")
    private AbstractTinyUrl tinyUrlService;
    @Autowired
    private HoneyRedisLock redisLock;

    /**
     * 长链接映射成短链接
     *
     * @param url 长链接
     * @return 短链接
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String mapping(String url) {
        boolean lock = redisLock.tryLock(URL_LOCK, 1);
        if (lock) {
            if (!isExists(url)) {
                try {
                    TinyUrlPo po = TinyUrlPo.builder().url(url).type(TinyUrlType.DEFAULT).build();
                    tinyUrlService.insertUrl(po);
                    String tinyUrl = tinyUrl(po.getId());
                    po.setKeyword(tinyUrl);
                    tinyUrlService.updateUrl(po);
                    this.bloomFilter.add(url);
                    return tinyUrl;
                } catch (Exception e) {
                    log.error("mapping url to tiny url happen error.", e);
                    throw new CustomMappingHasExistException("mapping url to tiny url happen error.", e);
                }finally {
                    redisLock.unlock(URL_LOCK, 0);
                }
            }else {
                redisLock.unlock(URL_LOCK, 0);
                return tinyUrlService.obtainTinyUrl(url);
            }
        }
        return null;
    }

    /**
     * 运用布隆过滤器检验长链接是否存在
     *
     * @param url 长链接
     * @return 是否存在
     */
    @Override
    public boolean isExists(String url) {

        BloomFilter<String> bloomFilter = createBloomFilter(DEFAULT_BLOOM_FILTER_NAME, Integer.MAX_VALUE, 0.01);
        return bloomFilter.contains(url);
    }

    /**
     * 将十进制id转成62进制的6位短码
     */
    private String tinyUrl(long id) {
        SplittableRandom splittableRandom = new SplittableRandom();
        int rand = splittableRandom.nextInt(1, 10);
        return ConversionUtil.encode((long) (rand * pow(10, 9) + id), 6);
    }

    /**
     * 创建布隆过滤器(单机版)
     *
     * @param filterName               filterName
     * @param expectedElements         expectedElements
     * @param falsePositiveProbability falsePositiveProbability
     * @return BloomFilter
     */
    @Override
    public BloomFilter<String> createBloomFilter(String filterName, int expectedElements, double falsePositiveProbability) {
        if (this.bloomFilter == null) {
            synchronized (this) {
                if (this.bloomFilter == null) {
                    //Open a Redis-backed Bloom filter
                    this.bloomFilter = new FilterBuilder(expectedElements, falsePositiveProbability)
                            //use a distinct name
                            .name(filterName)
                            .redisBacked(true)
                            .redisHost(redisProperties.getHost())
                            .redisPort(redisProperties.getPort())
                            .buildBloomFilter();
                }
            }
        }
        return this.bloomFilter;
    }
}
