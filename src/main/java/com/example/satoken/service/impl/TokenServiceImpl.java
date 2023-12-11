package com.example.satoken.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.satoken.service.ITokenService;
import com.example.satoken.vo.AccessToken;
import com.example.satoken.vo.JwtClaim;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Slf4j
@Service
public class TokenServiceImpl implements ITokenService {

    private static final String APPID = "dsso";

    @Override
    public AccessToken createToken(String refreshToken, String grantType) {
        AccessToken accessToken = null;
        if ("app_id".equals(grantType)) {
            accessToken = initTokenByAppId();
        } else if ("refresh_token".equals(grantType)) {
            accessToken = refreshToken(refreshToken);
        }
        return accessToken;
    }

    public AccessToken initTokenByAppId() {
        return JwtClaim.builder()
                .appId(APPID)
                .build().convertAccessToken();
    }

    public AccessToken refreshToken(String refreshToken) {
        Assert.notNull(refreshToken, "refreshToken不能为空");

        // 从token中解析信息
        if (ObjectUtil.isNotEmpty(StpUtil.getLoginIdByToken(refreshToken))) {
            String loginId = (String) StpUtil.getLoginIdByToken(refreshToken);
            StpUtil.switchTo(loginId);

            return JwtClaim.builder()
                    .appId((String) StpUtil.getExtra(refreshToken, "appId"))
                    .mobile((String) StpUtil.getExtra(refreshToken, "mobile"))
                    .build().convertAccessToken();
        }

        throw new RuntimeException("refreshToken不正确");
    }
}
