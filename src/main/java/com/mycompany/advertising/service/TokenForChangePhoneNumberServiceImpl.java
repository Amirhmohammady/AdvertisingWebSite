package com.mycompany.advertising.service;

import com.mycompany.advertising.components.utils.CreateTokenException;
import com.mycompany.advertising.components.utils.PhoneNumberFormatException;
import com.mycompany.advertising.components.utils.SendSmsException;
import com.mycompany.advertising.model.dao.TokenForChangePhoneNumberRepository;
import com.mycompany.advertising.model.to.TokenForChangePhoneNumberTo;
import com.mycompany.advertising.model.to.UserTo;
import com.mycompany.advertising.service.api.SmsService;
import com.mycompany.advertising.service.api.TokenForChangePhoneNumberService;
import com.mycompany.advertising.service.api.UserService;
import com.mycompany.advertising.service.util.UserStatuseByPhoneNumber;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by Amir on 12/28/2021.
 */
@Service
public class TokenForChangePhoneNumberServiceImpl implements TokenForChangePhoneNumberService {
    Logger logger = Logger.getLogger(TokenForChangePhoneNumberServiceImpl.class);

    @Autowired
    SmsService smsService;
    @Autowired
    UserService userService;
    @Autowired
    TokenForChangePhoneNumberRepository tokenForChangePhoneNumberRepository;
    @Value("${token.expire.minutes}")
    private int expiretockentime;

    @Override
    public TokenForChangePhoneNumberTo findByUser_Username(String username) {
        return tokenForChangePhoneNumberRepository.findByUser_Username(username);
    }

    @Override
    public TokenForChangePhoneNumberTo findByNewPhoneNumber(String newPhoneNumber) {
        return tokenForChangePhoneNumberRepository.findByNewPhoneNumber(newPhoneNumber);
    }

    @Override
    public TokenForChangePhoneNumberTo findByUser(UserTo userTo) {
        return tokenForChangePhoneNumberRepository.findByUser(userTo);
    }

    @Override
    public boolean existsByUser_Username(String username) {
        return tokenForChangePhoneNumberRepository.existsByUser_Username(username);
    }

    @Override
    public boolean existsByNewPhoneNumber(String newPhoneNumber) {
        return tokenForChangePhoneNumberRepository.existsByNewPhoneNumber(newPhoneNumber);
    }

    @Override
    public boolean existsByUser(UserTo userTo) {
        return tokenForChangePhoneNumberRepository.existsByUser(userTo);
    }

    @Override
    public void saveVerificationToken(UserTo userTo, String newphonenumber) throws CreateTokenException, PhoneNumberFormatException, SendSmsException {
        newphonenumber = userService.getCorrectFormatPhoneNo(newphonenumber);
        if (userService.getUserStatuseByPhoneNumber(newphonenumber) != UserStatuseByPhoneNumber.NOT_EXIST)
            throw new CreateTokenException("new phone number is exist");
        if (tokenForChangePhoneNumberRepository.existsByNewPhoneNumber(newphonenumber))
            throw new CreateTokenException("new phone number is exist");
        if (tokenForChangePhoneNumberRepository.existsByUser(userTo))
            throw new CreateTokenException("you already have a change phone number task");
        TokenForChangePhoneNumberTo tokenForChangePhoneNumber = new TokenForChangePhoneNumberTo();
        tokenForChangePhoneNumber.setToken(new DecimalFormat("000000").format(new Random().nextInt(999999)));
        smsService.sendSms("your vrification code is: " + tokenForChangePhoneNumber.getToken(), userTo.getUsername());
        tokenForChangePhoneNumber.setExpiryDate(new Date(System.currentTimeMillis() + (1000 * 60 * expiretockentime)));
        tokenForChangePhoneNumber.setUser(userTo);
        tokenForChangePhoneNumber.setNewPhoneNumber(newphonenumber);
        tokenForChangePhoneNumberRepository.save(tokenForChangePhoneNumber);
        logger.info("tocken " + tokenForChangePhoneNumber.getToken() + " sent to " + userTo.getUsername());
    }
}
