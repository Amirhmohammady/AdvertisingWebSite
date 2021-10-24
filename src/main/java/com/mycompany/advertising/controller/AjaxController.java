package com.mycompany.advertising.controller;

import com.mycompany.advertising.components.utils.AViewableException;
import com.mycompany.advertising.controller.validator.api.PhoneNoVadidator;
import com.mycompany.advertising.model.to.UserTo;
import com.mycompany.advertising.service.api.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Amir on 5/8/2021.
 */
@RestController
@RequestMapping("/ajax")
public class AjaxController {
    private static final Logger logger = Logger.getLogger(AjaxController.class);
    @Autowired
    PhoneNoVadidator phonenovadidator;
    @Autowired
    private UserService userService;

    //@ResponseBody use it for @controller classes
    @GetMapping("/checkemail/{email}")
    public ResponseEntity<Object> emailStatus(@PathVariable String email) throws JSONException {
        JSONObject entity = new JSONObject();
        entity.put("isEmailExist", userService.isEmailExist(email));
        logger.trace("request for " + email + " existance and returned " + entity.toString());
        return new ResponseEntity<Object>(entity.toString(), HttpStatus.OK);
    }

    @GetMapping("/checkphonenumber/{phonenumber}")
    public ResponseEntity<Object> phoneNoStatus(@PathVariable String phonenumber) throws JSONException {
        if (phonenumber != null && phonenumber.charAt(0) != '0') phonenumber = '0' + phonenumber;
        JSONObject entity = new JSONObject();
        Matcher matcher = Pattern.compile("^09[\\d]{9}$").matcher(phonenumber);
        if (!matcher.matches()) {
            entity.put("phoneNoStatus", "phoneFormatNotCorrect");
        } else {
            UserTo user = userService.getUserByPhoneNo(phonenumber);
            if (user == null) entity.put("phoneNoStatus", "ready");
            else {
                if (user.getEnabled()) entity.put("phoneNoStatus", "exist");
                else entity.put("phoneNoStatus", "existButNotConfirmed");
            }
        }
        logger.trace("request for " + phonenumber + " status and returned " + entity.toString());
        return new ResponseEntity<Object>(entity.toString(), HttpStatus.OK);
    }
}
