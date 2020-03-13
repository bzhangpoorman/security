package com.example.security.properties;

public class ValidateCodeProperties {
    
    /**
     * 图片验证码配置
     */
    private ImageCodeProperties image = new ImageCodeProperties();

    /**
     * 短信验证码
     */
    private SmsCodeProperties smsCode = new SmsCodeProperties();

    public SmsCodeProperties getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(SmsCodeProperties smsCode) {
        this.smsCode = smsCode;
    }

    public ImageCodeProperties getImage() {
        return image; 
    }

    public void setImage(ImageCodeProperties image) {
        this.image = image;
    }
}