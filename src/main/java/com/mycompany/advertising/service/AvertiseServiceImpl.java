package com.mycompany.advertising.service;

import com.mycompany.advertising.service.api.AvertiseService;
import com.mycompany.advertising.model.dao.AvertiseRepository;
import com.mycompany.advertising.model.to.AvertiseTo;
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
public class AvertiseServiceImpl implements AvertiseService {
    @Autowired
    AvertiseRepository avertiseRepository;

    @Override
    public Page<AvertiseTo> getPageAvertises(int page) {
        Pageable pageable = PageRequest.of((page - 1) * 30, page * 30);//, Sort.by("text")
        return avertiseRepository.findAll(pageable);//.getContent();
    }

    @Override
    public Page<AvertiseTo> getPageAvertises(int page, String search) {
        Pageable pageable = PageRequest.of((page - 1) * 30, page * 30);//, Sort.by("text")
        //messageRepository.findAllByTextOrTelegramlink(search, search, pageable).getTotalPages();
        return avertiseRepository.findAllByTextOrTelegramlink(search, search, pageable);//.getContent();
    }

    @Override
    public Long addAvertise(AvertiseTo messageTo) {
        avertiseRepository.save(messageTo);
        return messageTo.getId();
    }

    @Override
    public List<AvertiseTo> getAllAvertises() {
        return avertiseRepository.findAll();
    }

    @Override
    public Optional<AvertiseTo> getAvertiseById(Long id){
        return avertiseRepository.findById(id);
    }
    /*public List<MessageTo> getMessagesById(Long id) {
        return messageRepository.findAllById(id);
    }*/

    @Override
    public void deleteAvertiseById(Long id) {
        avertiseRepository.deleteById(id);
    }
}
