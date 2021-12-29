package com.mycompany.advertising.controller.events;

import com.mycompany.advertising.model.to.UserTo;
import com.mycompany.advertising.service.api.SmsService;
import com.mycompany.advertising.service.api.VerificationTokenService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Created by Amir on 9/14/2021.
 */
@Component
public class RegistrationListener implements ApplicationListener<OnSigningUpCompleteEvent> {
    final static Logger logger = Logger.getLogger(RegistrationListener.class);

    @Autowired
    private SmsService smsService;
    @Autowired
    private VerificationTokenService verificationTokenService;
    /*@Autowired
    private MessageSource messages;*/

    @Override
    public void onApplicationEvent(OnSigningUpCompleteEvent event) {
        logger.trace("SigningUpCompleteEvent happened");
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnSigningUpCompleteEvent event) {
        UserTo user = event.getUser();
        try {
            verificationTokenService.saveVerificationToken(user);
        } catch (Exception e) {
            logger.warn("tocken could not send to " + user.getUsername() + " " + e.getMessage());
        }
    }
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
