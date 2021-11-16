package com.mycompany.advertising.controller;

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

/**
 * Created by Amir on 5/8/2021.
 */
@RestController
@RequestMapping("/ajax")
public class AjaxController {
    private static final Logger logger = Logger.getLogger(AjaxController.class);
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
        JSONObject entity = new JSONObject();
        entity.put("phoneNoStatus", userService.getUserStatuseByPhoneNumber(phonenumber));
        logger.trace("request for " + phonenumber + " status and returned " + entity.toString());
        return new ResponseEntity<Object>(entity.toString(), HttpStatus.OK);
    }
}
