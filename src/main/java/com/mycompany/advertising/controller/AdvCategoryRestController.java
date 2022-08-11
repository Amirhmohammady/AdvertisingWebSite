package com.mycompany.advertising.controller;

import com.mycompany.advertising.model.to.AdvertiseCategoryTo;
import com.mycompany.advertising.service.Dto.CategoryIdPair;
import com.mycompany.advertising.service.api.AdvCategoryService;
import com.mycompany.advertising.service.language.LngManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Created by Amir on 6/27/2022.
 */
@RestController
@RequestMapping("/api")
public class AdvCategoryRestController {
    @Autowired
    AdvCategoryService advCategoryService;

    @PostMapping("/advCategory")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<AdvertiseCategoryTo> addCategory(@RequestBody AdvertiseCategoryTo category) {
        AdvertiseCategoryTo rslt = advCategoryService.addCategory(category);
        if (rslt != null) return new ResponseEntity<>(rslt, HttpStatus.OK);
        else return new ResponseEntity<>((AdvertiseCategoryTo) null, HttpStatus.NOT_ACCEPTABLE);
    }

    /*@PutMapping
    @PreAuthorize("#post.getCreator() == authentication.getName()")
    public void update(@RequestBody Post post, Authentication authentication) {
        service.updatePost(post);
    }

    @Autowired
    private CreatorCheck creatorCheck;

    @PutMapping
    @PreAuthorize("@creatorChecker.check(#post,authentication)")
    public void update(@RequestBody Post post, Authentication authentication) {
        service.updatePost(post);
    }*/
    @PatchMapping("/advCategory")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<AdvertiseCategoryTo> updateCategory(@RequestBody AdvertiseCategoryTo category) {
        AdvertiseCategoryTo advCategoryTo = advCategoryService.editCategory(category);
        if (advCategoryTo != null)
            return new ResponseEntity<>(advCategoryTo, HttpStatus.OK);
        return new ResponseEntity<>((AdvertiseCategoryTo) null, HttpStatus.NOT_ACCEPTABLE);
    }

    @DeleteMapping("/advCategory")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<String> deleteCategory(@RequestParam(required = true) Long id) {
        int rows = advCategoryService.deleteByIdCostum(id);
        if (rows > 0) return new ResponseEntity<>("Advertise category deleted successfully", HttpStatus.OK);
        return new ResponseEntity<>("failed to delete Advertise category", HttpStatus.NOT_ACCEPTABLE);
    }

    @GetMapping("/advCategories")
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public ResponseEntity<List<CategoryIdPair>> getCategoriesByParentId(@RequestParam(required = true) Long parentId,
                                                                        @RequestParam(required = true) String lan) {
        return new ResponseEntity<>(advCategoryService.getChildsByLanguageAndId(LngManager.whatLanguage(lan), parentId), HttpStatus.OK);
    }

    @GetMapping("/advCategory")
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public ResponseEntity<AdvertiseCategoryTo> getCategoryById(@RequestParam(required = true) Long id) {
        Optional<AdvertiseCategoryTo> category = advCategoryService.getCategoryById(id);
        if (category.isPresent())
            return new ResponseEntity<>(category.get(), HttpStatus.OK);
        return new ResponseEntity<>((AdvertiseCategoryTo) null, HttpStatus.NOT_ACCEPTABLE);
    }
}
