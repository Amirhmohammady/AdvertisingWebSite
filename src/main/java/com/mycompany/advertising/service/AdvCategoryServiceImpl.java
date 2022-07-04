package com.mycompany.advertising.service;

import com.mycompany.advertising.model.dao.AdvCategoryRepository;
import com.mycompany.advertising.model.to.AdvertiseCategoryTo;
import com.mycompany.advertising.model.to.enums.Language;
import com.mycompany.advertising.service.Dto.CategoryIdPair;
import com.mycompany.advertising.service.api.AdvCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    public Optional<AdvertiseCategoryTo> getCategoryById(Long id) {
        return advCategoryRepository.findById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return advCategoryRepository.existsById(id);
    }

    @Override
    public int deleteByIdCostum(Long id) {
        return advCategoryRepository.deleteByIdCostum(id);
    }

    @Override
    public List<AdvertiseCategoryTo> getRootCtegories() {
        return advCategoryRepository.findByParent(null);
    }

    @Override
    public List<AdvertiseCategoryTo> getChildCtegories(Long parentId) {
        return advCategoryRepository.findByParent_Id(parentId);
    }

    @Override
    public List<CategoryIdPair> getChildsByLanguageAndId(Language language, Long id) {
        return advCategoryRepository.getChildsByLanguageAndId(language, id);
    }
}
