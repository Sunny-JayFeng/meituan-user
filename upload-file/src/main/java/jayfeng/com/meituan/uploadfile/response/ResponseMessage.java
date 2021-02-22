package jayfeng.com.meituan.uploadfile.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 响应信息返回实体
 * @author JayFeng
 * @date 2021/2/2
 */
@Data
@AllArgsConstructor
public class ResponseMessage {

    /**
     * 请求码 去 redis 取
     */
    private Integer code;

    /**
     * 请求信息
     */
    private String requestMessage;

    /**
     * 响应码，正常请求都是 200
     * 出异常为 5xx
     * 拒绝处理等为 4xx
     * 未知异常为 999
     */
    private Integer responseCode;

    /**
     * 请求结果
     * 成功(0) or 失败(1)
     */
    private Integer requestStatus;

    /**
     * 请求数据
     */
    private Object responseData;


}
