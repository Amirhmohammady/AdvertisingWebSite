package com.mycompany.advertising.model.dao;

import com.mycompany.advertising.model.to.AdvertiseCategoryTo;
import com.mycompany.advertising.service.Dto.CategoryIdPair;
import com.mycompany.advertising.service.language.Language;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Amir on 7/25/2022.
 */
//@SpringBootTest
@TestPropertySource(locations = "file:D:/Developer/apache-tomcat-9.0.24--windows-x64/amirExtraFiles/adv-web/applicationte-test.properties")
@RunWith(SpringRunner.class)
//@PropertySources({@TestPropertySource(locations = "")})
//@TestPropertySource(properties={"encoding=UTF-8"},
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})//DevToolsDataSourceAutoConfiguration.class})
//@ContextConfiguration(classes = { MainConfig.class })
//@WebMvcTest(AdvertiseRepository.class)
@DataJpaTest
public class AdvertiseRepositoryTest {
    @Autowired
    AdvertiseRepository advertiseRepository;
    @Autowired
    AdvCategoryRepository advCategoryRepository;

    @Before
    public void setUp() {
        System.out.println("================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================");
        System.setProperty("file.encoding", "UTF-8");
    }

    //@Test
    @Transactional
    @Rollback(false)
    public void addADvertiseCategory() {
        AdvertiseCategoryTo advertiseCategoryTo1 = new AdvertiseCategoryTo();
        advertiseCategoryTo1.setCategory(new HashMap<Language, String>() {{
            put(Language.en_US, "value11");
            put(Language.fa_IR, "value12");
        }});
        AdvertiseCategoryTo advertiseCategoryTo2 = new AdvertiseCategoryTo();
        advertiseCategoryTo2.setCategory(new HashMap<Language, String>() {{
            put(Language.en_US, "value21");
            put(Language.fa_IR, "value22");
        }});
        advertiseCategoryTo2.setParent(advertiseCategoryTo1);
//        advCategoryRepository.save(advertiseCategoryTo1);
//        advCategoryRepository.save(advertiseCategoryTo2);
    }

    //@Test
    @Transactional
    public void testCommonAdvertises() {
//        assertNotNull(jdbcTemplate);
//        assertNotNull(entityManager);
        //List<AdvertiseTo> result = advertiseRepository.getCommonsAdvertisesByCategory((long) 76, 2);
        //Assert.assertEquals(result.size(), 2);
        //System.out.println(result.get(0).getCategories());
        Assert.assertEquals(2, 2);
    }

    @Test
    @Transactional
    public void getAdvertiseCategory() {
//        List<CategoryIdPair> result = advCategoryRepository.getChildsByLanguageAndId(Language.en_US, (long) 13);
        System.out.println("-----------------------------------------------------------------------------------");
//        result.forEach(System.out::println);
//        System.out.println(result.size());
        System.out.println(Language.en_US.ordinal());
        System.out.println(Language.fa_IR.ordinal());
        System.out.println("-----------------------------------------------------------------------------------");
    }
}