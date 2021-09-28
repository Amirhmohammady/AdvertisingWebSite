package com.mycompany.advertising.service.api;

import com.mycompany.advertising.entity.UserAlreadyExistException;
import com.mycompany.advertising.model.to.UserTo;
import com.mycompany.advertising.model.to.VerificationTokenTo;

/**
 * Created by Amir on 6/7/2020.
 */
public interface UserService {
    //void svaeUser(UserTo userTo) throws UserAlreadyExistException;

    boolean isEmailExist(String email);
    boolean isPhoneNoExist(String phoneno);
    UserTo getUserByToken(String verificationToken);
    //public UserTo getCurrentUser();
    void registerNewUserAccount(UserTo userto) throws UserAlreadyExistException;
    void saveRegisteredUser(UserTo user);
    void saveVerificationToken(UserTo user, String token);
    VerificationTokenTo getVerificationToken(String VerificationToken);
}
