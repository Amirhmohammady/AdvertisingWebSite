package com.mycompany.advertising.api;

import com.mycompany.advertising.entity.TokenNotFoundtException;
import com.mycompany.advertising.entity.UserAlreadyExistException;
import com.mycompany.advertising.model.to.UserTo;
import com.mycompany.advertising.model.to.VerificationToken;

/**
 * Created by Amir on 6/7/2020.
 */
public interface UserService {
    //void svaeUser(UserTo userTo) throws UserAlreadyExistException;

    boolean isEmailExist(String email);
    UserTo getUserByToken(String verificationToken);
    //public UserTo getCurrentUser();
    void registerNewUserAccount(UserTo userto) throws UserAlreadyExistException;
    void saveRegisteredUser(UserTo user);
    void createVerificationToken(UserTo user, String token);
    VerificationToken getVerificationToken(String VerificationToken);
}
