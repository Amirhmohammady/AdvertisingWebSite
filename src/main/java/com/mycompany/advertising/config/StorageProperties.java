package com.mycompany.advertising.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;

@Configuration
@ConfigurationProperties("storage")
public class StorageProperties implements ServletContextAware {

    /**
     * Folder location for storing files
     */
    private String WEB_INF_location;

    @Value("${amir.resource.folder}")
    private String recourcefolder;

    public String getRecourcefolder() {
        return recourcefolder;
    }

    public String getWebinflocation() {
        return WEB_INF_location;
    }

    public void setWebinflocation(String location) {
        this.WEB_INF_location = location;
    }

    public String getCertfolder() {
        return recourcefolder + "certification_folder/";
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        WEB_INF_location = servletContext.getRealPath("/WEB-INF/upload-dir/");
    }
}
