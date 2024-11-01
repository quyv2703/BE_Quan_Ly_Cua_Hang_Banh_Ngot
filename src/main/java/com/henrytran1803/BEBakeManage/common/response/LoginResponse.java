package com.henrytran1803.BEBakeManage.common.response;

public class LoginResponse {
    String token;

    public String getToken() {
        return token;
    }

    public LoginResponse(String token) {
        this.token = token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
