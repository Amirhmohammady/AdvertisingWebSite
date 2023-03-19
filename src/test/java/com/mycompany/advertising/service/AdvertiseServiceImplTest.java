package com.mycompany.advertising.service;

import com.mycompany.advertising.service.api.AdvertiseService;
import com.mycompany.advertising.service.api.StorageService;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Amir on 10/7/2022.
 */
@SpringBootTest
@TestPropertySource(locations = "file:D:/Developer/apache-tomcat-9.0.24--windows-x64/amirExtraFiles/adv-web/applicationte-test.properties")
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AdvertiseServiceImplTest extends TestCase {
    @Autowired
    StorageService storageService;
    @Autowired
    AdvertiseService advertiseService;

    public void testGetPageAcceptedAdvertises() throws Exception {

    }

    public void testGetPageAcceptedAdvertises1() throws Exception {

    }

    public void testSaveAdvertise() throws Exception {

    }

    public void testPublishAdvertiseByUser() throws Exception {

    }

    public void testUploadImage() throws Exception {

    }

    public void testGetAllAdvertises() throws Exception {

    }

    public void testGetAdvertiseById() throws Exception {

    }

    public void testAcceptAdvertiseById() throws Exception {

    }

    public void testRejectAdvertiseById() throws Exception {

    }

    public void testGetPageNotAcceptedAdvertises() throws Exception {

    }

    public void testDeleteAdvertiseById() throws Exception {

    }

    public void testFindByImage() throws Exception {

    }

    @Test
    @Transactional
    public void testFineeeeedAllByDomainNames() throws Exception {
        System.out.println("==================================");
        advertiseService.findAllByDomainNames(storageService.getAllDomainsName()).forEach(System.out::println);
        System.out.println("==================================");
    }
}