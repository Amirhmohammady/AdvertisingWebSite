package com.mycompany.advertising.controller;

import com.mycompany.advertising.components.api.AuthenticationFacade;
import com.mycompany.advertising.model.to.AdvertiseTo;
import com.mycompany.advertising.model.to.UserTo;
import com.mycompany.advertising.model.to.enums.AdvertiseStatus;
import com.mycompany.advertising.service.api.AdvertiseService;
import com.mycompany.advertising.service.api.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Created by Amir on 11/2/2019.
 */
@Controller
public class AdvertiseController {

    @Autowired
    AuthenticationFacade authenticationFacade;
    @Value("${amir.error.folder}")
    String errorfolder;
    @Autowired
    private StorageService storageService;
    @Autowired
    private AdvertiseService advertiseService;
    //private UserService userService;

    @GetMapping("/showAdvertise/id={id}")
    public String showAdvertise(Model model, @PathVariable Long id) {
        Optional<AdvertiseTo> advertise = advertiseService.getAdvertiseById(id);
        if (!advertise.isPresent() || advertise.get().getStatus() != AdvertiseStatus.Accepted) {
            return errorfolder + "error-404";
        }
/*        if (!authenticationFacade.hasRole("ROLE_ADMIN") && advertise.get().getStatus() != AdvertiseStatus.Accepted)
            return errorfolder + "error-403";*/
        model.addAttribute("advertise", advertise.get());
        return "showAdvertise";
    }

    @Secured({"ROLE_ADMIN", "ROLE_OWNER"})
    @GetMapping("/showAdvertiseAdminMode/id={id}")
    public String showAdvertiseAdminMode(Model model, @PathVariable Long id) {
        Optional<AdvertiseTo> advertise = advertiseService.getAdvertiseById(id);
        if (!advertise.isPresent()) {
            return errorfolder + "error-404";
        }
        model.addAttribute("advertise", advertise.get());
        model.addAttribute("pfragment01", "showAdvertiseAdminMode");
        return "showAdvertise";
    }

    @GetMapping("/addAdvertise")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public String addAdvertise() {
        return "add_advertise";
    }

    @PostMapping("/addAdvertise")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public String addAdvertise(Model model, @RequestParam(required = true) MultipartFile pic1,
                               @RequestParam(required = false) String description,
                               @RequestParam(required = false) String tel_link,
                               @RequestParam(required = true) String title,
                               @RequestParam(required = false) MultipartFile pic2) {
        String succsessMessage = new String();
        UserTo userTo = authenticationFacade.getCurrentUser();
        AdvertiseTo advertiseTo = new AdvertiseTo();
        List<String> files;
        if (pic1 != null && !pic1.isEmpty()) {
            try {
                files = storageService.storeImage(pic1);
                succsessMessage += "Successfully uploaded 1st pic";
                advertiseTo.setImageUrl1(files.get(0));
                advertiseTo.setSmallImageUrl1(files.get(1));
            } catch (Exception e) {
                succsessMessage += "Error uploading 1nd pic:" + e.getMessage();
                e.printStackTrace();
            }
        }
        if (pic2 != null && !pic2.isEmpty()) {
            try {
                files = storageService.storeImage(pic2);
                succsessMessage += "\nSuccessfully uploaded 2nd pic";
                advertiseTo.setImageUrl2(files.get(0));
                advertiseTo.setSmallImageUrl2(files.get(1));
            } catch (Exception e) {
                succsessMessage += "\nError uploading 2nd pic:" + e.getMessage();
                e.printStackTrace();
            }
        }
        advertiseTo.setWebSiteLink(tel_link);
        advertiseTo.setStatus(AdvertiseStatus.Not_Accepted);
        advertiseTo.setText(description);
        advertiseTo.setTitle(title);
        advertiseTo.setUserTo(userTo);
        advertiseTo.setStartdate(LocalDateTime.now());
        advertiseService.saveAdvertise(advertiseTo);
        succsessMessage += "\nSuccessfully added advertise";
        model.addAttribute("succsessmessage", succsessMessage);
        return "add_advertise";
    }

    @GetMapping(value = "/editAdvertise/id={id}")//, method = {RequestMethod.GET, RequestMethod.POST})
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    //@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public String editAdvertiseGet(Model model, @PathVariable Long id) {
        Optional<AdvertiseTo> advertiseoptl = advertiseService.getAdvertiseById(id);
        if (!advertiseoptl.isPresent()) return errorfolder + "error-404";
        UserTo userTo = authenticationFacade.getCurrentUser();
        if (userTo == null || userTo.getId() != advertiseoptl.get().getUserTo().getId()) return errorfolder + "error-403";
        model.addAttribute("advertise", advertiseoptl.get());
        return "editAdvertise";
    }

    @PostMapping(value = "/editAdvertise/id={id}")//, method = {RequestMethod.GET, RequestMethod.POST})
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    //@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public String editAdvertisePost(Model model, @PathVariable Long id,
                                    @RequestParam(required = false) MultipartFile pic1,
                                    @RequestParam(required = false) String description,
                                    @RequestParam(required = false) String tel_link,
                                    @RequestParam(required = true) String title,
                                    @RequestParam(required = false) MultipartFile pic2) {
        Optional<AdvertiseTo> advertiseoptl = advertiseService.getAdvertiseById(id);
        if (!advertiseoptl.isPresent()) return errorfolder + "error-404";
        UserTo userTo = authenticationFacade.getCurrentUser();
        if (userTo == null || userTo.getId() != advertiseoptl.get().getUserTo().getId()) return errorfolder + "error-403";
        AdvertiseTo advertise = advertiseoptl.get();
        //==========
        String succsessMessage = new String();
        List<String> files;
        if (pic1 != null && !pic1.isEmpty()) {
            try {
                files = storageService.storeImage(pic1);
                succsessMessage += "Successfully uploaded 1st pic";
                advertise.setImageUrl1(files.get(0));
                advertise.setSmallImageUrl1(files.get(1));
            } catch (Exception e) {
                succsessMessage += "Error uploading 1nd pic:" + e.getMessage();
                e.printStackTrace();
            }
        }
        if (pic2 != null && !pic2.isEmpty()) {
            try {
                files = storageService.storeImage(pic2);
                succsessMessage += "\nSuccessfully uploaded 2nd pic";
                advertise.setImageUrl2(files.get(0));
                advertise.setSmallImageUrl2(files.get(1));
            } catch (Exception e) {
                succsessMessage += "\nError uploading 2nd pic:" + e.getMessage();
                e.printStackTrace();
            }
        }
        advertise.setWebSiteLink(tel_link);
        advertise.setText(description);
        advertise.setTitle(title);
        advertiseService.saveAdvertise(advertise);
        succsessMessage += "\nSuccessfully updated advertise";
        model.addAttribute("succsessmessage", succsessMessage);
        model.addAttribute("advertise", advertise);
        return "editAdvertise";
    }
}
