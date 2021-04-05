package com.mycompany.advertising.service;

import com.mycompany.advertising.model.dao.UserRepository;
import com.mycompany.advertising.model.to.UserTo;
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
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserTo> user = userRepository.findByUsername(username);

        if (user.isPresent()){
            return user.get();
        }else{
            throw new UsernameNotFoundException("Username" + username + "not found");//String.format("Username[%s] not found"));
        }
    }
}
