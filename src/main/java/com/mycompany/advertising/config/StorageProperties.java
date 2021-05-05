package com.mycompany.advertising.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;

@ConfigurationProperties("storage")
public class StorageProperties implements ServletContextAware{

    /**
     * Folder location for storing files
     */
    private String webinflocation;

    @Value("${amir.resource.folder}")
    private String fixlocation;

    public String getFixlocation() {
        return fixlocation;
    }

    public String getWebinflocation() {
        return webinflocation;
    }

    public void setWebinflocation(String location) {
        this.webinflocation = location;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        webinflocation = servletContext.getRealPath("/WEB-INF/upload-dir/");
    }
}
