package com.mycompany.advertising.service.api;

import com.mycompany.advertising.components.utils.CreateTokenException;
import com.mycompany.advertising.components.utils.PhoneNumberFormatException;
import com.mycompany.advertising.components.utils.SendSmsException;
import com.mycompany.advertising.entity.UserAlreadyExistException;
import com.mycompany.advertising.model.to.UserTo;
import com.mycompany.advertising.service.util.UserStatuseByPhoneNumber;

import java.time.LocalDateTime;

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
    void createUser(UserTo userto) throws UserAlreadyExistException;

    //@Transactional put in implementation
    void activateUser(UserTo user);

    //@Transactional put in implementation
    void activateUser(String phonenumber);

    //public VerificationTokenTo getVerificationToken(String VerificationToken);
    //@Transactional put in implementation
    void deleteAllExiredToken(LocalDateTime date);

    String getCorrectFormatPhoneNo(String phonenumber) throws PhoneNumberFormatException;

    UserStatuseByPhoneNumber getUserStatuseByPhoneNumber(String phonenumber);

    void editUser(UserTo olddata, UserTo newdata) throws CreateTokenException, PhoneNumberFormatException, SendSmsException;
}
