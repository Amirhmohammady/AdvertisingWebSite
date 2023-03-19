package com.mycompany.advertising.controller;

import com.mycompany.advertising.components.api.AuthenticationFacade;
import com.mycompany.advertising.model.to.AdvertiseTo;
import com.mycompany.advertising.service.api.AdvCategoryService;
import com.mycompany.advertising.service.api.AdvertiseService;
import com.mycompany.advertising.service.api.OnlineAdvertiseDataService;
import com.mycompany.advertising.service.api.StorageService;
import com.mycompany.advertising.service.util.OnlineAdvertiseData;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Amir on 6/5/2022.
 */
@RestController
@RequestMapping("/api")
public class AdvertiseRestController {
    private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());
    @Autowired
    OnlineAdvertiseDataService onlineService;
    @Autowired
    AdvCategoryService advCategoryService;
    @Autowired
    AuthenticationFacade authenticationFacade;
    @Autowired
    AdvertiseService advertiseService;
    @Autowired
    private StorageService storageService;

    @Secured({"ROLE_ADMIN"})
    @PatchMapping(value = "/advertises/acceptbyid={id}")
    public ResponseEntity<String> acceptAdvertise(@PathVariable long id) {
        Optional<AdvertiseTo> adv = advertiseService.acceptAdvertiseById(id);
        if (adv.isPresent()) return new ResponseEntity<>("Advertise accepted successfully", HttpStatus.OK);
        else return new ResponseEntity<>("Can not accept advertise", HttpStatus.NOT_ACCEPTABLE);
    }

    @Secured({"ROLE_ADMIN"})
    @PatchMapping(value = "/advertises/rejectbyid={id}")
    public ResponseEntity<String> rejectAdvertise(@PathVariable long id) {
        Optional<AdvertiseTo> adv = advertiseService.rejectAdvertiseById(id);
        if (adv.isPresent()) return new ResponseEntity<>("Advertise rejected successfully", HttpStatus.OK);
        else return new ResponseEntity<>("Can not reject advertise", HttpStatus.NOT_ACCEPTABLE);
    }

    @PostMapping("/advertises")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity<List<String>> addAdvertise(@RequestParam(required = false) MultipartFile pic1,
                                               @Valid AdvertiseTo advertiseTo) {
        List<String> succsessMessage = new ArrayList<>();
        advertiseService.uploadImage(advertiseTo, pic1);
        if (advertiseTo.getImageUrl1() != null) succsessMessage.add("Image successfully uploaded");
        else succsessMessage.add("Image didn't upload");
        AdvertiseTo rslt = advertiseService.publishAdvertiseByUser(advertiseTo);
        succsessMessage.add("advertise successfully sent and waiting for accepting by admin");
        logger.info("user " + advertiseTo.getUserTo().getUsername() + " successfully published advertise " + rslt.getId());
        return new ResponseEntity<>(succsessMessage, HttpStatus.OK);
    }

    @GetMapping("/advertises/onlineData")
    ResponseEntity<OnlineAdvertiseData> getOnlineData(@RequestParam URL url) {
        try {
            return new ResponseEntity<>(onlineService.getData(url), HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>((OnlineAdvertiseData) null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
