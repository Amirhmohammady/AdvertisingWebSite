package com.mycompany.advertising.model.dao;

import com.mycompany.advertising.model.to.AdvertiseCategoryTo;
import com.mycompany.advertising.service.api.AdvCategoryService;
import com.mycompany.advertising.service.language.Language;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Amir on 7/30/2022.
 */
@TestPropertySource(locations = "file:D:/Developer/apache-tomcat-9.0.24--windows-x64/amirExtraFiles/adv-web/applicationte-test.properties")
@SpringBootTest
@RunWith(SpringRunner.class)
public class ServiceTest {
    @Autowired
    AdvCategoryService advCategoryService;

    @Test
    @Rollback(false)
    public void testAdvertiseCategory(){
//        AdvertiseCategoryTo advertiseCategoryTo = new AdvertiseCategoryTo();
//        advertiseCategoryTo.setId((long) 13);
//        Map<Language,String> cats = new HashMap<>();
//        cats.put(Language.fa_IR,"aaaa");
//        advertiseCategoryTo.setCategory(cats);
//        advCategoryService.editCategory(advertiseCategoryTo);
    }
}
