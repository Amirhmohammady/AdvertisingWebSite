package com.mycompany.advertising.controller.events;

import com.mycompany.advertising.model.to.UserTo;
import com.mycompany.advertising.service.api.SmsService;
import com.mycompany.advertising.service.api.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.Random;

/**
 * Created by Amir on 9/14/2021.
 */
@Component
public class RegistrationListener implements ApplicationListener<OnSigningUpCompleteEvent> {
    final static Logger logger = Logger.getLogger(RegistrationListener.class);

    @Autowired
    SmsService smsService;
    @Autowired
    private UserService userservice;
    @Autowired
    private MessageSource messages;

    @Override
    public void onApplicationEvent(OnSigningUpCompleteEvent event) {
        logger.trace("SigningUpCompleteEvent happened");
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnSigningUpCompleteEvent event) {
        UserTo user = event.getUser();
        String token = new DecimalFormat("000000").format(new Random().nextInt(999999));
        userservice.saveVerificationToken(user, token);
        int smsstatus = smsService.sendSms("your vrification code is: " + token, user.getPhonenumber());
        /*String recipientAddress = user.getEmail();
        String subject = "Registration Confirmation";
        String confirmationUrl
                = event.getAppUrl() + "/regitrationConfirm.html?token=" + token;
        String message = messages.getMessage("message.regSucc", null, event.getLocale());

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + "\r\n" + "http://localhost:8080" + confirmationUrl);
        mailSender.send(email);*/
    }
}