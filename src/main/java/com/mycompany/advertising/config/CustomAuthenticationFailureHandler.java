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
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {//} extends ExceptionMappingAuthenticationFailureHandler {//AuthenticationFailureHandler {
    private static final Logger logger = Logger.getLogger(CustomAuthenticationFailureHandler.class);
//    @Autowired
//    private UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter;

    //to create bean UsernamePasswordAuthenticationFilter requires that you set the authenticationManager property
//    @Autowired
//    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
//        usernamePasswordAuthenticationFilter.setAuthenticationManager(authenticationManager);
//        //super.setAuthenticationManager(authenticationManager);
//    }

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception)
            throws IOException, ServletException {

//        String usernameParameter =
//                usernamePasswordAuthenticationFilter.getUsernameParameter();
        String lastphonenumber = request.getParameter("phonenumber");//usernameParameter);
        logger.debug("User " + lastphonenumber + " failed to login");
        HttpSession session = request.getSession(false);
        if (session != null) request.getSession().setAttribute("LAST_PHONENUMBER", lastphonenumber);
        request.getRequestDispatcher("login_error").forward(request, response);
        //response.sendRedirect("/" + globalSetting.getFailLoginUrl());
    }
/*    @Bean
    public UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter(){
        return new UsernamePasswordAuthenticationFilter();
    }*/
}
