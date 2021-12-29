package com.mycompany.advertising.service.api;

import com.mycompany.advertising.components.utils.SendSmsException;

/**
 * Created by Amir on 9/16/2021.
 */
public interface SmsService {
    void sendSms(String message, String phonenumber) throws SendSmsException;
    /*FarazSmsResponse sendTocken(String phonenumber) throws CreateTokenException, PhoneNumberFormatException;
    FarazSmsResponse sendTokenForEditNumber(UserTo userTo, String newphonenumber) throws PhoneNumberFormatException, CreateTokenException;*/
}
