package jayfeng.com.meituan.loginregistry.exception;

/**
 * 服务端超负荷或停机维护
 * @author JayFeng
 * @date 2021/1/24
 */
public class ServerBusyException extends RuntimeException {

    public ServerBusyException(String message) {
        super(message);
    }

}
