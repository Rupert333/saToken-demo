package com.example.satoken.rest;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import cn.dev33.satoken.util.SaResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Sa-Token 注解鉴权示例 
 * 
 * @author kong
 * @since 2022-10-13
 */
@RestController
@RequestMapping("/at-check/")
public class MethodCheckController {

    /*
     * 前提1：首先调用登录接口进行登录
     *      ---- http://localhost:8081/doLogin?name=zhang&pwd=123456
     * 
     * 前提2：项目在配置类中注册拦截器 SaInterceptor ，此拦截器将打开注解鉴权功能 
     * 
     * 前提3：项目实现了 StpInterface 接口，此接口会告诉框架指定账号拥有哪些权限码
     * 
     * 然后我们就可以使用以下示例中的代码进行注解鉴权了 
     */

    // 权限校验   ---- http://localhost:8081/at-check/checkPermission
    //      只有具有 user.add 权限的账号才可以进入方法 
    @SaCheckPermission("user.add")
    @RequestMapping("checkPermission")
    public SaResult checkPermission() {
        // ... 
        return SaResult.ok();
    }

    // 角色校验   ---- http://localhost:8081/at-check/checkRole
    //      只有具有 super-admin 角色的账号才可以进入方法 
    @SaCheckRole("super-admin")
    @RequestMapping("checkRole")
    public SaResult checkRole() {
        // ... 
        return SaResult.ok();
    }

    // 注解式鉴权：只要具有其中一个权限即可通过校验
    @RequestMapping("atJurOr")
    @SaCheckPermission(value = {"user-add", "user-all", "user-delete"}, mode = SaMode.OR)
    public SaResult atJurOr() {
        return SaResult.data("用户信息");
    }
}