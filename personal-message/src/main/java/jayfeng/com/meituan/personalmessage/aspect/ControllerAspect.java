package jayfeng.com.meituan.personalmessage.aspect;

import jayfeng.com.meituan.personalmessage.exception.RequestForbiddenException;
import jayfeng.com.meituan.personalmessage.exception.ServerBusyException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求接口切面
 * @author JayFeng
 * @date 2021/1/20
 */
@Aspect
@Component
public class ControllerAspect {

    Logger logger = LoggerFactory.getLogger(ControllerAspect.class);

    // 存放每个线程来请求的开始时间 key 为线程 id
    private Map<Long, ThreadLocal<Long>> threadLocalMap = new HashMap<>(16384);

    /**
     * 配置切点
     * 所有的请求都必须判断是否来自浏览器
     * execution 表达式
     * 第一个 * 表示返回类型，* 表示所有类型。这里实则是：jayfeng.meituan.personalmessage.response.ResponseMessage
     * 接下来是包名
     * controller.类名  哪一个类, * 表示所有类
     * controller.类名.方法名(参数)  * 表示所有方法 (..) 表示任何参数
     */
    @Pointcut("execution(* jayfeng.meituan.personalmessage.controller.*.*(..))")
    public void requestInterface() {

    }

    /**
     * 接口业务逻辑处理之前
     * @param joinPoint 切点
     */
    @Before("requestInterface()")
    public void requestInterfaceDoBefore(JoinPoint joinPoint) {
        ThreadLocal<Long> startTimeThreadLocal = new ThreadLocal<>();
        startTimeThreadLocal.set(System.currentTimeMillis());
        threadLocalMap.put(Thread.currentThread().getId(), startTimeThreadLocal);

        HttpServletRequest request = this.getHttpServletRequest();
        if (!isRequestFromBrowser(request)) {
            logger.info("请求不是来自浏览器, 拒绝处理");
            throw new RequestForbiddenException("您无权访问该服务");
        }
    }

    /**
     * 请求是否来自浏览器
     * @param request HttpServletRequest 用于获取请求头数据
     * @return 返回请求是否来自浏览器
     */
    private Boolean isRequestFromBrowser(HttpServletRequest request) {
        String requestFrom = request.getHeader("request-from");
        return "browser".equals(requestFrom);
    }

    /**
     * 获取 ServletRequestAttributes 对象
     * @return 返回 ServletRequestAttributes 对象
     */
    private ServletRequestAttributes getServletRequestAttributes() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        if (servletRequestAttributes == null) {
            logger.info("请求无法获取到 ServletRequestAttributes 对象");
            throw new ServerBusyException("服务器繁忙");
        }
        return servletRequestAttributes;
    }

    /**
     * 获取 HttpServletRequest 对象
     * @return 返回 HttpServletRequest 对象
     */
    private HttpServletRequest getHttpServletRequest() {
        return this.getServletRequestAttributes().getRequest();
    }

    /**
     * 获取 HttpServletResponse 对象
     * @return  返回 HttpServletResponse 对象
     */
    private HttpServletResponse getHttpServletResponse () {
        return this.getServletRequestAttributes().getResponse();
    }

    /**
     * 接口业务逻辑处理之后
     * @param result 请求结果数据
     */
    @AfterReturning(returning = "result", pointcut = "requestInterface()")
    public void requestInterfaceDoAfterReturning(Object result) {
        ThreadLocal<Long> startTimeThreadLocal = threadLocalMap.get(Thread.currentThread().getId());
        logger.info("请求耗时: {}ms", System.currentTimeMillis() - startTimeThreadLocal.get());
        logger.info("请求结果: {}", result);
    }

}
