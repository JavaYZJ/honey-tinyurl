package red.honey.tinyurl.server.api.web;

import com.eboy.honey.response.commmon.HoneyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import red.honey.redis.component.HoneyRedisLock;
import red.honey.tinyurl.server.application.constant.TinyUrlType;
import red.honey.tinyurl.server.application.service.AbstractTinyUrl;
import red.honey.tinyurl.server.application.service.UrlMapping;
import red.honey.tinyurl.server.application.service.impl.VipTinyUrl;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * @author yangzhijie
 */
@RestController
@RequestMapping
public class TinyUrlController {

    private final TinyUrlType NO_TINY_URL_TYPE = null;
    @Resource(name = "vipTinyUrl")
    private VipTinyUrl vipTinyUrl;
    @Autowired
    private UrlMapping<String> defaultUrlMapping;
    @Autowired
    private AbstractTinyUrl defaultAbstractTinyUrl;
    @Autowired
    private HoneyRedisLock redisLock;

    @GetMapping("/v1/tinyUrl")
    public HoneyResponse<String> getTinyUrl(String url) {
        return HoneyResponse.success(vipTinyUrl.obtainTinyUrl(url));
    }

    @GetMapping("/v1/tinyUrl/vip")
    public HoneyResponse<Boolean> getUrl(String url, String customTinyUrl) {
        return HoneyResponse.success(vipTinyUrl.customTinyUrl(url, customTinyUrl));
    }

    @GetMapping("/{tinyUrl}")
    public void sendRedirect(HttpServletResponse response, @PathVariable String tinyUrl) {
        String url = vipTinyUrl.obtainUrl(tinyUrl, NO_TINY_URL_TYPE);
        response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
        response.setHeader("Location", url);
    }

    @PostConstruct
    public void init() {
        this.vipTinyUrl.setUrlMapping(defaultUrlMapping);
        this.vipTinyUrl.setTinyUrlService(defaultAbstractTinyUrl);
        this.vipTinyUrl.setRedisLock(redisLock);
    }
}
