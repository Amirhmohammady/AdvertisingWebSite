package com.mycompany.advertising.service.api;

import com.mycompany.advertising.components.utils.CreateTokenException;
import com.mycompany.advertising.components.utils.PhoneNumberFormatException;
import com.mycompany.advertising.components.utils.SendSmsException;
import com.mycompany.advertising.model.to.UserTo;
import com.mycompany.advertising.model.to.VerificationTokenTo;

import java.util.Date;
import java.util.List;

/**
 * Created by Amir on 12/29/2021.
 */
public interface VerificationTokenService {
    VerificationTokenTo findByToken(String token);

    VerificationTokenTo findByUser(UserTo user);

    void deleteAllExpiredTokenSince(Date now);

    VerificationTokenTo findByUser_Username(String phonenumber);

    long deleteByUser(UserTo user);

    List<VerificationTokenTo> findByExpiryDateLessThan(Date date);

    public void saveVerificationToken(UserTo user) throws CreateTokenException, PhoneNumberFormatException, SendSmsException;

    public void saveVerificationToken(String phonenumber) throws CreateTokenException, PhoneNumberFormatException, SendSmsException;
}
