package com.example.security.config;

import com.example.security.filter.ValidateCodeFilter;
import com.example.security.handler.TestAuthenticationFailureHandler;
import com.example.security.handler.TestAuthenticationSuccessHandler;
import com.example.security.properties.SecurityProperties;
import com.example.security.repository.RedisPersistentTokenRepository;
import com.example.security.service.TestUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

/**
 * @CreateTime 2020/3/10
 * @Autor bzhang
 **/
@Configuration
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {
    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private TestAuthenticationSuccessHandler testAuthenticationSuccessHandler;

    @Autowired
    private TestAuthenticationFailureHandler testAuthenticationFailureHandler;

    @Autowired
    private TestUserDetailsService testUserDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        System.out.println(securityProperties.getBrowser().getLoginPage());
        ValidateCodeFilter validateCodeFilter = new ValidateCodeFilter();
        validateCodeFilter.setSecurityProperties(securityProperties);
        validateCodeFilter.setAuthenticationFailureHandler(testAuthenticationFailureHandler);
        validateCodeFilter.afterPropertiesSet();

        http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class).formLogin()
                .loginPage("/authentication/require").loginProcessingUrl("/form")
                .successHandler(testAuthenticationSuccessHandler)
                .failureHandler(testAuthenticationFailureHandler)
                .and()
                .logout().logoutUrl("/logout")
                .and().rememberMe()
                .tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds())
                .userDetailsService(testUserDetailsService)
                // http.httpBasic()
                .and().authorizeRequests()// 表示下面是认证的配置
                .antMatchers("/authentication/require","/code/image",securityProperties.getBrowser().getLoginPage())
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
