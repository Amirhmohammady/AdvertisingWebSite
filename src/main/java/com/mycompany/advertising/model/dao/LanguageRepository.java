package com.mycompany.advertising.model.dao;

import com.mycompany.advertising.model.to.LanguageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Amir on 8/19/2020.
 */
@Repository
public interface LanguageRepository extends JpaRepository<LanguageEntity, Long> {

    LanguageEntity findTopByKkeyAndLocale(String kkey, String locale);

    LanguageEntity findTopByLocaleOrderByIdDesc(String locale);//findTopByOrderByIdDesc
}