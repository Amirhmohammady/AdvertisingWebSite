package com.mycompany.advertising.model.dao;

import com.mycompany.advertising.model.to.UserTo;
import com.mycompany.advertising.model.to.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Amir on 9/10/2021.
 */
public interface VerificationTokenRepository extends JpaRepository<VerificationToken,Long> {
    VerificationToken findByToken(String token);

    VerificationToken findByUser(UserTo user);
}
