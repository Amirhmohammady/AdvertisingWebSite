package com.mycompany.advertising.model.dao;

import com.mycompany.advertising.model.to.AdvertiseCategoryTo;
import com.mycompany.advertising.service.Dto.CategoryIdPair;
import com.mycompany.advertising.service.language.Language;
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

    @Query(value = "SELECT new com.mycompany.advertising.service.Dto.CategoryIdPair(ac.id , ml.text) FROM AdvertiseCategoryTo ac LEFT JOIN MultiLanguageCategoryTo ml ON ml.advertiseCategory = ac AND ml.language = ?1 WHERE ac.parent.id = ?2")
    List<CategoryIdPair> getChildsByLanguageAndId(Language language, Long id);

    @Query(value = "SELECT new com.mycompany.advertising.service.Dto.CategoryIdPair(ac.id , ml.text) FROM AdvertiseCategoryTo ac LEFT JOIN MultiLanguageCategoryTo ml ON ml.advertiseCategory = ac AND ml.language = ?1 WHERE ac.parent IS NULL")
    /*(value = "SELECT new com.mycompany.advertising.service.Dto.CategoryIdPair(ml.advertiseCategory.id , ml.text) FROM MultiLanguageCategoryTo ml\n" +
            "WHERE ml.advertiseCategory IN (SELECT ac.id FROM AdvertiseCategoryTo ac WHERE ac.parent.id IS NULL)\n" +
            "AND ml.language = ?1")*/
    List<CategoryIdPair> getChildsByLanguageByNullId(Language language);
}
