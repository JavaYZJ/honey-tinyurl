package red.honey.tinyurl.server.application.service;

/**
 * @author yangzhijie
 * @date 2021/4/27 11:20
 */
public interface BloomFilter<T> {


    /**
     * 创建布隆过滤器
     *
     * @param filterName               filterName
     * @param expectedElements         expectedElements
     * @param falsePositiveProbability falsePositiveProbability
     * @return BloomFilter
     */
    orestes.bloomfilter.BloomFilter<T> createBloomFilter(String filterName, int expectedElements,
                                                         double falsePositiveProbability);
}
