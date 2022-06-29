package com.mycompany.advertising.service.api;

import com.mycompany.advertising.model.to.AdvertiseCategoryTo;

/**
 * Created by Amir on 6/27/2022.
 */
public interface AdvCategoryService {
    boolean editCategory(AdvertiseCategoryTo category);

    AdvertiseCategoryTo saveCategory(AdvertiseCategoryTo category);

    boolean existsById(Long id);

    int deleteByIdCostum(Long id);
}
