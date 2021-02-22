package jayfeng.com.meituan.personalmessage.exception;

/**
 * @author JayFeng
 * @date 2021/2/9
 */
public class RequestForbiddenException extends RuntimeException {

    public RequestForbiddenException(String message) {
        super(message);
    }

}
