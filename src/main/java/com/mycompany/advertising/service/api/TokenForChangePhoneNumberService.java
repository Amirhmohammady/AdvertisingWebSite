package com.mycompany.advertising.service.api;

import com.mycompany.advertising.components.utils.CreateTokenException;
import com.mycompany.advertising.components.utils.PhoneNumberFormatException;
import com.mycompany.advertising.components.utils.SendSmsException;
import com.mycompany.advertising.model.to.TokenForChangePhoneNumberTo;
import com.mycompany.advertising.model.to.UserTo;

/**
 * Created by Amir on 12/28/2021.
 */
public interface TokenForChangePhoneNumberService {
    TokenForChangePhoneNumberTo findByUser_Username(String username);

    TokenForChangePhoneNumberTo findByNewPhoneNumber(String newPhoneNumber);

    TokenForChangePhoneNumberTo findByUser(UserTo userTo);

    boolean existsByUser_Username(String username);

    boolean existsByNewPhoneNumber(String newPhoneNumber);

    boolean existsByUser(UserTo userTo);

    void saveVerificationToken(UserTo userTo, String newphonenumber) throws CreateTokenException, PhoneNumberFormatException, SendSmsException;
}
