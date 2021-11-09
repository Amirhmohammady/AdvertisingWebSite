package com.mycompany.advertising.controller;

import com.mycompany.advertising.components.utils.PhoneNumberFormatException;
import com.mycompany.advertising.model.to.UserTo;
import com.mycompany.advertising.service.api.SmsService;
import com.mycompany.advertising.service.api.UserService;
import com.mycompany.advertising.service.util.FarazSmsResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.DecimalFormat;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Amir on 11/4/2021.
 */
@RestController
@RequestMapping("/restapi")
public class RestApi {
    private static final Logger logger = Logger.getLogger(RestApi.class);
    @Autowired
    UserService userservice;
    @Autowired
    SmsService smsservice;

    @GetMapping("/sendsms/{phonenumber}")
    public String sendSMS(@PathVariable String phonenumber) {
        if (phonenumber != null && phonenumber.charAt(0) != '0') phonenumber = '0' + phonenumber;
        Matcher matcher = Pattern.compile("^09[\\d]{9}$").matcher(phonenumber);
        if (!matcher.matches())
            return "phoneFormatNotCorrect";
        else if (userservice.getVerficationTokenByPhoneNumber(phonenumber) != null)
            return "token is exist and sms will not send";
        else {
            UserTo user = userservice.getUserByPhoneNo(phonenumber);
            String token = new DecimalFormat("000000").format(new Random().nextInt(999999));
            FarazSmsResponse smsresponse = smsservice.sendSms("your vrification code is: " + token, user.getPhonenumber());
            if (smsresponse.getStatus().equals("0")) {
                logger.info("tocken " + token + " sent to " + user.getPhonenumber());
                userservice.saveVerificationToken(user, token);
                return "sms sent successfully";
            } else {
                //Amir todo
                userservice.saveVerificationToken(user, token);
                logger.debug("tocken " + token + " could not send to " + user.getPhonenumber() + " " + smsresponse.getMessage());
                return "can not send sms call admin";
            }
        }
    }

    @GetMapping("/registerUser/{phonenumber}/{confirmcode}")
    public String registerUser(@PathVariable String phonenumber, @PathVariable String confirmcode) {
        logger.debug("request for activating phone number " + phonenumber);
        try {
            phonenumber = userservice.getCorrectFormatPhoneNo(phonenumber);
            userservice.activateUser(phonenumber);
            return "user by phone number " + phonenumber + " activated successfully";
        } catch (PhoneNumberFormatException e) {
            return e.getMessage();
        }
    }
}
