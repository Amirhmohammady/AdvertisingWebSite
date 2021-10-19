package com.mycompany.advertising.service.api;

import com.mycompany.advertising.service.util.FarazSmsResponse;

/**
 * Created by Amir on 9/16/2021.
 */
public interface SmsService {
    public FarazSmsResponse sendSms(String message, String phonenumber);
}
