package com.mycompany.advertising.service;

import com.mycompany.advertising.api.MessageService;
import com.mycompany.advertising.model.dao.MessageRepository;
import com.mycompany.advertising.model.to.MessageTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import org.springframework.data.domain.PageRequest.*;

/**
 * Created by Amir on 10/28/2019.
 */
@Service
public class MessageServiceImpl implements MessageService{
    @Autowired
    MessageRepository messageRepository;

    @Override
    public List<MessageTo> getPageMessages(int page){
        Pageable pageable = PageRequest.of(page - 1, 30);//, Sort.by("text")
        return messageRepository.findAll(pageable).getContent();
    }

    @Override
    public Long addMessage(MessageTo messageTo){
        messageRepository.save(messageTo);
        return messageTo.getID();
    }

    @Override
    public List<MessageTo> getMessages() {
        return messageRepository.findAll();
    }

}
