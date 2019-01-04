package com.dyzhsw.efficient.config.jwt;

/**
 * @author zhaoqingjie
 * 登录返回
 */
public class LoginResponse {

    /**
     * jwt验证token
     */
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
