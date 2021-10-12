package com.mycompany.advertising.controller.validator
        ;

import com.mycompany.advertising.controller.validator.api.PhoneNoVadidator;
import org.springframework.stereotype.Component;

/**
 * Created by Amir on 10/12/2021.
 */
@Component
public class PhoneNoVadidatorImpl implements PhoneNoVadidator {
    @Override
    public boolean isPhoneNoValid(String phoneno) {
        if (phoneno == null) return false;
        if (phoneno.length() != 11) return false;
        if (phoneno.charAt(0) != '0') return false;
        if (phoneno.charAt(1) != '9') return false;
        return true;
    }
}
