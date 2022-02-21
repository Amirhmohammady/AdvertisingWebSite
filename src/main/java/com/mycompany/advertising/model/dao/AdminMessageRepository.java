package com.mycompany.advertising.model.dao;

import com.mycompany.advertising.model.to.AdminMessageTo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Created by Amir on 1/27/2022.
 */
@Repository
public interface AdminMessageRepository extends JpaRepository<AdminMessageTo, Long> {
    AdminMessageTo findFirstByOrderByIdDesc();

    Page<AdminMessageTo> findAllByOrderByIdDesc(Pageable page);

    Optional<AdminMessageTo> findById(Long id);

    void deleteById(Long id);

    @Modifying
    @Query(value = "DELETE FROM AdminMessageTo adm where adm.id = ?1")
    int deleteByIdCount(Long id);
}
