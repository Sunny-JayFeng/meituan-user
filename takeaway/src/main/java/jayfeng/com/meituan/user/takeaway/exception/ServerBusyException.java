package jayfeng.com.meituan.user.takeaway.exception;

/**
 * 服务端超负荷或停机维护
 * @author JayFeng
 * @date 2021/4/22
 */
public class ServerBusyException extends RuntimeException {

    public ServerBusyException(String message) {
        super(message);
    }

}
