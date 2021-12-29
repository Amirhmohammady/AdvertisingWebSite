package com.mycompany.advertising.service;

import com.mycompany.advertising.components.utils.CreateTokenException;
import com.mycompany.advertising.components.utils.PhoneNumberFormatException;
import com.mycompany.advertising.components.utils.SendSmsException;
import com.mycompany.advertising.entity.UserAlreadyExistException;
import com.mycompany.advertising.model.dao.UserRepository;
import com.mycompany.advertising.model.dao.VerificationTokenRepository;
import com.mycompany.advertising.model.to.UserTo;
import com.mycompany.advertising.model.to.VerificationTokenTo;
import com.mycompany.advertising.service.api.SmsService;
import com.mycompany.advertising.service.api.TokenForChangePhoneNumberService;
import com.mycompany.advertising.service.api.UserService;
import com.mycompany.advertising.service.util.UserStatuseByPhoneNumber;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/**
 * Created by Amir on 6/7/2020.
 */
@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = Logger.getLogger(UserServiceImpl.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;
    @Autowired
    private TokenForChangePhoneNumberService tokenForChangePhoneNumberService;
    @Value("${token.expire.minutes}")
    private int expiretockentime;
    @Autowired
    private SmsService smsService;

    //todoAmir
    @Override
    public void createUser(UserTo userTo) throws UserAlreadyExistException {
        if (userRepository.existsByUsername(userTo.getUsername())) {
            logger.debug(userTo.toString() + "Phone Number is exist");
            throw new UserAlreadyExistException(userTo.getUsername());
        } else {
            userTo.setPassword(passwordEncoder.encode(userTo.getPassword()));
            userTo.setEnabled(false);
            userRepository.save(userTo);
            logger.debug(userTo.toString() + "saved successfully");
        }
    }

    @Override
    @Transactional
    public void activateUser(UserTo user) {
        long deletedrows = verificationTokenRepository.deleteByUser(user);
        //amir todo uncomment after adding user update
        //if (deletedrows > 0) {
        user.setEnabled(true);
        userRepository.save(user);
        logger.info("user: " + user.toString() + "activated");
        //} else logger.info("user: " + user.toString() + "coluldn,t activate");
    }

    @Override
    @Transactional
    public void activateUser(String phonenumber) {
        Optional<UserTo> user = userRepository.findByUsername(phonenumber);
        if (user.isPresent()) {
            activateUser(user.get());
        } else logger.info("can not activate user phone number " + phonenumber + "is not exist");
    }

    /*@Override
    public VerificationTokenTo getVerificationToken(String verificationToken) {
        return verificationTokenRepository.findByToken(verificationToken);
    }*/

    @Override
    public boolean isEmailExist(String email) {
        if (userRepository.existsByEmailAndEnabled(email, true)) {
            logger.trace("Email " + email + " is exist");
            return true;
        } else {
            logger.trace("Email " + email + " is not exist");
            return false;
        }
    }

    @Override
    public boolean isPhoneNoExist(String phonenumber) {
        if (phonenumber.charAt(0) != '0') phonenumber = '0' + phonenumber;
        if (userRepository.existsByUsername(phonenumber)) {
            logger.trace("Phone number " + phonenumber + " is exist");
            return true;
        } else {
            logger.trace("Phone number " + phonenumber + " is not exist");
            return false;
        }
    }

    @Override
    public UserTo getUserByToken(String verificationToken) {
        return verificationTokenRepository.findByToken(verificationToken).getUser();
    }

    @Override
    public UserTo getUserByPhoneNo(String phoneno) {
        Optional<UserTo> user = userRepository.findByUsername(phoneno);
        if (user.isPresent()) {
            return user.get();
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public void deleteAllExiredToken(Date date) {
        List<VerificationTokenTo> verificationTokenTos = verificationTokenRepository.findByExpiryDateLessThan(date);
        if (verificationTokenTos.size() > 0) {
            List<UserTo> userTos = verificationTokenTos.stream().map((vto) -> {
                return (vto.getUser().getEnabled()) ? null : vto.getUser();
            }).filter(e -> e != null).collect(Collectors.toList());
            verificationTokenRepository.deleteAll(verificationTokenTos);
            userRepository.deleteAll(userTos);
        }
        //verificationTokenRepository.deleteAllExpiredTokenSince(date);
    }

    @Override
    public String getCorrectFormatPhoneNo(String phonenumber) throws PhoneNumberFormatException {
        if (phonenumber == null) throw new PhoneNumberFormatException("phone number can not be empty");
        if (phonenumber.charAt(0) != '0') phonenumber = '0' + phonenumber;
        Matcher matcher = Pattern.compile("^09[\\d]{9}$").matcher(phonenumber);
        if (!matcher.matches()) throw new PhoneNumberFormatException("phone format not correct");
        return phonenumber;
    }

    @Override
    public UserStatuseByPhoneNumber getUserStatuseByPhoneNumber(String phonenumber) {
        try {
            phonenumber = getCorrectFormatPhoneNo(phonenumber);
            UserTo user = getUserByPhoneNo(phonenumber);
            if (user == null) return UserStatuseByPhoneNumber.NOT_EXIST;
            else {
                if (user.getEnabled()) return UserStatuseByPhoneNumber.EXIST_AND_ACTIVATED;
                else if (verificationTokenRepository.existsByUser_Username(phonenumber)) {
                    return UserStatuseByPhoneNumber.EXIST_BUT_NOT_ACTIVATED;
                } else {
                    return UserStatuseByPhoneNumber.EXIST_BUT_TOKEN_DID_NOT_SEND;
                }
            }
        } catch (PhoneNumberFormatException e) {
            return UserStatuseByPhoneNumber.PHONE_FORMAT_NOT_CORRECT;
        }
    }

    @Override
    public void editUser(UserTo olddata, UserTo newdata) throws CreateTokenException, PhoneNumberFormatException, SendSmsException {
        if (!olddata.getUsername().equals(newdata.getUsername())) {
            tokenForChangePhoneNumberService.saveVerificationToken(olddata, newdata.getUsername());
            // to end a session of a user:
            /*SessionRegistryImpl sessionRegistryImpl = new SessionRegistryImpl();
            List<SessionInformation> sessions = sessionRegistryImpl.getAllSessions(olddata, false);
            for (SessionInformation si : sessions)
                sessionRegistryImpl.getSessionInformation(si.getSessionId()).expireNow();*/
            // note: you can get all users and their corresponding session Ids:
            /*List<Object> users = sessionRegistryImpl.getAllPrincipals();
            List<String> sessionIds = new ArrayList<>(users.size());
            for (Object user : users) {
                List<SessionInformation> sessions = sessionRegistryImpl.getAllSessions(user, false);
                sessionIds.add(sessions.get(0).getSessionId());
            }*/
            //olddata.setEnabled(false);
        }
        olddata.setProfilename(newdata.getProfilename());
        olddata.setAboutme(newdata.getAboutme());
        olddata.setWebsiteurl(newdata.getWebsiteurl());
        olddata.setFullname(newdata.getFullname());
        olddata.setEmail(newdata.getEmail());
        userRepository.save(olddata);
    }
    /*@Override
    public UserTo getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();
        UserTo userTo;
        if (principal instanceof UserTo) {
            userTo = (UserTo)auth.getPrincipal();
        } else {
            userTo = null;
        }
        return userTo;
    }*/
}
