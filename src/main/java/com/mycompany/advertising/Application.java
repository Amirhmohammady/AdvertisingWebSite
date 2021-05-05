package com.mycompany.advertising;

import com.mycompany.advertising.api.StorageService;
import com.mycompany.advertising.config.StorageProperties;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

//import com.mycompany.advertising.api.UserService;
//import com.mycompany.advertising.entity.Role;
//import com.mycompany.advertising.model.to.UserTo;
//import org.springframework.beans.factory.annotation.Autowired;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
/*@PropertySources({
        @PropertySource(value = "file:/application.properties", ignoreResourceNotFound = true)
})*/
public class Application extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner init(StorageService storageService) {
        return (args) -> {
            //storageService.deleteAll();
            storageService.init();
        };
    }
}
/*@Component
class DemoCommandLineRunner implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Override
    public void run(String... args) throws Exception {

        UserTo user = new UserTo();
        user.setUsername("amir");
        user.setPassword("password1234");
        user.grantAuthority(Role.ROLE_ADMIN);
        userService.svaeUser(user);
    }
}*/