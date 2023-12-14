package com.example.satoken.rest;

import cn.dev33.satoken.annotation.SaCheckPermission;
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
@RequestMapping("/multi-check/")
public class MultiCheckController {

    @SaCheckPermission(value = "user.add", orRole = "admin")
    @RequestMapping("checkPermission")
    public SaResult checkPermission() {
        // ... 
        return SaResult.ok();
    }
}