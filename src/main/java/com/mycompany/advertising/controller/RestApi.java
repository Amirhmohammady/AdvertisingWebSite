package com.mycompany.advertising.controller;

import com.mycompany.advertising.components.api.AuthenticationFacade;
import com.mycompany.advertising.components.utils.PhoneNumberFormatException;
import com.mycompany.advertising.model.to.enums.Role;
import com.mycompany.advertising.model.to.AdvertiseTo;
import com.mycompany.advertising.model.to.UserTo;
import com.mycompany.advertising.model.to.VerificationTokenTo;
import com.mycompany.advertising.service.api.AdminMessageService;
import com.mycompany.advertising.service.api.AdvertiseService;
import com.mycompany.advertising.service.api.UserService;
import com.mycompany.advertising.service.api.VerificationTokenService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.lang.invoke.MethodHandles;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Amir on 11/4/2021.
 */
@RestController
@RequestMapping("/restapi")
public class RestApi {
    private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());
    @Autowired
    UserService userservice;
    @Autowired
    VerificationTokenService verificationTokenService;
    @Autowired
    AdminMessageService adminMessageService;
    @Autowired
    AuthenticationFacade authenticationFacade;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AdvertiseService advertiseService;

    @GetMapping("/sendsms/{phonenumber}")
    public String sendSMS(@PathVariable String phonenumber) {
        logger.info("request for sending sms to " + phonenumber);
        try {
            verificationTokenService.saveVerificationToken(phonenumber);
            return "sms sent successfully";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @GetMapping("/registerUser/{phonenumber}/{confirmcode}")
    public String registerUser(@PathVariable String phonenumber, @PathVariable String confirmcode) {
        logger.info("request for activating phone number " + phonenumber + " by token " + confirmcode);
        try {
            phonenumber = userservice.getCorrectFormatPhoneNo(phonenumber);
            VerificationTokenTo token = verificationTokenService.findByUser_Username(phonenumber);
            if (token != null && token.getToken().equals(confirmcode)) {
                userservice.activateUser(phonenumber);
                return phonenumber + " activated successfully";
            } else return "can not active user";
        } catch (PhoneNumberFormatException e) {
            return e.getMessage();
        }
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @PostMapping(value = "/editUser")
//, headers = "Accept=application/json", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> editUser(@RequestBody Map<String, Object> body) {
        UserTo userTo = new UserTo();
        String pass = "";
        try {
        /*try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> tmp1 = (Map<String, Object>) body.get("userTo");
            logger.info(tmp1);
            userTo = objectMapper.readValue(tmp1.toString(), UserTo.class);
        } catch (IOException e) {
            return "your json format is not correct";
        }*/
            String newpn = userservice.getCorrectFormatPhoneNo((String) body.get("username"));
            userTo.setUsername(newpn);
            userTo.setProfilename((String) body.get("profilename"));
            userTo.setFullname((String) body.get("fullname"));
            pass = (String) body.get("password");
            String newpass = (String) body.get("newpass");
            if (newpass != null && !newpass.equals("")) userTo.setPassword(newpass);
            userTo.setAboutme((String) body.get("aboutme"));
            userTo.setWebsiteurl((String) body.get("websiteurl"));
            userTo.setEmail((String) body.get("email"));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        logger.info("try to edit user " + userTo);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof UserTo)) return new ResponseEntity<>("Error in editing user! can not find current user!", HttpStatus.NOT_ACCEPTABLE);
        UserTo cuser = (UserTo) principal;
        if (!passwordEncoder.matches(pass, cuser.getPassword())) return new ResponseEntity<>("password is not correct", HttpStatus.NOT_ACCEPTABLE);
        try {
            userservice.editUser(cuser, userTo);
            return new ResponseEntity<>("profile edited successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Secured({"ROLE_ADMIN"})
    @DeleteMapping("/userComment/{id}")
    public ResponseEntity<String> deleteUserComment(@PathVariable long id) {
        int rows = adminMessageService.deleteUserCommentById(id);
        if (rows > 0) return new ResponseEntity<>("deleted successfully", HttpStatus.OK);
        return new ResponseEntity<>("fail to delete", HttpStatus.NOT_ACCEPTABLE);
    }

    @DeleteMapping("/advertise/{id}")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity<String> deleteAdvertise(@PathVariable Long id) {
        Optional<AdvertiseTo> advertiseoptl = advertiseService.getAdvertiseById(id);
        int rows = 0;
        if (advertiseoptl.isPresent()) {
            UserTo userTo = authenticationFacade.getCurrentUser();
            if (userTo != null && (userTo.hasRole(Role.ROLE_ADMIN) || advertiseoptl.get().getUserTo().getId() == userTo.getId()))
                rows = advertiseService.deleteAdvertiseById(id);
        }
        if (rows > 0) return new ResponseEntity<>("deleted successfully", HttpStatus.OK);
        return new ResponseEntity<>("fail to delete", HttpStatus.NOT_ACCEPTABLE);
    }

    @Secured({"ROLE_ADMIN"})
    @DeleteMapping("/adminMessage/{id}")
    public ResponseEntity<String> deleteAdminMessage(@PathVariable long id) {
        int rows = adminMessageService.deleteAdminMessageById(id);
        if (rows > 0) return new ResponseEntity<>("deleted successfully", HttpStatus.OK);
        return new ResponseEntity<>("fail to delete", HttpStatus.NOT_ACCEPTABLE);
    }
}
