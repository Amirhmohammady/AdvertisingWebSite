package com.mycompany.advertising.service.util;

import com.mycompany.advertising.model.to.enums.Role;
import com.mycompany.advertising.model.to.UserTo;
import com.mycompany.advertising.service.api.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;


/**
 * Created by Amir on 10/30/2021.
 */

@Configuration
@EnableScheduling
public class ScheduledTasks {
    private final static Logger logger = Logger.getLogger(ScheduledTasks.class);
    @Autowired
    private UserService userservice;
    /*    @Scheduled(cron = "0 * * * * ?")
        public void doEveryMinute() {
            //deleteUserTocken
            userservice.deleteAllExiredToken(new Date(System.currentTimeMillis()));
            logger.info("all expired token deleted");
        }*/
    @Value("${admin.password}")
    private String adminPassword;
    @Value("${admin.phone.number}")
    private String adminPhoneNumber;
    @Value("${admin.username}")
    private String adminUserNmae;

    //@Scheduled(fixedDelay = 60*60*1000)
    @Scheduled(cron = "0 0 * * * ?")
    public void doEveryHoure() {
        //deleteUserTocken
        userservice.deleteAllExiredToken(LocalDateTime.now());
        logger.info("all expired token deleted");
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void doEveryDay() {
        System.out.println("doEveryDay");
    }

    @EventListener
    public void addAdminUser(ApplicationReadyEvent event) {
        createAdminAtStartup();
    }

    private void createAdminAtStartup(){
        UserTo user;
        user = new UserTo();
        user.setPassword(adminPassword);
        user.setUsername(adminPhoneNumber);
        user.setProfilename(adminUserNmae);
        user.grantAuthority(Role.ROLE_ADMIN);
        user.grantAuthority(Role.ROLE_USER);
        try {
            userservice.createUser(user);
            userservice.activateUser(adminPhoneNumber);
            logger.info("Admin User created at server start up");
        } catch (Exception e) {
            logger.info("Error! can not creating admin user at server startup." + e.getMessage());
        }
    }
}
