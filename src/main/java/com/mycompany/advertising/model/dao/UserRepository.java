package com.mycompany.advertising.model.dao;

import com.mycompany.advertising.model.to.UserTo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Amir on 6/6/2020.
 */
public interface UserRepository extends JpaRepository<UserTo, Long> {

    UserTo findByUsername(String username);
}
