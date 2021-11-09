package com.mycompany.advertising.model.dao;

import com.mycompany.advertising.model.to.UserTo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Created by Amir on 6/6/2020.
 */
@Repository
public interface UserRepository extends JpaRepository<UserTo, Long> {

    Optional<UserTo> findByUsername(String username);

    Optional<UserTo> findByEmail(String email);

    Optional<UserTo> findByPhonenumber(String phonenumber);

    boolean existsByEmailAndEnabled(String email, boolean enabled);

    boolean existsByPhonenumberAndEnabled(String phonenumber, boolean enabled);

    boolean existsByPhonenumber(String phonenumber);

    long deleteByPhonenumber(String phonenumber);

    //dont need
    //void deleteAll(List<UserTo> users);
}
