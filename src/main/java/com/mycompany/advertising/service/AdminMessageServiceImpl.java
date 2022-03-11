package com.mycompany.advertising.service;

import com.mycompany.advertising.model.dao.AdminMessageRepository;
import com.mycompany.advertising.model.dao.UserCommentRepository;
import com.mycompany.advertising.model.to.AdminMessageTo;
import com.mycompany.advertising.model.to.UserCommentTo;
import com.mycompany.advertising.service.api.AdminMessageService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * Created by Amir on 1/28/2022.
 */
@Service
public class AdminMessageServiceImpl implements AdminMessageService {
    private static final Logger logger = Logger.getLogger(AdminMessageServiceImpl.class);
    @Autowired
    UserCommentRepository userCommentRepository;
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
        Pageable pageable = PageRequest.of((page - 1) * 30, page * 30);//, Sort.by("text")
        return adminMessageRepository.findAllByOrderByIdDesc(pageable);
    }

    @Override
    public List<AdminMessageTo> getAllAdminMessage() {
        return adminMessageRepository.findAll();
    }

    @Override
    public Optional<AdminMessageTo> getAdminMessageById(Long id) {
        return adminMessageRepository.findById(id);
    }

    @Override
    public void addUserComment(UserCommentTo userCommentTo) {
        userCommentRepository.save(userCommentTo);
    }

    @Override
    @Transactional
    public int deleteUserCommentById(long id) {
        return userCommentRepository.deleteByIdCont(id);
    }

    @Override
    @Transactional
    public int deleteAdminMessageById(long id) {
        return adminMessageRepository.deleteByIdCount(id);
    }
}
