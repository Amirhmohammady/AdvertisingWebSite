package com.mycompany.advertising.components;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by Amir on 8/19/2020.
 */
@Component
public class ExpireMessageScheduler {
    @Scheduled(cron = "0 0 0 * * *", zone = "Indian/Maldives")
    public void trackOverduePayments() {
        System.out.println("Scheduled task running");
    }
}
