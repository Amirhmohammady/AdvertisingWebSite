package com.mycompany.advertising.model.dao;

import com.mycompany.advertising.model.to.AdvertiseTo;
import com.mycompany.advertising.model.to.enums.AdvertiseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Created by Amir on 10/28/2019.
 */
@Repository
public interface AdvertiseRepository extends JpaRepository<AdvertiseTo, Long>, AdvertiseCustomRepository {
    //List<MessageTo> findAllByPrice(double price, Pageable pageable);
    //List<Email> findByEmailIdInAndPincodeIn(List<String> emails, List<String> pinCodes);
    //List<Transaction> findAllByIdOrParentId(Long id, Long parentId);
    //Page<AdvertiseTo> findAllByTextContainingOrWebSiteLinkContainingOrderByStartdateDesc(String text, String webSiteLink, Pageable pageable);
    Page<AdvertiseTo> findAllByStatusAndTextContainingOrTitleContainingOrderByStartdateDesc(AdvertiseStatus status, String text, String title, Pageable pageable);

    Optional<AdvertiseTo> findByImageUrl1OrSmallImageUrl1(String imageUrl1, String smallImageUrl1);

    Page<AdvertiseTo> findAllByStatusOrderByStartdateDesc(AdvertiseStatus status, Pageable page);

    Page<AdvertiseTo> findAllByStatusOrderByStartdateAsc(AdvertiseStatus status, Pageable page);

    Page<AdvertiseTo> findAll(Pageable page);

    List<AdvertiseTo> findAll();

    @Modifying
    @Query(value = "DELETE FROM AdvertiseTo adv where adv.id = ?1")
    int deleteByIdCount(Long id);

    //List<User> findByEmailAddressAndLastname(String emailAddress, String lastname);
//    @Query(nativeQuery = true, value = "SELECT jt.advertise_id AS id FROM junction_table_advertise_category jt WHERE jt.category_id IN (SELECT jt2.category_id FROM junction_table_advertise_category jt2 WHERE jt2.advertise_id= :advId) AND jt.advertise_id != :advId GROUP BY jt.advertise_id ORDER BY COUNT(jt.category_id) DESC LIMIT :listLimit")
    @Query(nativeQuery = true, value = "SELECT * FROM advertise_to adv INNER JOIN junction_table_advertise_category jt ON adv.id = jt.advertise_id AND jt.category_id IN (SELECT jt2.category_id FROM junction_table_advertise_category jt2 WHERE jt2.advertise_id = :advId) AND adv.id != :advId GROUP BY jt.advertise_id ORDER BY COUNT(jt.category_id) DESC LIMIT :listLimit")
    List<AdvertiseTo> getCommonsAdvertisesByCategory(@Param("advId") Long advId, @Param("listLimit") int listLimit);

    //    Set<AdvertiseTo> findByImageUrl1ContainingInOrSmallImageUrl1ContainingIn(Set<String> domainNames);
    @Query(nativeQuery = true, value = "WITH inps(a) AS ( VALUES (:domainNames)) " +
            "SELECT adv.* FROM advertise_to AS adv INNER JOIN inps " +
            "ON adv.image_url1 like CONCAT('%', inps.a, '%') OR adv.small_image_url1 like CONCAT('%', inps.a, '%')")
    Set<AdvertiseTo> findByImageUrl1Containing_Costum2(@Param("domainNames") Set<String> domainNames);
}


