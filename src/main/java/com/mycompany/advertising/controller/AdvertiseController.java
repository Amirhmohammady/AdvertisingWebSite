package com.mycompany.advertising.controller;

import com.mycompany.advertising.components.api.AuthenticationFacade;
import com.mycompany.advertising.entity.AdvertiseStatus;
import com.mycompany.advertising.model.to.AdvertiseTo;
import com.mycompany.advertising.model.to.UserTo;
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

import java.io.IOException;
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
        if (!advertise.isPresent()) {
            return errorfolder + "error-404";
        }
        model.addAttribute("advertise", advertise.get());
        return "showAdvertise";
    }

    @GetMapping("/addAdvertise")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public String addAdvertise() {
        return "add_advertise";
    }

    @PostMapping("/addAdvertise")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public String addAdvertise(Model model, @RequestParam(required = true, name = "pic") MultipartFile file,
                               @RequestParam(required = false, name = "description") String description,
                               @RequestParam(required = false, name = "tel_link") String telegramlink) {
        //String filename = StringUtils.cleanPath(file.getOriginalFilename());
        UserTo userTo = authenticationFacade.getUserToDetails();
        if (file != null) {
            List<String> files = null;
            try {
                files = storageService.storeImage(file);
                model.addAttribute("succsessmessage", "Successfully added your advertise");
            } catch (IOException e) {
                model.addAttribute("succsessmessage", e.getMessage());
                e.printStackTrace();
            }
            AdvertiseTo advertiseTo = new AdvertiseTo();
            advertiseTo.setWebSiteLink(telegramlink);
            advertiseTo.setImageUrl(files.get(0));
            advertiseTo.setStatus(AdvertiseStatus.Not_Accepted);
            advertiseTo.setSmallImageUrl(files.get(1));
            advertiseTo.setText(description);
            advertiseTo.setUserTo(userTo);
            advertiseService.addAdvertise(advertiseTo);
        } else {
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            System.out.println("add_advertise no param");
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        }
        return "add_advertise";
    }

    @GetMapping("/delete/id={id}")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    //@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public String delete(Model model, @PathVariable Long id, //Principal principal,
                         @RequestParam(required = true, name = "pLink") String previouslink) {
        Optional<AdvertiseTo> advertiseoptl = advertiseService.getAdvertiseById(id);
        if (advertiseoptl.isPresent()) {
            UserTo userTo = authenticationFacade.getUserToDetails();
            boolean isAuthenticated = false;
            System.out.println(advertiseoptl.get().getUserTo() + "-------------------------");
            System.out.println(userTo.getId() + "-------------------------");
            System.out.println("testtttttttttttttttttttttttt");
            if (authenticationFacade.hasRole("ROLE_ADMIN")) {
                isAuthenticated = true;
                System.out.println("admin-------------------------");
            } else if (userTo != null && userTo.getId() == advertiseoptl.get().getUserTo().getId()) {
                isAuthenticated = true;
                System.out.println("not admin-------------------------");
            }
            if (isAuthenticated) advertiseService.deleteAdvertiseById(id);
        }
        return "redirect:" + previouslink;
    }

    @GetMapping("/editAdvertise/id={id}")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    //@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public String edit(Model model, @PathVariable Long id,
                       @RequestParam(required = true, name = "pLink") String previouslink) {
        Optional<AdvertiseTo> advertiseoptl = advertiseService.getAdvertiseById(id);
        UserTo userTo = authenticationFacade.getUserToDetails();
        if (advertiseoptl.isPresent())
            if (userTo != null)
                if (userTo.getId() == advertiseoptl.get().getId()) ;

        return "redirect:" + previouslink;
    }
}
