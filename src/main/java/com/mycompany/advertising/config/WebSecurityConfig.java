package com.mycompany.advertising.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

/**
 * Created by Amir on 5/31/2020.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    AuthenticationFailureHandler authenticationFailureHandler;
    @Value("${max.inactive.interval.seconds.remember.me}")
    private int rememberMeSessionTimeout;
    //@Autowired
    //@Qualifier("persistentTokenRepository")
    //private PersistentTokenRepository persistentTokenRepository;
    //@Autowired
    //AuthenticationSuccessHandler authenticationSuccessHandler;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private DataSource dataSource;

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
        /*http
                .headers()
                .xssProtection()
                .and()
                .contentSecurityPolicy("script-src 'self'");*/
        http
                .authorizeRequests()
                .anyRequest().permitAll().and()
                .logout().logoutUrl("/logout").permitAll().logoutSuccessUrl("/").and()
                .formLogin()//.successHandler(authenticationSuccessHandler)
                //maybe for controlling error exception Search Login Failure Handler https://www.codejava.net/frameworks/spring-boot/spring-boot-security-customize-login-and-logout
                .failureUrl("/login_error").failureHandler(authenticationFailureHandler).loginPage("/login")
                .permitAll()//.usernameParameter("phonenumber")
                .and().rememberMe().userDetailsService(userDetailsService)//.key("uniqueAndSecret")
                .tokenValiditySeconds(rememberMeSessionTimeout).tokenRepository(persistentTokenRepository())
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
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

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepo = new JdbcTokenRepositoryImpl();
        tokenRepo.setDataSource(dataSource);
        return tokenRepo;
    }
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
