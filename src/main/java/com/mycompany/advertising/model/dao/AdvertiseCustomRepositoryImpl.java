package com.mycompany.advertising.model.dao;

import com.mycompany.advertising.model.to.AdvertiseTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by Amir on 10/7/2022.
 */
//!!!!!!!!! it works without @Repository  !!!!!!!!!!!!!!!!
@Repository
public class AdvertiseCustomRepositoryImpl implements AdvertiseCustomRepository {
    @Autowired
    EntityManager entityManager;

    @Override
    public List<AdvertiseTo> findByImageUrl1Containing_Costum(List<String> domainNames) {
        String nativeQuery = "WITH inps(a) AS ( VALUES ";
        if (domainNames.size() > 0) nativeQuery += "('" + domainNames.get(0) + "')";
        for (int i = 1; i < domainNames.size(); i++)
            nativeQuery += ",('" + domainNames.get(i) + "')";
        nativeQuery += ") SELECT adv.* FROM advertise_to AS adv INNER JOIN inps " +
                "ON adv.image_url1 like CONCAT('%', inps.a, '%') OR adv.small_image_url1 like CONCAT('%', inps.a, '%')";

        List<AdvertiseTo> result = entityManager.createNativeQuery(nativeQuery, AdvertiseTo.class).getResultList();
        return result;
    }
}
