package jayfeng.com.meituan.loginregistry.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

/**
 * 时间戳-日期处理工具
 * @author JayFeng
 * @date 2021/1/12
 */
@Component
public class DateUtil {

    private Logger logger = LoggerFactory.getLogger(DateUtil.class);
    private Long boundTime = 24 * 3600 * 14 * 1000L; // 极限时间 14 天

    /**
     * 毫秒值转日期
     * @param timeMillis 时间戳
     * @return
     */
    public LocalDate getDate(Long timeMillis) {
        return Instant.ofEpochMilli(timeMillis).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * 是否超过极限时间（14天）
     * @param oldTime 时间戳
     * @return
     */
    public Boolean timeIsOverBoundTime(Long oldTime) {
        Long nowTime = System.currentTimeMillis();
        Boolean result = (nowTime - oldTime) <= boundTime;
        logger.info("timeIsOverBoundTime, nowTime: {}, oldTime: {}, result: {}", getDate(nowTime), getDate(oldTime), result);
        return result;
    }

}
