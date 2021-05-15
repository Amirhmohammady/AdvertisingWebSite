package com.mycompany.advertising.model.dao;

import com.mycompany.advertising.model.to.UserTo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Created by Amir on 6/6/2020.
 */
@Repository
public interface UserRepository extends JpaRepository<UserTo, Long> {

    Optional<UserTo> findByUsername(String username);
    Optional<UserTo> findByEmail(String email);
    boolean existsByEmail(String email);
}
