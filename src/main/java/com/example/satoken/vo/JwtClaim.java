package com.example.satoken.vo;

import com.example.satoken.util.JwtUtil;
import lombok.*;

import java.util.HashMap;

import static com.example.satoken.constants.Constants.ACCESS_TOKEN_EXPIRE;


/**
 * @desc
 * @Date 2021/5/7 18:32
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class JwtClaim {

    private String mobile;

    private String appId;

    public String createRefreshToken() {
        return JwtUtil.createToken(convertMap(), 60 * 60 * 24);
    }

    public String createToken() {
        return JwtUtil.createToken(convertMap(), Integer.valueOf(ACCESS_TOKEN_EXPIRE));
    }

    private HashMap<String, Object> convertMap() {
        return new HashMap<>(8) {
            {
                put("mobile", getMobile());
                put("appId", getAppId());
            }
        };
    }

    public AccessToken convertAccessToken() {
        return new AccessToken(this.createToken(), ACCESS_TOKEN_EXPIRE, this.createRefreshToken());
    }
}
