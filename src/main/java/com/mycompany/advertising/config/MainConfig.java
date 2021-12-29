package com.mycompany.advertising.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.Locale;

/**
 * Created by Amir on 11/11/2019.
 */

@Configuration
public class MainConfig implements WebMvcConfigurer {
    @Autowired
    StorageProperties storageproperties;
    @Value("${max.inactive.interval.seconds}")
    private int maxinactiveinterval;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/t/**")
                .addResourceLocations("file:/" + storageproperties.getRecourcefolder(), "file:/" + storageproperties.getWebinflocation())
                .setCachePeriod(31556926);
        /*registry.addResourceHandler("/.well-known/pki-validation*//**")
         .addResourceLocations("file:/" + storageproperties.getCertfolder())
         .setCachePeriod(31556926);*/
    }

    @Bean
    //set language to save in cookie and set cookie name
    public LocaleResolver localeResolver() {
        CookieLocaleResolver r = new CookieLocaleResolver();
        r.setDefaultLocale(new Locale("fa"));//Locale.US);
        r.setCookieName("localeInfo");
        //if set to -1, the cookie is deleted when browser shuts down
        //r.setCookieMaxAge(24*60*60);
        return r;
    }

    /*@Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
        sessionLocaleResolver.setDefaultLocale(Locale.US);
        return sessionLocaleResolver;
    }*/
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("language");
        return localeChangeInterceptor;
    }

    @Override
    //to take effect needs to be added to the application's interceptor registry
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

    @Bean
    //set resource loctions
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:languages/message");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }

    //Async events
    @Bean(name = "applicationEventMulticaster")
    public ApplicationEventMulticaster simpleApplicationEventMulticaster() {
        SimpleApplicationEventMulticaster eventMulticaster =
                new SimpleApplicationEventMulticaster();

        eventMulticaster.setTaskExecutor(new SimpleAsyncTaskExecutor());
        return eventMulticaster;
    }

    @Bean
    public ServletListenerRegistrationBean<MySessionListener> sessionListener() {
        ServletListenerRegistrationBean<MySessionListener> listenerRegBean =
                new ServletListenerRegistrationBean<>();

        listenerRegBean.setListener(new MySessionListener(maxinactiveinterval));
        return listenerRegBean;
    }
    //set CacheMaxSize to 30MB default is 10MB
/*    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> tomcatFactory() {
        WebServerFactoryCustomizer<TomcatServletWebServerFactory> server = new WebServerFactoryCustomizer<TomcatServletWebServerFactory>() {
            @Override
            public void customize(TomcatServletWebServerFactory tomcatServletWebServerFactory) {
                tomcatServletWebServerFactory.addContextCustomizers((context) -> {
                    StandardRoot standardRoot = new StandardRoot(context);
                    standardRoot.setCacheMaxSize(30 * 1024);
                });
            }
        };
//        TomcatServletWebServerFactory tomcatFactory = new TomcatServletWebServerFactory();
//        tomcatFactory.addContextCustomizers((context) -> {
//            StandardRoot standardRoot = new StandardRoot(context);
//            standardRoot.setCacheMaxSize(30 * 1024);
//        });
        return server;
    }*/
}