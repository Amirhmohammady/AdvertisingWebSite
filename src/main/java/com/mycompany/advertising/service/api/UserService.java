package com.mycompany.advertising.service.api;

import com.mycompany.advertising.entity.UserAlreadyExistException;
import com.mycompany.advertising.model.to.UserTo;
import com.mycompany.advertising.model.to.VerificationTokenTo;

import java.util.Optional;

/**
 * Created by Amir on 6/7/2020.
 */
public interface UserService {
    //void svaeUser(UserTo userTo) throws UserAlreadyExistException;

    public boolean isEmailExist(String email);
    public boolean isPhoneNoExist(String phoneno);
    public UserTo getUserByToken(String verificationToken);
    public UserTo getUserByPhoneNo(String phoneno);
    //public UserTo getCurrentUser();
    public void registerNewUserAccount(UserTo userto) throws UserAlreadyExistException;
    public void saveRegisteredUser(UserTo user);
    public void saveVerificationToken(UserTo user, String token);
    public VerificationTokenTo getVerificationToken(String VerificationToken);
}
