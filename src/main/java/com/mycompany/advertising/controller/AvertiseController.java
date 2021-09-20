package com.mycompany.advertising.controller;

import com.mycompany.advertising.service.api.AuthenticationFacade;
import com.mycompany.advertising.service.api.AvertiseService;
import com.mycompany.advertising.service.api.StorageService;
import com.mycompany.advertising.entity.AvertiseStatus;
import com.mycompany.advertising.model.to.AvertiseTo;
import com.mycompany.advertising.model.to.UserTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

/**
 * Created by Amir on 11/2/2019.
 */
@Controller
public class AvertiseController {

    @Autowired
    private StorageService storageService;
    @Autowired
    private AvertiseService avertiseService;
    @Autowired
    AuthenticationFacade authenticationFacade;
    //private UserService userService;

    @GetMapping("/showAvertise")
    public String showAvertise(Model model) {
        List<AvertiseTo> avertises = avertiseService.getPageAvertises(1).getContent();
        model.addAttribute("avertises", avertises);
        return "show_advertise";
    }

    @GetMapping("/addAvertise")
    public String addAvertise() {
        return "add_advertise";
    }

    @PostMapping("/addAvertise")
    //@ResponseBody
    public String addAvertise(Model model, @RequestParam(required = false, name = "pic") MultipartFile file,
                             @RequestParam(required = false, name = "description") String description,
                             @RequestParam(required = false, name = "tel_link") String telegramlink) {
        //String filename = StringUtils.cleanPath(file.getOriginalFilename());
        UserTo userTo = authenticationFacade.getUserToDetails();
        if (file != null) {
            List<String> files = storageService.storeImage(file);
            AvertiseTo avertiseTo = new AvertiseTo();
            avertiseTo.setTelegramlink(telegramlink);
            avertiseTo.setImagename(files.get(0));
            avertiseTo.setStatus(AvertiseStatus.Not_Accepted);
            avertiseTo.setSmallimagename(files.get(1));
            avertiseTo.setText(description);
            avertiseTo.setOwnerid(userTo.getId());
            avertiseService.addAvertise(avertiseTo);
            model.addAttribute("succsessmessage","Successfully added your advertise");
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
            System.out.println(avertiseoptl.get().getOwnerid() + "-------------------------");
            System.out.println(userTo.getId() + "-------------------------");
            System.out.println("testtttttttttttttttttttttttt");
            if (authenticationFacade.hasRole("ROLE_ADMIN")) {
                isAuthenticated = true;
                System.out.println("admin-------------------------");
            } else if (userTo != null & userTo.getId() == avertiseoptl.get().getOwnerid()) {
                isAuthenticated = true;
                System.out.println("not admin-------------------------");
            }
            if (isAuthenticated) avertiseService.deleteAvertiseById(id);
        }
        return "redirect:" + previouslink;
    }

    @GetMapping("/edit/id={id}")
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
