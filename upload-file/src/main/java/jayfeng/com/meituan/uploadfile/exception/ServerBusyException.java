package jayfeng.com.meituan.uploadfile.exception;

/**
 * @author JayFeng
 * @date 2021/2/10
 */
public class ServerBusyException extends RuntimeException {

    public ServerBusyException(String message) {
        super(message);
    }

}
