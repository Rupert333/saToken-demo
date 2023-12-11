package com.example.satoken.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author by shanxf
 * @desc
 * @Date 2021/5/7 17:54
 */
@Data
@Builder
@AllArgsConstructor
public class AccessToken {

    private String accessToken;

    private String expiresIn;

    private String refreshToken;

}
