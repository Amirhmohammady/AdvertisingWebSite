package com.mycompany.advertising.model.dao;

import com.mycompany.advertising.model.to.MultiLanguageCategoryTo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Amir on 7/11/2022.
 */
@Repository
public interface MultiLanguageCategoryRepository extends JpaRepository<MultiLanguageCategoryTo,Long> {
    List<MultiLanguageCategoryTo> findByAdvertiseCategory_Id(Long advId);
}
