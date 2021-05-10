package red.honey.tinyurl.server.util;

import orestes.bloomfilter.BloomFilter;
import orestes.bloomfilter.FilterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.stereotype.Component;

/**
 * @author yangzhijie
 * @date 2021/4/27 10:43
 */
@Component
public class BloomFilterUtil<T> {

    @Autowired
    private RedisProperties redisProperties;

    public BloomFilter<T> createBloomFilter(String filterName, int expectedElements,
                                            double falsePositiveProbability) {
        //Open a Redis-backed Bloom filter
        return new FilterBuilder(expectedElements, falsePositiveProbability)
                //use a distinct name
                .name(filterName)
                .redisBacked(true)
                .redisHost(redisProperties.getHost())
                .redisPort(redisProperties.getPort())
                .buildBloomFilter();
    }

}
