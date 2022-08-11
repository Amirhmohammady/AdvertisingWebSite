package com.mycompany.advertising.service;

import com.mycompany.advertising.components.api.AuthenticationFacade;
import com.mycompany.advertising.model.dao.AdvertiseRepository;
import com.mycompany.advertising.model.to.AdvertiseTo;
import com.mycompany.advertising.model.to.enums.AdvertiseStatus;
import com.mycompany.advertising.service.api.AdvertiseService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Created by Amir on 10/28/2019.
 */
@Service
public class AdvertiseServiceImpl implements AdvertiseService {
    private static final Logger logger = Logger.getLogger(AdvertiseServiceImpl.class);
    @Autowired
    AuthenticationFacade authenticationFacade;
    @Autowired
    AdvertiseRepository advertiseRepository;
    @Value("${advertises.per.index.page}")
    private int advPerPage;
    private int unAcceptedAdvPerPagee = 20;

    @Override
    public Page<AdvertiseTo> getPageAcceptedAdvertises(int page) {
        Pageable pageable = PageRequest.of(page - 1, advPerPage);//, Sort.by("text")
        Page<AdvertiseTo> result = advertiseRepository.findAllByStatusOrderByStartdateDesc(AdvertiseStatus.Accepted, pageable);//.getContent();
        logger.info("get advertises at page " + page + " reult: " + result.getTotalElements());
        return result;
    }

    @Override
    public Page<AdvertiseTo> getPageAcceptedAdvertises(int page, String search) {
        Pageable pageable = PageRequest.of(page - 1, advPerPage);//, Sort.by("text")
        //messageRepository.findAllByTextOrTelegramlink(search, search, pageable).getTotalPages();
        return advertiseRepository.findAllByStatusAndTextContainingOrTitleContainingOrderByStartdateDesc(AdvertiseStatus.Accepted, search, search, pageable);//.getContent();
    }

    @Override
    public AdvertiseTo saveAdvertise(AdvertiseTo advertise) {
        return advertiseRepository.save(advertise);
        //return messageTo.getId();
    }

    @Override
    public List<AdvertiseTo> getAllAdvertises() {
        return advertiseRepository.findAll();
    }

    @Override
    public Optional<AdvertiseTo> getAdvertiseById(Long id) {
        return advertiseRepository.findById(id);
    }

    @Override
    public Optional<AdvertiseTo> acceptAdvertiseById(Long id) {
        logger.info("Admin id:" + authenticationFacade.getCurrentUser().getId() + " try to accept advertise id:" + id);
        AdvertiseTo advertise = advertiseRepository.getOne(id);
        if (advertise == null || advertise.getStatus() != AdvertiseStatus.Not_Accepted) {
            logger.warn("can not accept advertise by id:" + id);
            return Optional.empty();
        }
        advertise.setStatus(AdvertiseStatus.Accepted);
        advertise.setStartdate(LocalDateTime.now());
        logger.info("advertise by id:" + id + " accepted successfully");
        return Optional.of(advertiseRepository.save(advertise));
    }

    @Override
    public Optional<AdvertiseTo> rejectAdvertiseById(Long id) {
        logger.info("Admin id:" + authenticationFacade.getCurrentUser().getId() + " try to reject advertise id:" + id);
        AdvertiseTo advertise = advertiseRepository.getOne(id);
        if (advertise == null || advertise.getStatus() != AdvertiseStatus.Not_Accepted) {
            logger.warn("can not reject advertise by id:" + id);
            return Optional.empty();
        }
        advertise.setStatus(AdvertiseStatus.Rejected);
        advertise.setStartdate(LocalDateTime.now());
        logger.info("advertise by id:" + id + " rejected successfully");
        return Optional.of(advertiseRepository.save(advertise));
    }

    @Override
    public Page<AdvertiseTo> getPageNotAcceptedAdvertises(int page) {
        Pageable pageable = PageRequest.of(page - 1, unAcceptedAdvPerPagee);//, Sort.by("text")
        Page<AdvertiseTo> result = advertiseRepository.findAllByStatusOrderByStartdateAsc(AdvertiseStatus.Not_Accepted, pageable);//.getContent();
        logger.info("get unaccepted advertises at page " + page + " reult: " + result.getTotalElements());
        return result;
    }
    /*public List<MessageTo> getMessagesById(Long id) {
        return messageRepository.findAllById(id);
    }*/

    @Override
    @Transactional
    public int deleteAdvertiseById(Long id) {
        return advertiseRepository.deleteByIdCount(id);
    }

    /*@Override
    public Optional<AdvertiseTo> editAdvertises(AdvertiseTo advertise) {
        Optional<AdvertiseTo> advertiseopt = advertiseRepository.findById(advertise.getId());
        if (!advertiseopt.isPresent()) return Optional.empty();
        return Optional.of(advertiseRepository.save(advertise));
    }*/
}
