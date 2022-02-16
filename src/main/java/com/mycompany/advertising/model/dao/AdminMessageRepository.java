package com.mycompany.advertising.model.dao;

import com.mycompany.advertising.model.to.AdminMessageTo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Amir on 1/27/2022.
 */
@Repository
public interface AdminMessageRepository extends JpaRepository<AdminMessageTo, Long> {
    AdminMessageTo findFirstByOrderByIdDesc();

    Page<AdminMessageTo> findAllByOrderByIdDesc(Pageable page);

    AdminMessageTo findById(long id);
}
