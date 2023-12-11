package com.example.satoken.util;

import cn.dev33.satoken.stp.SaLoginConfig;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import com.example.satoken.vo.JwtClaim;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

/**
 * @author by shanxf
 * @desc
 * @Date 2021/5/10 10:36
 */

@Slf4j
public class JwtUtil {


    public static String createToken(HashMap<String, Object> claims, Integer expireTime) {
        StpUtil.login(claims.get("appId"), SaLoginConfig
                .setExtra("mobile", claims.get("mobile"))
                .setExtra("appId", claims.get("appId"))
                .setTimeout(expireTime));
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        return tokenInfo.getTokenValue();
    }

    public static JwtClaim parseToken(String token) {
        Assert.notNull(token, "token不能为空");

        if (ObjectUtil.isNotEmpty(StpUtil.getLoginIdByToken(token))) {
            return JwtClaim.builder()
                .mobile((String) StpUtil.getExtra(token, "mobile"))
                .appId((String) StpUtil.getExtra(token, "appId"))
                .build();
        }

     throw new RuntimeException("token不正确");
    }
}
