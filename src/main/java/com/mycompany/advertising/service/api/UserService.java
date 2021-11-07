package com.mycompany.advertising.service.api;

import com.mycompany.advertising.entity.UserAlreadyExistException;
import com.mycompany.advertising.model.to.UserTo;

import java.util.Date;

/**
 * Created by Amir on 6/7/2020.
 */
public interface UserService {
    //void svaeUser(UserTo userTo) throws UserAlreadyExistException;

    boolean isEmailExist(String email);

    boolean isPhoneNoExist(String phoneno);

    UserTo getUserByToken(String verificationToken);

    UserTo getUserByPhoneNo(String phoneno);

    //public UserTo getCurrentUser();
    void registerNewUserAccount(UserTo userto) throws UserAlreadyExistException;

    void saveRegisteredUser(UserTo user);

    void saveVerificationToken(UserTo user, String token);

    //public VerificationTokenTo getVerificationToken(String VerificationToken);
    //@Transactional put in implementation
    void deleteAllExiredToken(Date date);

    //@Transactional put in implementation
    String getVerficationTokenByPhoneNumber(String phonenumber);
}
