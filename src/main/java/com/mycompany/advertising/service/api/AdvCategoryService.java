package com.mycompany.advertising.service.api;

import com.mycompany.advertising.model.to.AdvertiseCategoryTo;
import com.mycompany.advertising.service.language.Language;
import com.mycompany.advertising.service.Dto.CategoryIdPair;

import java.util.List;
import java.util.Optional;

/**
 * Created by Amir on 6/27/2022.
 */
public interface AdvCategoryService {
    AdvertiseCategoryTo editCategory(AdvertiseCategoryTo category);

    AdvertiseCategoryTo addCategory(AdvertiseCategoryTo category);

    Optional<AdvertiseCategoryTo> getCategoryById(Long id);

    boolean existsById(Long id);

    int deleteByIdCostum(Long id);

    List<AdvertiseCategoryTo> getRootCtegories();

    List<CategoryIdPair> getRootCtegories(Language language);

    List<AdvertiseCategoryTo> getChildCtegories(Long parentId);

    List<CategoryIdPair> getChildsByLanguageAndId(Language language, Long id);
}
