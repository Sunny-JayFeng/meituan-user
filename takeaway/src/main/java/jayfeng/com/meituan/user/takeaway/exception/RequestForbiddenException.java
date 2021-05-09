package jayfeng.com.meituan.user.takeaway.exception;

/**
 * 拒绝处理请求，抛出这个异常
 * @author JayFeng
 * @date 2021/4/22
 */
public class RequestForbiddenException extends RuntimeException {

    public RequestForbiddenException(String message) {
        super(message);
    }

}
