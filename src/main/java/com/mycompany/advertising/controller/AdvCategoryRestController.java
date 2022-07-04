package com.mycompany.advertising.controller;

import com.mycompany.advertising.model.to.AdvertiseCategoryTo;
import com.mycompany.advertising.model.to.enums.Language;
import com.mycompany.advertising.service.Dto.CategoryIdPair;
import com.mycompany.advertising.service.api.AdvCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Amir on 6/27/2022.
 */
@RestController
@RequestMapping("/api")
public class AdvCategoryRestController {
    @Autowired
    AdvCategoryService advCategoryService;

    @PostMapping("/advCatecory")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<String> addCategory(@RequestBody AdvertiseCategoryTo category) {
        category.setId(null);
        System.out.println(category);
        advCategoryService.saveCategory(category);
        return new ResponseEntity<>("Advertise category saved successfully", HttpStatus.OK);
    }

    @PatchMapping("/advCatecory")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<String> updateCategory(@RequestBody AdvertiseCategoryTo category) {
        if (advCategoryService.editCategory(category))
            return new ResponseEntity<>("Advertise category updated successfully", HttpStatus.OK);
        return new ResponseEntity<>("failed to update advertise category", HttpStatus.NOT_ACCEPTABLE);
    }

    @DeleteMapping("/advCatecory")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<String> deleteCategory(@RequestParam(required = true) Long id) {
        int rows = advCategoryService.deleteByIdCostum(id);
        if (rows > 0) return new ResponseEntity<>("Advertise category deleted successfully", HttpStatus.OK);
        return new ResponseEntity<>("failed to delete Advertise category", HttpStatus.NOT_ACCEPTABLE);
    }

    @GetMapping("/advCatecories")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<List<CategoryIdPair>> getCategoriesByParentId(@RequestParam(required = true) Long id,
                                                                     @RequestParam(required = true) String lan) {
        return new ResponseEntity<>(advCategoryService.getChildsByLanguageAndId(Language.valueOf(lan), id), HttpStatus.OK);
    }

    @GetMapping("/advCatecory")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Map<String, String>> getCategoryById(@RequestParam(required = true) Long id) {
        Optional<AdvertiseCategoryTo> category = advCategoryService.getCategoryById(id);
        if (category.isPresent())
            return new ResponseEntity<>(category.get().getLanguagesAsMap(), HttpStatus.OK);
        return new ResponseEntity<>(new HashMap<>(), HttpStatus.NOT_ACCEPTABLE);
    }
}
