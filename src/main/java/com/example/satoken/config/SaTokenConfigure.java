package com.example.satoken.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.jwt.StpLogicJwtForSimple;
import cn.dev33.satoken.stp.StpLogic;
import cn.dev33.satoken.stp.StpUtil;
import com.example.satoken.intercrepter.LogbackInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {
    private static final List<String> IGNORE_LOG_URIS = Arrays.asList("/api/public/*/renewal");


//    /**
//     * 重写 Sa-Token 框架内部算法策略
//     */
//    @Autowired
//    public void rewriteSaStrategy() {
//        // 重写 Token 生成策略
//        SaStrategy.instance.createToken = (loginId, loginType) -> {
//            return SaFoxUtil.getRandomString(60);    // 随机60位长度字符串
//        };
//    }

    // 注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 日志记录
        registry.addInterceptor(new LogbackInterceptor()).addPathPatterns("/**").excludePathPatterns(IGNORE_LOG_URIS);
        // 注册 Sa-Token 拦截器，校验规则为 StpUtil.checkLogin() 登录校验。
        registry.addInterceptor(new SaInterceptor(handle -> StpUtil.checkLogin())).addPathPatterns("/**").excludePathPatterns("/login/**");
    }

    // Sa-Token 整合 jwt (Simple 简单模式)
    @Bean
    public StpLogic getStpLogicJwt() {
        return new StpLogicJwtForSimple();
    }
}
