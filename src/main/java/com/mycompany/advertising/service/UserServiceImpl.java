package com.mycompany.advertising.service;

import com.mycompany.advertising.components.utils.PhoneNumberFormatException;
import com.mycompany.advertising.entity.UserAlreadyExistException;
import com.mycompany.advertising.model.dao.UserRepository;
import com.mycompany.advertising.model.dao.VerificationTokenRepository;
import com.mycompany.advertising.model.to.UserTo;
import com.mycompany.advertising.model.to.VerificationTokenTo;
import com.mycompany.advertising.service.api.UserService;
import com.mycompany.advertising.service.util.UserStatuseByPhoneNumber;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
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
    UserDetailsService userDetailsService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;
    @Value("${token.expire.minutes}")
    private int expiretockentime;

    //todoAmir
    @Override
    public void createUser(UserTo userTo) throws UserAlreadyExistException {
        if (userRepository.existsByPhonenumber(userTo.getPhonenumber())) {
            logger.debug(userTo.toString() + "Phone Number is exist");
            throw new UserAlreadyExistException(userTo.getPhonenumber());
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
        if (deletedrows > 0) {
            user.setEnabled(true);
            userRepository.save(user);
            logger.info("user: " + user.toString() + "activated");
        } else logger.info("user: " + user.toString() + "coluldn,t activate");
    }

    @Override
    @Transactional
    public void activateUser(String phonenumber) {
        Optional<UserTo> user = userRepository.findByPhonenumber(phonenumber);
        if (user.isPresent()) {
            activateUser(user.get());
        } else logger.info("can not activate user phone number " + phonenumber + "is not exist");
    }

    @Override
    public void saveVerificationToken(UserTo user, String token) {
        VerificationTokenTo mytoken = new VerificationTokenTo(token, user, new Date(System.currentTimeMillis() + (1000 * 60 * expiretockentime)));
        verificationTokenRepository.save(mytoken);
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
        if (userRepository.existsByPhonenumber(phonenumber)) {
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
        Optional<UserTo> user = userRepository.findByPhonenumber(phoneno);
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
    @Transactional
    public String getVerficationTokenByPhoneNumber(String phonenumber) {
        return verificationTokenRepository.findTokenByNPhoneNumber(phonenumber);
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
                else if (getVerficationTokenByPhoneNumber(phonenumber) != null) {
                    return UserStatuseByPhoneNumber.EXIST_BUT_NOT_ACTIVATED;
                } else {
                    return UserStatuseByPhoneNumber.EXIST_BUT_TOKEN_DID_NOT_SEND;
                }
            }
        } catch (PhoneNumberFormatException e) {
            return UserStatuseByPhoneNumber.PHONE_FORMAT_NOT_CORRECT;
        }
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
