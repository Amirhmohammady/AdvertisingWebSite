package com.mycompany.advertising.service;

import com.mycompany.advertising.components.utils.CreateTokenException;
import com.mycompany.advertising.components.utils.PhoneNumberFormatException;
import com.mycompany.advertising.components.utils.SendSmsException;
import com.mycompany.advertising.model.dao.UserRepository;
import com.mycompany.advertising.model.dao.VerificationTokenRepository;
import com.mycompany.advertising.model.to.UserTo;
import com.mycompany.advertising.model.to.VerificationTokenTo;
import com.mycompany.advertising.service.api.SmsService;
import com.mycompany.advertising.service.api.UserService;
import com.mycompany.advertising.service.api.VerificationTokenService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.invoke.MethodHandles;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * Created by Amir on 12/29/2021.
 */
@Service
public class VerificationTokenServiceImpl implements VerificationTokenService {
    private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    SmsService smsService;
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    VerificationTokenRepository verificationTokenRepository;
    @Value("${token.expire.minutes}")
    private int expiretockentime;

    @Override
    public VerificationTokenTo findByToken(String token) {
        return verificationTokenRepository.findByToken(token);
    }

    @Override
    public VerificationTokenTo findByUser(UserTo user) {
        return verificationTokenRepository.findByUser(user);
    }

    @Override
    public void deleteAllExpiredTokenSince(LocalDateTime now) {
        verificationTokenRepository.deleteAllExpiredTokenSince(now);
    }

    @Override
    public VerificationTokenTo findByUser_Username(String phonenumber) {
        return verificationTokenRepository.findByUser_Username(phonenumber);
    }

    @Override
    @Transactional
    public long deleteByUser(UserTo user) {
        return verificationTokenRepository.deleteByUser(user);
    }

    @Override
    public List<VerificationTokenTo> findByExpiryDateLessThan(LocalDateTime date) {
        return verificationTokenRepository.findByExpiryDateLessThan(date);
    }

    @Override
    public void saveVerificationToken(UserTo user) throws CreateTokenException, PhoneNumberFormatException, SendSmsException {
        if (user.getEnabled()) throw new CreateTokenException("user is activated");
        if (verificationTokenRepository.existsByUser(user))
            throw new CreateTokenException("token is exist and sms will not send");
        VerificationTokenTo token = new VerificationTokenTo(new DecimalFormat(
                "000000").format(new Random().nextInt(999999)),
                user,
                LocalDateTime.now().plusMinutes(expiretockentime));//(System.currentTimeMillis() + (1000 * 60 * expiretockentime)));
        //Amir todo remove try/catch
        try {
            smsService.sendSms("your vrification code is: " + token.getToken(), user.getUsername());
        } catch (SendSmsException se) {

        }
        verificationTokenRepository.save(token);
        logger.info("token " + token.getToken() + " successfully send to " + user.getUsername());
    }

    @Override
    public void saveVerificationToken(String phonenumber) throws CreateTokenException, PhoneNumberFormatException, SendSmsException {
        phonenumber = userService.getCorrectFormatPhoneNo(phonenumber);
        Optional<UserTo> user = userRepository.findByUsername(phonenumber);
        //UserTo user = userService.getUserByPhoneNo(pn);
        if (!user.isPresent()) throw new CreateTokenException("user is not exist");
        saveVerificationToken(user.get());
        /*if (user.get().getEnabled()) throw new CreateTokenException("user is activated");
        if (verificationTokenRepository.existsByUser_Username(phonenumber))
            throw new CreateTokenException("token is exist and sms will not send");
        VerificationTokenTo token = new VerificationTokenTo(new DecimalFormat(
                "000000").format(new Random().nextInt(999999)),
                user.get(),
                new Date(System.currentTimeMillis() + (1000 * 60 * expiretockentime)));
        smsService.sendSms("your vrification code is: " + token.getToken(), user.get().getUsername());
        verificationTokenRepository.save(token);*/
    }
}
