package com.mycompany.advertising.model.dao;

import com.mycompany.advertising.model.to.UserTo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by Amir on 6/6/2020.
 */
public interface UserRepository extends JpaRepository<UserTo, Long> {

    Optional<UserTo> findByUsername(String username);
}
