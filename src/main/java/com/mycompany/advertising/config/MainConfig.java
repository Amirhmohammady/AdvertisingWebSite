package com.mycompany.advertising.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created by Amir on 11/11/2019.
 */

@Configuration
public class MainConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/t/**")
                .addResourceLocations("file:/D:/nnnn/","file:upload-dir/")
                .setCachePeriod(31556926);
    }
}