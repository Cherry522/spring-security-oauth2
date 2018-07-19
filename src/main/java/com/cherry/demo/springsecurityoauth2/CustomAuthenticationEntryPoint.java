package com.cherry.demo.springsecurityoauth2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用来配置如果没有权限访问接口时我们返回的错误码以及错误内容
 * @author chenyan
 * @date 下午3:05
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    public static Logger log = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        //预认证入口点被调用。拒绝访问。
        log.info("Pre-authenticated entry point called.Rejecting access.");
        //返回错误码为401
        //错误信息为：Accesss Denied!
        httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Accesss Denied!");
    }
}
