package com.mycompany.advertising.model.dao;

import com.mycompany.advertising.model.to.AdvertiseTo;
import com.mycompany.advertising.model.to.enums.AdvertiseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Amir on 10/28/2019.
 */
@Repository
public interface AdvertiseRepository extends JpaRepository<AdvertiseTo, Long> {
    //List<MessageTo> findAllByPrice(double price, Pageable pageable);
    //List<Email> findByEmailIdInAndPincodeIn(List<String> emails, List<String> pinCodes);
    //List<Transaction> findAllByIdOrParentId(Long id, Long parentId);
    //Page<AdvertiseTo> findAllByTextContainingOrWebSiteLinkContainingOrderByStartdateDesc(String text, String webSiteLink, Pageable pageable);
    Page<AdvertiseTo> findAllByStatusAndTextContainingOrTitleContainingOrderByStartdateDesc(AdvertiseStatus status, String text, String title, Pageable pageable);

    Page<AdvertiseTo> findAllByStatusOrderByStartdateDesc(AdvertiseStatus status, Pageable page);

    Page<AdvertiseTo> findAll(Pageable page);

    List<AdvertiseTo> findAll();

    @Modifying
    @Query(value = "DELETE FROM AdvertiseTo adv where adv.id = ?1")
    int deleteByIdCount(Long id);
    //List<User> findByEmailAddressAndLastname(String emailAddress, String lastname);
}


