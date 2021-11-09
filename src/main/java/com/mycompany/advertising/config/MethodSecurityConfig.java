package com.mycompany.advertising.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

/**
 * Created by Amir on 11/8/2021.
 */
//comment because defined in WebSecurityConfig by @EnableGlobalMethodSecurity(securedEnabled = true)
//@Configuration
//@EnableGlobalMethodSecurity(
//        prePostEnabled = true,
//        securedEnabled = true,
//        jsr250Enabled = true)
public class MethodSecurityConfig {//extends GlobalMethodSecurityConfiguration {
}
