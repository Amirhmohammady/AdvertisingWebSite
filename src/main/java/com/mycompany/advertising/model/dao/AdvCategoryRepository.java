package com.mycompany.advertising.model.dao;

import com.mycompany.advertising.model.to.AdvertiseCategoryTo;
import com.mycompany.advertising.model.to.enums.Language;
import com.mycompany.advertising.service.Dto.CategoryIdPair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Amir on 6/27/2022.
 */
@Repository
public interface AdvCategoryRepository extends JpaRepository<AdvertiseCategoryTo, Long> {
    @Modifying
    @Query(value = "DELETE FROM AdvertiseCategoryTo adm where adm.id = ?1")
    int deleteByIdCostum(Long id);

    boolean existsById(Long id);

    List<AdvertiseCategoryTo> findByParent_Id(Long parentId);

    List<AdvertiseCategoryTo> findByParent(AdvertiseCategoryTo parent);

    @Query(value = "SELECT new com.mycompany.advertising.service.Dto.CategoryIdPair(ml.advertiseCategory.id , ml.text) FROM MultiLanguageCategoryTo ml\n" +
            "WHERE ml.advertiseCategory IN (SELECT ac.id FROM AdvertiseCategoryTo ac WHERE ac.parent.id = ?2)\n" +
            "AND ml.language = ?1")
    List<CategoryIdPair> getChildsByLanguageAndId(Language language, Long id);
}
