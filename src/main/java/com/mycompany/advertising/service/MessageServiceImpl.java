package com.mycompany.advertising.service;

import com.mycompany.advertising.api.MessageService;
import com.mycompany.advertising.model.dao.MessageRepository;
import com.mycompany.advertising.model.to.MessageTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Created by Amir on 10/28/2019.
 */
@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    MessageRepository messageRepository;

    @Override
    public Page<MessageTo> getPageMessages(int page) {
        Pageable pageable = PageRequest.of((page - 1) * 30, page * 30);//, Sort.by("text")
        return messageRepository.findAll(pageable);//.getContent();
    }

    @Override
    public Page<MessageTo> getPageMessages(int page, String search) {
        Pageable pageable = PageRequest.of((page - 1) * 30, page * 30);//, Sort.by("text")
        //messageRepository.findAllByTextOrTelegramlink(search, search, pageable).getTotalPages();
        return messageRepository.findAllByTextOrTelegramlink(search, search, pageable);//.getContent();
    }

    @Override
    public Long addMessage(MessageTo messageTo) {
        messageRepository.save(messageTo);
        return messageTo.getId();
    }

    @Override
    public List<MessageTo> getAllMessages() {
        return messageRepository.findAll();
    }

    @Override
    public Optional<MessageTo> getMessageById(Long id){
        return messageRepository.findById(id);
    }
    /*public List<MessageTo> getMessagesById(Long id) {
        return messageRepository.findAllById(id);
    }*/

    @Override
    public void deleteMessageById(Long id) {
        messageRepository.deleteById(id);
    }
}
