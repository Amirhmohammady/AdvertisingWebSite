package com.mycompany.advertising.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

/**
 * Created by Amir on 5/31/2020.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    AuthenticationFailureHandler authenticationFailureHandler;
    @Autowired
    //@Qualifier("persistentTokenRepository")
    private PersistentTokenRepository persistentTokenRepository;
    //    @Autowired
//    AuthenticationSuccessHandler authenticationSuccessHandler;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }
   /* @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user1").password(passwordEncoder.encode("user1Pass")).roles("USER")
                .and()
                .withUser("user2").password(passwordEncoder.encode("user2Pass")).roles("USER")
                .and()
                .withUser("admin").password(passwordEncoder.encode("adminPass")).roles("ADMIN");
    }*/

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .authorizeRequests()
//                .antMatchers("/", "/ajax/**", "/restapi/**", "/index/**", "/share/**", "/t/**", "/test**/**", "/signup/**", "/.well-known/pki-validation/**")//, "/test/**"
//                .permitAll()
//                .anyRequest().authenticated()
//                .antMatchers("/addMessage/**").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
//                .and().logout().logoutUrl("/logout").logoutSuccessUrl("/").and()
//                .formLogin()
//                .loginPage("/login")
//                .permitAll()
//                .and()
//                .logout()
//                .permitAll().and()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
        http
                .authorizeRequests()
                .anyRequest().permitAll().and()
                .logout().logoutUrl("/logout").permitAll().logoutSuccessUrl("/").and()
                .formLogin()//.successHandler(authenticationSuccessHandler)
//maybe fo controlling error exception Search Login Failure Handler https://www.codejava.net/frameworks/spring-boot/spring-boot-security-customize-login-and-logout
                .failureUrl("/login_error").failureHandler(authenticationFailureHandler).loginPage("/login")
                .permitAll().usernameParameter("phonenumber")
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                .and().rememberMe().userDetailsService(userDetailsService)//.key("uniqueAndSecret")
                .tokenValiditySeconds(60 * 60 * 12).tokenRepository(persistentTokenRepository);
        //for enabling multipart sending and handling logout
        http.csrf().disable();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    /*@Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl db = new JdbcTokenRepositoryImpl();
        db.setDataSource(dataSource);
        return db;
    }*/
    /*@Bean
    @Override
    public UserDetailsService userDetailsService() {
        UserDetails user =
                User.withDefaultPasswordEncoder()
                        .username("user")
                        .password("password")
                        .roles("USER")
                        .build();

        return new InMemoryUserDetailsManager(user);
    }*/
}
