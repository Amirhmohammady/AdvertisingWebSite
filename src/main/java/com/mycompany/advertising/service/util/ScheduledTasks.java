package com.mycompany.advertising.service.util;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Created by Amir on 10/30/2021.
 */

@Configuration
@EnableScheduling
public class ScheduledTasks {
    private final static Logger logger = Logger.getLogger(ScheduledTasks.class);

    //@Scheduled(fixedDelay = 60*60*1000)
    @Scheduled(cron = "0 * * * * ?")
    public void doEveryMinute() {
        //deleteUserTocken
        System.out.println("doEveryMinute");
    }

    @Scheduled(cron = "0 0 * * * ?")
    public void doEveryHoure() {
        //deleteUserTocken
        System.out.println("doEveryHoure");
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void doEveryDay() {
        System.out.println("doEveryDay");
    }
}
