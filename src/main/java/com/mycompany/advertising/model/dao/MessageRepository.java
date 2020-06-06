package com.mycompany.advertising.model.dao;

import com.mycompany.advertising.model.to.MessageTo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by Amir on 10/28/2019.
 */
public interface MessageRepository extends JpaRepository<MessageTo,Long> {
    Page<MessageTo> findAll(Pageable page);
    List<MessageTo> findAll();
}


