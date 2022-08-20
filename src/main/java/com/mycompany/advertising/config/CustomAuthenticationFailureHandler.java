package com.mycompany.advertising.config;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by Amir on 11/11/2021.
 */
@Configuration
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private static final Logger logger = Logger.getLogger(CustomAuthenticationFailureHandler.class);

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception)
            throws IOException, ServletException {
//        String usernameParameter =
//                usernamePasswordAuthenticationFilter.getUsernameParameter();
        logger.debug("User " + request.getParameter("username") + " failed to login");
        HttpSession session = request.getSession(false);
        logger.debug("session is " + session);
        logger.debug("exception is " + exception + (exception != null ? " exception message is " + exception.getMessage() : ""));
        if (session != null) {
            request.getSession().setAttribute("exception", exception);
        }
        request.getRequestDispatcher("login_error").forward(request, response);
        //response.sendRedirect("/" + globalSetting.getFailLoginUrl());
    }
}
