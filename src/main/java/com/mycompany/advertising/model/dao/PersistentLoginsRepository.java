package com.mycompany.advertising.model.dao;

import com.mycompany.advertising.model.to.PersistentLoginsTo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Amir on 12/10/2021.
 */
@Repository
public interface PersistentLoginsRepository extends JpaRepository<PersistentLoginsTo, Long> {
    PersistentLoginsTo findTopBySeries(String series);
    void deleteByUsername(String username);
}
