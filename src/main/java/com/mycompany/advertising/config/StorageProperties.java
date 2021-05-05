package com.mycompany.advertising.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;

@ConfigurationProperties("storage")
public class StorageProperties implements ServletContextAware{

    /**
     * Folder location for storing files
     */
    private String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        location = servletContext.getRealPath("/WEB-INF/upload-dir");
    }
}
