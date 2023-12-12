package com.example.satoken.intercrepter;

import cn.dev33.satoken.stp.StpUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @author by shanxf
 * @desc
 * @Date 2021/5/6 17:54
 */
@Slf4j
public class LogbackInterceptor implements HandlerInterceptor {

    public static final String APPID = "appId";
    public static final String FLOW_ID = "flowId";
    private static final Long INFLUENCE_DURATION = 500L;

    public static final String START_TIME = "start";

    private ThreadLocal<Long> startTime = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("进入LogbackInterceptor-preHandle");
        startTime.set(System.currentTimeMillis());
        MDC.put(START_TIME, String.valueOf(startTime.get()));
        MDC.put(FLOW_ID, UUID.randomUUID().toString());
        log.info("步出LogbackInterceptor-preHandle");
        StpUtil.login(1001);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("进入LogbackInterceptor-afterCompletion");
        MDC.remove(APPID);
        MDC.remove(FLOW_ID);
        MDC.remove(START_TIME);
        if (log.isDebugEnabled()) {
            long spentTime = System.currentTimeMillis() - startTime.get();
            if (spentTime > INFLUENCE_DURATION) {
                log.debug("found slow request {}, it spent {} milliseconds.", request.getRequestURL(), spentTime);
            }
        }
        log.info("步出LogbackInterceptor-afterCompletion");
        startTime.remove();
    }
}
