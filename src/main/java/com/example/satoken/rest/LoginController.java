package com.example.satoken.rest;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录认证注解测试
 */
@RestController
public class LoginController {

    // 访问 home 页，登录后才能访问  ---- http://localhost:8081/home
    @SaCheckLogin
    @RequestMapping("home")
    public SaResult home() {
        return SaResult.ok("访问成功，此处为登录后才能看到的信息");
    }

    // 登录接口  ---- http://localhost:8081/doLogin?name=zhang&pwd=123456
    @RequestMapping("doLogin")
    public SaResult doLogin(String name, String pwd) {
        // 此处仅作模拟示例，真实项目需要从数据库中查询数据进行比对
        if("zhang".equals(name) && "123456".equals(pwd)) {
            StpUtil.login(10001);
            return SaResult.ok("登录成功");
        }
        return SaResult.error("登录失败");
    }

}