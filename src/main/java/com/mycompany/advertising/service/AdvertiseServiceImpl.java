package com.mycompany.advertising.service;

import com.mycompany.advertising.model.dao.AdvertiseRepository;
import com.mycompany.advertising.model.to.AdvertiseTo;
import com.mycompany.advertising.service.api.AdvertiseService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Created by Amir on 10/28/2019.
 */
@Service
public class AdvertiseServiceImpl implements AdvertiseService {
    private static final Logger logger = Logger.getLogger(AdvertiseServiceImpl.class);
    @Autowired
    AdvertiseRepository advertiseRepository;

    @Override
    public Page<AdvertiseTo> getPageAdvertises(int page) {
        Pageable pageable = PageRequest.of(page - 1, 30);//, Sort.by("text")
        Page<AdvertiseTo> result = advertiseRepository.findAll(pageable);//.getContent();
        logger.info("get advertises at page " + page + " reult: " + result.getTotalElements());
        return result;
    }

    @Override
    public Page<AdvertiseTo> getPageAdvertises(int page, String search) {
        Pageable pageable = PageRequest.of(page - 1, 30);//, Sort.by("text")
        //messageRepository.findAllByTextOrTelegramlink(search, search, pageable).getTotalPages();
        return advertiseRepository.findAllByTextContainingOrWebSiteLinkContaining(search, search, pageable);//.getContent();
    }

    @Override
    public Long addAdvertise(AdvertiseTo messageTo) {
        advertiseRepository.save(messageTo);
        return messageTo.getId();
    }

    @Override
    public List<AdvertiseTo> getAllAdvertises() {
        return advertiseRepository.findAll();
    }

    @Override
    public Optional<AdvertiseTo> getAdvertiseById(Long id) {
        return advertiseRepository.findById(id);
    }
    /*public List<MessageTo> getMessagesById(Long id) {
        return messageRepository.findAllById(id);
    }*/

    @Override
    @Transactional
    public int deleteAdvertiseById(Long id) {
        return advertiseRepository.deleteByIdCount(id);
    }
}
