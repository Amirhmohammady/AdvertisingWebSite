package com.mycompany.advertising.api;

import com.mycompany.advertising.model.to.AvertiseTo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

/**
 * Created by Amir on 11/10/2019.
 */
public interface AvertiseService {

    Long addAvertise(AvertiseTo avertiseTo);

    List<AvertiseTo> getAllAvertises();

    Optional<AvertiseTo> getAvertiseById(Long id);

    Page<AvertiseTo> getPageAvertises(int page);

    Page<AvertiseTo> getPageAvertises(int page, String search);

    void deleteAvertiseById(Long id);
}
