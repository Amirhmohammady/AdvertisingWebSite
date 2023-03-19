package com.mycompany.advertising.model.dao;

import com.mycompany.advertising.model.to.AdvertiseTo;

import java.util.List;

/**
 * Created by Amir on 10/7/2022.
 */
public interface AdvertiseCustomRepository {
    List<AdvertiseTo> findByImageUrl1Containing_Costum(List<String> domainNames);
}
