package com.example.satoken.service;

import com.example.satoken.vo.AccessToken;

/**
 * token service
 *
 * @author yangyg
 * @date 2023/12/11 2:24 PM
 */
public interface ITokenService {

    AccessToken createToken(String refreshToken, String grantType);
}
