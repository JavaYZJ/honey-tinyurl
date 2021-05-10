package red.honey.tinyurl.server.application.entity.po;

import lombok.Builder;
import lombok.Data;
import red.honey.tinyurl.server.application.constant.TinyUrlType;

import java.util.Date;

/**
 * @author yangzhijie
 */
@Data
@Builder
public class TinyUrlPo {

    /**
     * 自增id
     */
    private long id;

    /**
     * 长链接(原始url)
     */
    private String url;

    /**
     * 短链接
     */
    private String keyword;

    /**
     * 短链接类型
     * 0 系统 1 自定义
     */
    private TinyUrlType type;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}
