package com.mycompany.advertising.config;

import com.mycompany.advertising.service.UserDetailsServiceImpl;
import com.mycompany.advertising.service.api.LockerApiService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.invoke.MethodHandles;

/**
 * Created by Amir on 12/4/2021.
 */
//comment because dont need to handle AuthenticationSuccessHandler manually
@Configuration
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Value("${max.inactive.interval.seconds.remember.me}")
    private int maxinactiveinterval;
    @Autowired
    private LockerApiService lockerApiService;
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        httpServletRequest.getSession().setMaxInactiveInterval(maxinactiveinterval);
        String username = httpServletRequest.getParameter("username");
        logger.info("User " + username + " successfully logged in.");
        try {
            lockerApiService.removeLastLockByVariable(UserDetailsServiceImpl.class.getMethod("loadUserByUsername", String.class), username);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, "/");
    }
}
