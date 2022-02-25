package com.mycompany.advertising.controller;

import com.mycompany.advertising.components.api.AuthenticationFacade;
import com.mycompany.advertising.entity.AvertiseStatus;
import com.mycompany.advertising.model.to.AvertiseTo;
import com.mycompany.advertising.model.to.UserTo;
import com.mycompany.advertising.service.api.AvertiseService;
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
public class AvertiseController {

    @Autowired
    AuthenticationFacade authenticationFacade;
    @Autowired
    private StorageService storageService;
    @Autowired
    private AvertiseService avertiseService;
    @Value("${amir.error.folder}")
    String errorfolder;
    //private UserService userService;

    @GetMapping("/showAvertise/id={id}")
    public String showAvertise(Model model, @PathVariable long id) {
        Optional<AvertiseTo> avertise = avertiseService.getAvertiseById(id);
        if (!avertise.isPresent()) {
            return errorfolder + "error-404";
        }
        model.addAttribute("avertises", avertise.get());
        return "showAdvertise";
    }

    @GetMapping("/addAvertise")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public String addAvertise() {
        return "add_advertise";
    }

    @PostMapping("/addAvertise")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public String addAvertise(Model model, @RequestParam(required = false, name = "pic") MultipartFile file,
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
            AvertiseTo avertiseTo = new AvertiseTo();
            avertiseTo.setWebSiteLink(telegramlink);
            avertiseTo.setImageUrl(files.get(0));
            avertiseTo.setStatus(AvertiseStatus.Not_Accepted);
            avertiseTo.setSmallImageUrl(files.get(1));
            avertiseTo.setText(description);
            avertiseTo.setUserTo(userTo);
            avertiseService.addAvertise(avertiseTo);
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
        Optional<AvertiseTo> avertiseoptl = avertiseService.getAvertiseById(id);
        if (avertiseoptl.isPresent()) {
            UserTo userTo = authenticationFacade.getUserToDetails();
            boolean isAuthenticated = false;
            System.out.println(avertiseoptl.get().getUserTo() + "-------------------------");
            System.out.println(userTo.getId() + "-------------------------");
            System.out.println("testtttttttttttttttttttttttt");
            if (authenticationFacade.hasRole("ROLE_ADMIN")) {
                isAuthenticated = true;
                System.out.println("admin-------------------------");
            } else if (userTo != null && userTo.getId() == avertiseoptl.get().getUserTo().getId()) {
                isAuthenticated = true;
                System.out.println("not admin-------------------------");
            }
            if (isAuthenticated) avertiseService.deleteAvertiseById(id);
        }
        return "redirect:" + previouslink;
    }

    @GetMapping("/editAvertise/id={id}")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    //@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public String edit(Model model, @PathVariable Long id,
                       @RequestParam(required = true, name = "pLink") String previouslink) {
        Optional<AvertiseTo> avertiseoptl = avertiseService.getAvertiseById(id);
        UserTo userTo = authenticationFacade.getUserToDetails();
        if (avertiseoptl.isPresent())
            if (userTo != null)
                if (userTo.getId() == avertiseoptl.get().getId()) ;

        return "redirect:" + previouslink;
    }
}
