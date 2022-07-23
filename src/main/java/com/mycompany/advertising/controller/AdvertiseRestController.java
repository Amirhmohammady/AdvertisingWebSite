package com.mycompany.advertising.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.advertising.components.api.AuthenticationFacade;
import com.mycompany.advertising.model.to.AdvertiseCategoryTo;
import com.mycompany.advertising.model.to.AdvertiseTo;
import com.mycompany.advertising.model.to.UserTo;
import com.mycompany.advertising.model.to.enums.AdvertiseStatus;
import com.mycompany.advertising.service.api.AdvCategoryService;
import com.mycompany.advertising.service.api.AdvertiseService;
import com.mycompany.advertising.service.api.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Amir on 6/5/2022.
 */
@RestController
@RequestMapping("/api/advertises")
public class AdvertiseRestController {
    @Autowired
    AdvCategoryService advCategoryService;
    @Autowired
    AuthenticationFacade authenticationFacade;
    @Autowired
    AdvertiseService advertiseService;
    @Autowired
    private StorageService storageService;

    @Secured({"ROLE_ADMIN"})
    @PatchMapping(value = "/acceptbyid={id}")
    public ResponseEntity<String> acceptAdvertise(@PathVariable long id) {
        Optional<AdvertiseTo> adv = advertiseService.acceptAdvertiseById(id);
        if (adv.isPresent()) return new ResponseEntity<>("Advertise accepted successfully", HttpStatus.OK);
        else return new ResponseEntity<>("Can not accept advertise", HttpStatus.NOT_ACCEPTABLE);
    }

    @Secured({"ROLE_ADMIN"})
    @PatchMapping(value = "/rejectbyid={id}")
    public ResponseEntity<String> rejectAdvertise(@PathVariable long id) {
        Optional<AdvertiseTo> adv = advertiseService.rejectAdvertiseById(id);
        if (adv.isPresent()) return new ResponseEntity<>("Advertise rejected successfully", HttpStatus.OK);
        else return new ResponseEntity<>("Can not reject advertise", HttpStatus.NOT_ACCEPTABLE);
    }

    @PostMapping("/")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity<List<String>> addAdvertise(Model model, @RequestParam(required = true) MultipartFile pic1,
                                                     @RequestParam(required = true) String advContent
                                                     //,@RequestBody AdvertiseTo advertiseTo
                               /*@RequestParam(required = false) String description,
                               @RequestParam(required = false) String ext_link,
                               @RequestParam(required = true) String title*/) {
        List<String> succsessMessage = new ArrayList<>();
        try {
            AdvertiseTo advertiseTo = new ObjectMapper().readValue(advContent, AdvertiseTo.class);
            System.out.println(advertiseTo);
            UserTo userTo = authenticationFacade.getCurrentUser();
            List<String> files;
            if (pic1 != null && !pic1.isEmpty()) {
                try {
                    files = storageService.storeImage(pic1);
                    succsessMessage.add("Successfully uploaded 1st pic");
                    advertiseTo.setImageUrl1(files.get(0));
                    advertiseTo.setSmallImageUrl1(files.get(1));
                } catch (Exception e) {
                    succsessMessage.add("Error uploading 1nd pic:" + e.getMessage());
                    e.printStackTrace();
                }
            }
            //advertiseTo.setWebSiteLink(ext_link);
            advertiseTo.setStatus(AdvertiseStatus.Not_Accepted);
            advertiseTo.setUserTo(userTo);
            for (int i = 0; i < advertiseTo.getCategories().size(); i++) {
                Optional<AdvertiseCategoryTo> tadv = advCategoryService.getCategoryById(advertiseTo.getCategories().get(i).getId());
                if (tadv.isPresent()) advertiseTo.getCategories().set(i, tadv.get());
                else {
                    advertiseTo.getCategories().remove(i);
                    i++;
                }
            }
            System.out.println(advertiseTo);
            advertiseTo.setStartdate(LocalDateTime.now());
            advertiseService.saveAdvertise(advertiseTo);
            succsessMessage.add("Successfully added advertise");
            return new ResponseEntity<List<String>>(succsessMessage, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<List<String>>(succsessMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
