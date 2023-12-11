package com.example.satoken.rest;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.SaLoginConfig;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.example.satoken.service.ITokenService;
import com.example.satoken.vo.AccessToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * 登录认证注解测试
 */
@Slf4j
@RestController
@RequestMapping("/login/")
@RequiredArgsConstructor
public class LoginController {
    private final ITokenService tokenService;

    // 访问 home 页，登录后才能访问  ---- http://localhost:8081/home
    @SaCheckLogin
    @RequestMapping("home")
    public SaResult home() {
        return SaResult.ok("访问成功，此处为登录后才能看到的信息");
    }

    // 前后端一体模式的登录样例    ---- http://localhost:8081/NotCookie/doLogin?name=zhang&pwd=123456
    @RequestMapping("doLogin")
    public SaResult doLogin(String name, String pwd) {
        if("zhang".equals(name) && "123456".equals(pwd)) {
            // 会话登录
            StpUtil.login(10001);
            return SaResult.ok();
        }
        return SaResult.error("登录失败");
    }

    // 前后端分离模式的登录样例    ---- http://localhost:8081/NotCookie/doLogin2?name=zhang&pwd=123456
    @RequestMapping("doLogin2")
    public SaResult doLogin2(String name, String pwd, Integer expireTime) {

        if("zhang".equals(name) && "123456".equals(pwd)) {

            // 会话登录
            StpUtil.login(10001, expireTime);
            StpUtil.login(10001, expireTime * 10);

            // 与常规登录不同点之处：这里需要把 Token 信息从响应体中返回到前端
            SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
            return SaResult.data(tokenInfo);
        }
        return SaResult.error("登录失败");
    }

    // 前后端分离模式的登录样例    ---- http://localhost:8081/NotCookie/doLogin2?name=zhang&pwd=123456
    @RequestMapping("doLogin3")
    public SaResult doLogin3(String name, String pwd, Integer expireTime) {


        // 用户1
        // 连缀写法追加多个
        StpUtil.login(10001, SaLoginConfig
                .setExtra("name", name)
                .setExtra("pwd", pwd)
                .setExtra("role", "超级管理员")
                .setTimeout((long) expireTime));
        // 用户2
        StpUtil.login(10002, SaLoginConfig
                .setExtra("name", "yyg")
                .setExtra("pwd", "yyg")
                .setExtra("role", "超级管理员")
                .setTimeout((long) expireTime));

        // 与常规登录不同点之处：这里需要把 Token 信息从响应体中返回到前端
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        return SaResult.data(tokenInfo);
    }

    @GetMapping("/token")
    public SaResult token(String refreshToken, String grantType) {
        AccessToken token = tokenService.createToken(refreshToken, grantType);
        if (Objects.isNull(token)) {
            log.error("获取token失败");
            return SaResult.error();
        } else {
            log.info("成功获取token: {}", token.getAccessToken());
            return SaResult.data(token);
        }
    }
}