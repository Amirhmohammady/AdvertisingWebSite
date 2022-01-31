package com.mycompany.advertising.service;

import com.mycompany.advertising.model.dao.AdminMessageRepository;
import com.mycompany.advertising.model.to.AdminMessageTo;
import com.mycompany.advertising.service.api.AdminMessageService;
import org.apache.log4j.Logger;
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
    private static final Logger logger = Logger.getLogger(AdminMessageServiceImpl.class);
    @Autowired
    AdminMessageRepository adminMessageRepository;

    @Override
    public AdminMessageTo addAdminMessage(AdminMessageTo adminMessage) {
        AdminMessageTo rslt = adminMessageRepository.save(adminMessage);
        logger.info("admin message saved: " + rslt);
        return rslt;
    }

    @Override
    public AdminMessageTo getLastMessage() {
        AdminMessageTo rslt = adminMessageRepository.findFirstByOrderByIdDesc();
        logger.info("get last admin message: " + rslt);
        return rslt;
    }

    @Override
    public Page<AdminMessageTo> getPageAdminMessage(int page) {
        logger.info("request fo admin message pag " + page);
        Pageable pageable = PageRequest.of((page - 1) * 100, page * 30);//, Sort.by("text")
        return adminMessageRepository.findAll(pageable);
    }
}
