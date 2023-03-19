package com.mycompany.advertising.model.dao;

import com.mycompany.advertising.service.api.StorageService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by Amir on 7/25/2022.
 */
@SpringBootTest
@TestPropertySource(locations = "file:D:/Developer/apache-tomcat-9.0.24--windows-x64/amirExtraFiles/adv-web/applicationte-test.properties")
@RunWith(SpringRunner.class)
//@PropertySources({@TestPropertySource(locations = "")})
//@TestPropertySource(properties={"encoding=UTF-8"},
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})//DevToolsDataSourceAutoConfiguration.class})
//@ContextConfiguration(classes = { MainConfig.class })
//@WebMvcTest(AdvertiseRepository.class)
//@DataJpaTest
public class StorageServiceImplTest {
    @Autowired
    StorageService storageService;

    @Before
    public void setUp() {
        System.out.println("================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================");
        System.setProperty("file.encoding", "UTF-8");
    }

    @Test
    public void testDeleteUnusedImages() {
        storageService.deleteUnusedImages();
    }

    @Test
    public void checkListAllImages() {
        System.out.println("==========================================");
        storageService.getAllImagesURL().forEach(System.out::println);
        System.out.println("==========================================");
    }

}