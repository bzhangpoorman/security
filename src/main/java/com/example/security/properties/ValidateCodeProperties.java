package com.example.security.properties;

public class ValidateCodeProperties {
    
    /**
     * 图片验证码配置
     */
    private ImageCodeProperties image = new ImageCodeProperties();
    

    public ImageCodeProperties getImage() {
        return image; 
    }

    public void setImage(ImageCodeProperties image) {
        this.image = image;
    }
}