package com.mycompany.advertising.service;

import com.mycompany.advertising.model.dao.AdvCategoryRepository;
import com.mycompany.advertising.model.to.AdvertiseCategoryTo;
import com.mycompany.advertising.service.api.AdvCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Amir on 6/28/2022.
 */
@Service
public class AdvCategoryServiceImpl implements AdvCategoryService {
    @Autowired
    AdvCategoryRepository advCategoryRepository;

    @Override
    public boolean editCategory(AdvertiseCategoryTo category) {
        if (existsById(category.getId())){
            advCategoryRepository.save(category);
            return true;
        }
        return false;
    }

    @Override
    public AdvertiseCategoryTo saveCategory(AdvertiseCategoryTo category) {
        return advCategoryRepository.save(category);
    }

    @Override
    public boolean existsById(Long id) {
        return advCategoryRepository.existsById(id);
    }

    @Override
    public int deleteByIdCostum(Long id) {
        return advCategoryRepository.deleteByIdCostum(id);
    }
}
