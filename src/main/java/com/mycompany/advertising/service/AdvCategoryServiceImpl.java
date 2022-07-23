package com.mycompany.advertising.service;

import com.mycompany.advertising.model.dao.AdvCategoryRepository;
import com.mycompany.advertising.model.to.AdvertiseCategoryTo;
import com.mycompany.advertising.model.to.MultiLanguageCategoryTo;
import com.mycompany.advertising.service.Dto.CategoryIdPair;
import com.mycompany.advertising.service.api.AdvCategoryService;
import com.mycompany.advertising.service.language.Language;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Created by Amir on 6/28/2022.
 */
@Service
public class AdvCategoryServiceImpl implements AdvCategoryService {
    private static final Logger logger = Logger.getLogger(AdvCategoryServiceImpl.class);

    @Autowired
    AdvCategoryRepository advCategoryRepository;

    @Override
    @Transactional
    public AdvertiseCategoryTo editCategory(AdvertiseCategoryTo category) {
        Optional<AdvertiseCategoryTo> advTo = advCategoryRepository.findById(category.getId());
        if (!advTo.isPresent()) return null;
        List<MultiLanguageCategoryTo> multiLanguageCategoryTos = advTo.get().getCategory();
        for (MultiLanguageCategoryTo mlcInput : category.getCategory()) {
            boolean isFound = false;
            for (MultiLanguageCategoryTo mlc : multiLanguageCategoryTos)
                if (mlc.getLanguage().equals(mlcInput.getLanguage())) {
                    mlc.setText(mlcInput.getText());
                    isFound = true;
                    break;
                }
            if (!isFound) {
                mlcInput.setAdvertiseCategory(advTo.get());
                multiLanguageCategoryTos.add(mlcInput);
            }
        }
        System.out.println(advTo);
        return advCategoryRepository.save(advTo.get());
    }

    @Override
    @Transactional
    public AdvertiseCategoryTo addCategory(AdvertiseCategoryTo category) {
        for (int i = 0; i < category.getCategory().size(); i++)
            category.getCategory().get(i).setAdvertiseCategory(category);
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
    @Transactional
    public int deleteByIdCostum(Long id) {
        return advCategoryRepository.deleteByIdCostum(id);
    }

    @Override
    public List<AdvertiseCategoryTo> getRootCtegories() {
        return advCategoryRepository.findByParent(null);
    }

    @Override
    public List<CategoryIdPair> getRootCtegories(Language language) {
        List<CategoryIdPair> rslt = advCategoryRepository.getChildsByLanguageByNullId(language);
        logger.info("Root Categories in " + language + " are: " + rslt);
        return rslt;
    }

    @Override
    public List<AdvertiseCategoryTo> getChildCtegories(Long parentId) {
        return advCategoryRepository.findByParent_Id(parentId);
    }

    @Override
    public List<CategoryIdPair> getChildsByLanguageAndId(Language language, Long pid) {
        if (pid == 0) return getRootCtegories(language);
        return advCategoryRepository.getChildsByLanguageAndId(language, pid);
    }
}
