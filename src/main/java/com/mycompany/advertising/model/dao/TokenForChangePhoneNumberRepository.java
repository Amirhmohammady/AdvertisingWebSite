package com.mycompany.advertising.model.dao;

import com.mycompany.advertising.model.to.TokenForChangePhoneNumberTo;
import com.mycompany.advertising.model.to.UserTo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Amir on 12/28/2021.
 */
@Repository
public interface TokenForChangePhoneNumberRepository extends JpaRepository<TokenForChangePhoneNumberTo, Long> {
    TokenForChangePhoneNumberTo findByUser_Username(String username);

    TokenForChangePhoneNumberTo findByNewPhoneNumber(String newPhoneNumber);

    TokenForChangePhoneNumberTo findByUser(UserTo userTo);

    boolean existsByUser_Username(String username);

    boolean existsByNewPhoneNumber(String newPhoneNumber);

    boolean existsByUser(UserTo userTo);
}
