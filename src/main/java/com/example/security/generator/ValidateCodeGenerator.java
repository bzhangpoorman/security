package com.example.security.generator;

import com.example.security.bean.ImageCode;
import com.example.security.bean.ValidateCode;
import org.springframework.web.context.request.ServletWebRequest;

public interface ValidateCodeGenerator {
    ValidateCode generate(ServletWebRequest request);
}