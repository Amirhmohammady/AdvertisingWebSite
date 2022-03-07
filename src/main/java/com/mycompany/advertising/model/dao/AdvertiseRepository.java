package com.mycompany.advertising.model.dao;

import com.mycompany.advertising.model.to.AdvertiseTo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Amir on 10/28/2019.
 */
@Repository
public interface AdvertiseRepository extends JpaRepository<AdvertiseTo,Long> {
    //List<MessageTo> findAllByPrice(double price, Pageable pageable);
    //List<Email> findByEmailIdInAndPincodeIn(List<String> emails, List<String> pinCodes);
    //List<Transaction> findAllByIdOrParentId(Long id, Long parentId);
    Page<AdvertiseTo> findAllByTextOrWebSiteLink(String text, String webSiteLink, Pageable pageable);
    Page<AdvertiseTo> findAll(Pageable page);
    List<AdvertiseTo> findAll();
    List<AdvertiseTo> findAllById(Long id);
    //List<User> findByEmailAddressAndLastname(String emailAddress, String lastname);
}


