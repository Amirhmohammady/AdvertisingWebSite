package com.mycompany.advertising.model.dao;

import com.mycompany.advertising.model.to.UserTo;
import com.mycompany.advertising.model.to.VerificationTokenTo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Amir on 9/10/2021.
 */
public interface VerificationTokenRepository extends JpaRepository<VerificationTokenTo,Long> {
    VerificationTokenTo findByToken(String token);

    VerificationTokenTo findByUser(UserTo user);
}
