package com.mycompany.advertising.controller;

import com.mycompany.advertising.model.to.AdvertiseCategoryTo;
import com.mycompany.advertising.service.api.AdvCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Amir on 6/27/2022.
 */
@RestController
@RequestMapping("/api/advCatecory")
public class AdvCategoryRestController {
    @Autowired
    AdvCategoryService advCategoryService;

    @PostMapping("/")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<String> addCategory(@RequestBody AdvertiseCategoryTo category) {
        category.setId(null);
        advCategoryService.saveCategory(category);
        return new ResponseEntity<>("Advertise category saved successfully", HttpStatus.OK);
    }

    @PatchMapping("/")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<String> updateCategory(@RequestBody AdvertiseCategoryTo category) {
        if (advCategoryService.editCategory(category))
            return new ResponseEntity<>("Advertise category updated successfully", HttpStatus.OK);
        return new ResponseEntity<>("failed to update advertise category", HttpStatus.NOT_ACCEPTABLE);
    }

    @DeleteMapping("/id={id}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        int rows = advCategoryService.deleteByIdCostum(id);
        if (rows > 0) return new ResponseEntity<>("Advertise category deleted successfully", HttpStatus.OK);
        return new ResponseEntity<>("failed to delete Advertise category", HttpStatus.NOT_ACCEPTABLE);
    }
}
