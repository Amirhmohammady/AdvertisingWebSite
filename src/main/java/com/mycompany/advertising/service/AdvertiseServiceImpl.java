package com.mycompany.advertising.service;

import com.mycompany.advertising.components.api.AuthenticationFacade;
import com.mycompany.advertising.model.dao.AdvertiseRepository;
import com.mycompany.advertising.model.to.AdvertiseTo;
import com.mycompany.advertising.model.to.UserTo;
import com.mycompany.advertising.model.to.enums.AdvertiseStatus;
import com.mycompany.advertising.model.to.enums.Role;
import com.mycompany.advertising.service.api.AdvertiseService;
import com.mycompany.advertising.service.api.StorageService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Created by Amir on 10/28/2019.
 */
@Service
public class AdvertiseServiceImpl implements AdvertiseService {
    private static final Logger logger = Logger.getLogger(AdvertiseServiceImpl.class);
    @Autowired
    StorageService storageService;
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
    @Transactional
    public AdvertiseTo publishAdvertiseByUser(AdvertiseTo advertise) {
        UserTo userTo = authenticationFacade.getCurrentUser();
        advertise.setStatus(AdvertiseStatus.Not_Accepted);
        advertise.setUserTo(userTo);
        advertise.setStartdate(LocalDateTime.now());
        System.out.println(advertise);
        AdvertiseTo rslt = advertiseRepository.save(advertise);
        logger.info("user " + userTo.getUsername() + " successfully published advertise " + advertise.getId());
        return rslt;
    }

    @Override
    public void uploadImage(AdvertiseTo advertise, MultipartFile pic1) {
        if (pic1 != null && !pic1.isEmpty()) {
            try {
                List<URL> files;
                files = storageService.storeImage(pic1);
                advertise.setImageUrl1(files.get(0));
                advertise.setSmallImageUrl1(files.get(1));
            } catch (Exception e) {
                e.printStackTrace();
                logger.warn("couldn't upload image " + pic1.getOriginalFilename() + " because of " + e.getMessage());
            }
        }
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
        advertise.setExpiredate(LocalDateTime.now().plusDays(14));
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
        Optional<AdvertiseTo> advertise = advertiseRepository.findById(id);
        UserTo userTo = authenticationFacade.getCurrentUser();
        if (advertise.isPresent()) {
            if (userTo.hasRole(Role.ROLE_ADMIN)) {
                advertise.get().setStatus(AdvertiseStatus.Deleted_By_Admin);
                return 1;
            } else if (userTo.hasRole(Role.ROLE_USER)) {
                advertise.get().setStatus(AdvertiseStatus.Deleted_By_User);
                return 1;
            }
        }
        return 0;
        //return advertiseRepository.deleteByIdCount(id);
    }

    @Override
    public Optional<AdvertiseTo> findByImage(String imageUrl) {
        return advertiseRepository.findByImageUrl1OrSmallImageUrl1(imageUrl, imageUrl);
    }

    @Override
    public List<AdvertiseTo> findAllByDomainNames(List<String> domainNames) {
        return advertiseRepository.findByImageUrl1Containing_Costum(domainNames);
    }

    /*@Override
    public Optional<AdvertiseTo> editAdvertises(AdvertiseTo advertise) {
        Optional<AdvertiseTo> advertiseopt = advertiseRepository.findById(advertise.getId());
        if (!advertiseopt.isPresent()) return Optional.empty();
        return Optional.of(advertiseRepository.save(advertise));
    }*/
}
