package com.mycompany.advertising.model.dao;

import com.mycompany.advertising.model.to.AdvertiseCategoryTo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by Amir on 6/27/2022.
 */
@Repository
public interface AdvCategoryRepository extends JpaRepository<AdvertiseCategoryTo, Long> {
    @Modifying
    @Query(value = "DELETE FROM AdvertiseCategoryTo adm where adm.id = ?1")
    int deleteByIdCostum(Long id);

    boolean existsById(Long id);
}
