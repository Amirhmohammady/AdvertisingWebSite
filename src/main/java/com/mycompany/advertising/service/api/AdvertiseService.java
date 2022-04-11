package com.mycompany.advertising.service.api;

import com.mycompany.advertising.model.to.AdvertiseTo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

/**
 * Created by Amir on 11/10/2019.
 */
public interface AdvertiseService {

    Long addAdvertise(AdvertiseTo advertiseTo);

    List<AdvertiseTo> getAllAdvertises();

    Optional<AdvertiseTo> getAdvertiseById(Long id);

    Page<AdvertiseTo> getPageNotAcceptedAdvertises(int page);

    Page<AdvertiseTo> getPageAcceptedAdvertises(int page);

    Page<AdvertiseTo> getPageAcceptedAdvertises(int page, String search);

    int deleteAdvertiseById(Long id);
}
