package com.example.security.properties;

import com.example.security.type.LoginResponseType;

public class BrowserProperties {
    private String loginPage="/testlogin";

    private String loginResponseType = LoginResponseType.JSON;

    private int rememberMeSeconds = 3600;

    public int getRememberMeSeconds() {
        return rememberMeSeconds;
    }

    public void setRememberMeSeconds(int rememberMeSeconds) {
        this.rememberMeSeconds = rememberMeSeconds;
    }

    public String getLoginResponseType() {
        return loginResponseType;
    }

    public void setLoginResponseType(String loginResponseType) {
        this.loginResponseType = loginResponseType;
    }

    public String getLoginPage() {
        return loginPage;
    }

    public void setLoginPage(String loginPage) {
        this.loginPage = loginPage;
    }
    
}