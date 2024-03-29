package com.mycompany.advertising.service;

import com.mycompany.advertising.components.utils.PhoneNumberFormatException;
import com.mycompany.advertising.controller.utils.annotations.LockApiByVariable;
import com.mycompany.advertising.controller.utils.annotations.TimeLimiter;
import com.mycompany.advertising.model.dao.UserRepository;
import com.mycompany.advertising.model.to.UserTo;
import com.mycompany.advertising.service.api.UserService;
import com.mycompany.advertising.service.util.exceptions.TooManyLoginAttempt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by Amir on 6/6/2020.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Override
    @LockApiByVariable(timeLimiter = @TimeLimiter(maxRequest = 2, inSeconds = 16), variableName = "phonenumber", excptionType = TooManyLoginAttempt.class, exceptionMsg = "too many attempt to login")
    public UserDetails loadUserByUsername(String phonenumber) throws UsernameNotFoundException {
        try {
            phonenumber = userService.getCorrectFormatPhoneNo(phonenumber);
        } catch (PhoneNumberFormatException e) {
            throw new UsernameNotFoundException("Phone Number " + phonenumber + " not found");//String.format("Username[%s] not found"));
        }
        Optional<UserTo> user = userRepository.findByUsername(phonenumber);

        if (user.isPresent()) {
            return user.get();
        } else {
            throw new UsernameNotFoundException("Phone Number " + phonenumber + " not found");//String.format("Username[%s] not found"));
        }
    }
}
