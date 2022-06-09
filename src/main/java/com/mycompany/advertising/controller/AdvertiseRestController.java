package com.mycompany.advertising.controller;

import com.mycompany.advertising.model.to.AdvertiseTo;
import com.mycompany.advertising.service.api.AdvertiseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Created by Amir on 6/5/2022.
 */
@RestController
@RequestMapping("/api/advertises")
public class AdvertiseRestController {
    @Autowired
    AdvertiseService advertiseService;

    @Secured({"ROLE_ADMIN"})
    @PatchMapping(value = "/acceptbyid={id}")
    public ResponseEntity<String> acceptAdvertise(@PathVariable long id){
        Optional<AdvertiseTo> adv = advertiseService.acceptAdvertiseById(id);
        if (adv.isPresent()) return new ResponseEntity<>("Advertise accepted successfully", HttpStatus.OK);
        else return new ResponseEntity<>("Can not accept advertise", HttpStatus.NOT_ACCEPTABLE);
    }

    @Secured({"ROLE_ADMIN"})
    @PatchMapping(value = "/rejectbyid={id}")
    public ResponseEntity<String> rejectAdvertise(@PathVariable long id){
        Optional<AdvertiseTo> adv = advertiseService.rejectAdvertiseById(id);
        if (adv.isPresent()) return new ResponseEntity<>("Advertise rejected successfully", HttpStatus.OK);
        else return new ResponseEntity<>("Can not reject advertise", HttpStatus.NOT_ACCEPTABLE);
    }
}
