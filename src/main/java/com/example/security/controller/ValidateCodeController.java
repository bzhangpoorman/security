package com.example.security.controller;

import com.example.security.bean.ImageCode;
import com.example.security.bean.ValidateCode;
import com.example.security.generator.ValidateCodeGenerator;
import com.example.security.processor.ValidateCodeProcessorHolder;
import com.example.security.sms.SmsCodeSender;
import com.example.security.type.SecurityConstants;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@RestController
public class ValidateCodeController {

    public static final String SESSION_KEY = "SESSION_KEY_IMAGE_CODE";
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();
    @Qualifier("imageValidateCodeGenerator")
    @Autowired
    private ValidateCodeGenerator imageCodeGenerator;

    @Qualifier("smsValidateCodeGenerator")
    @Autowired
    private ValidateCodeGenerator smsCodeGenerator;

    @Autowired
    private SmsCodeSender smsCodeSender;

    /**
     * 创建验证码，根据验证码类型不同，调用不同的 接口实现
     * 
     * @param request
     * @param response
     * @throws Exception
     */
    @GetMapping("/code/image")
    public void createCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ValidateCode code = imageCodeGenerator.generate(new ServletWebRequest(request));
        ImageCode imageCode = (ImageCode)code;
        sessionStrategy.setAttribute(new ServletWebRequest(request), SESSION_KEY, imageCode);
        ImageIO.write(imageCode.getImage(), "JPEG", response.getOutputStream());
    }

    /**
     * 创建生成短信验证码
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @GetMapping("/code/sms")
    @ApiOperation(value = "获取短信验证码")
    public void createSmsCode(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletRequestBindingException {
        ValidateCode smsCode = smsCodeGenerator.generate(new ServletWebRequest(request));
        // 将短信验证码放入session
        sessionStrategy.setAttribute(new ServletWebRequest(request), SESSION_KEY, smsCode);
        // 从get请求中获取需要发送验证码的手机号
        String mobile = ServletRequestUtils.getRequiredStringParameter(request, "mobile");
        smsCodeSender.send(mobile, smsCode.getCode());
    }

//    @Autowired
//    private ValidateCodeProcessorHolder validateCodeProcessorHolder;
//    /**
//     * 创建验证码，根据验证码类型不同，调用不同的 {@link ValidateCodeProcessor}接口实现
//     *
//     * @param request
//     * @param response
//     * @param type
//     * @throws Exception
//     */
//    @GetMapping(SecurityConstants.DEFAULT_VALIDATE_CODE_URL_PREFIX + "/{type}")
//    public void createCode(HttpServletRequest request, HttpServletResponse response, @PathVariable String type)
//            throws Exception {
//        validateCodeProcessorHolder.findValidateCodeProcessor(type).create(new ServletWebRequest(request, response));
//    }

}