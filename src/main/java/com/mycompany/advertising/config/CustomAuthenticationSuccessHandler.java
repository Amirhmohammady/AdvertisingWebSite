package com.mycompany.advertising.config;

/**
 * Created by Amir on 12/4/2021.
 */
//comment because dont need to handle AuthenticationSuccessHandler manually
//@Configuration
public class CustomAuthenticationSuccessHandler {
}/* implements AuthenticationSuccessHandler {
    @Value("${max.inactive.interval}")
    private int maxinactiveinterval;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        httpServletRequest.getSession().setMaxInactiveInterval(maxinactiveinterval);
    }
}
*/