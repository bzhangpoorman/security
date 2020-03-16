package com.example.security.config;

import com.example.security.handler.TestAuthenticationFailureHandler;
import com.example.security.handler.TestAuthenticationSuccessHandler;
import com.example.security.properties.SecurityProperties;
import com.example.security.repository.RedisPersistentTokenRepository;
import com.example.security.service.TestUserDetailsService;
import com.example.security.type.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

/**
 * @CreateTime 2020/3/10
 * @Autor bzhang
 **/
@Configuration
public class SecurityConfigurer extends AbstractChannelSecurityConfig {
    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private TestAuthenticationSuccessHandler testAuthenticationSuccessHandler;

    @Autowired
    private TestAuthenticationFailureHandler testAuthenticationFailureHandler;

    @Autowired
    private TestUserDetailsService testUserDetailsService;

    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    @Autowired
    private ValidateCodeSecurityConfig validateCodeSecurityConfig;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        applyPasswordAuthenticationConfig(http);

        http.apply(validateCodeSecurityConfig)
                .and()
                .apply(smsCodeAuthenticationSecurityConfig)
                .and()
                .logout().logoutUrl("/logout")
                .and().rememberMe()
                .tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds())
                .userDetailsService(testUserDetailsService)
                // http.httpBasic()
                .and().authorizeRequests()// 表示下面是认证的配置
                .antMatchers(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL,
                        SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_MOBILE,
                        "/code/*",
                        securityProperties.getBrowser().getLoginPage())
                .permitAll()
                .anyRequest()// 任何请求
                .authenticated()// 都需要身份认证
                .and()
                .csrf().disable();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        return new RedisPersistentTokenRepository();
    }
}
