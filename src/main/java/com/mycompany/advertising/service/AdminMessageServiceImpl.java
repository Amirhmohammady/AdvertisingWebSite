package com.mycompany.advertising.service;

import com.mycompany.advertising.model.dao.AdminMessageRepository;
import com.mycompany.advertising.model.to.AdminMessageTo;
import com.mycompany.advertising.service.api.AdminMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Created by Amir on 1/28/2022.
 */
@Service
public class AdminMessageServiceImpl implements AdminMessageService {
    @Autowired
    AdminMessageRepository adminMessageRepository;

    @Override
    public AdminMessageTo getLastMessage() {
        return adminMessageRepository.findTopByOrderByIdDesc();
    }

    @Override
    public Page<AdminMessageTo> getPageAdminMessage(int page) {
        Pageable pageable = PageRequest.of((page - 1) * 100, page * 30);//, Sort.by("text")
        return adminMessageRepository.findAll(pageable);
    }
}
