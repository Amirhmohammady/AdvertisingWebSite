package com.mycompany.advertising.controller;

import com.mycompany.advertising.components.api.AuthenticationFacade;
import com.mycompany.advertising.model.to.AdminMessageTo;
import com.mycompany.advertising.model.to.UserTo;
import com.mycompany.advertising.service.api.AdminMessageService;
import com.mycompany.advertising.service.api.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * Created by Amir on 8/21/2020.
 */
@Controller
@RequestMapping("User")
public class UserController {
    private final static Logger logger = Logger.getLogger(UserController.class);

    @Autowired
    AdminMessageService adminMessageService;
    @Autowired
    UserService userService;
    @Autowired
    AuthenticationFacade authenticationFacade;
    @Autowired
    private UserDetailsService userDetailsService;

    @GetMapping("/profile")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public String profile() {
        return "profile";
    }

    @GetMapping("/adminMessage")
    @Secured("ROLE_ADMIN")
    public String adminMessageGet(Model model) {
        model.addAttribute("pfragment01", "adminMessage");
        return "profile2/DashboardAdmin";
    }

    @PostMapping("/adminMessage")
    @Secured("ROLE_ADMIN")
    public String adminMessagePost(Model model, @RequestParam String title, @RequestParam String message) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserTo) {
            AdminMessageTo adminMessage = new AdminMessageTo();
            adminMessage.setDate(LocalDateTime.now());
            adminMessage.setMessage(message);
            adminMessage.setTitle(title);
            adminMessage.setOwner((UserTo) principal);
            adminMessageService.addAdminMessage(adminMessage);
            logger.info("message added: " + message);
        } else logger.warn("message NOT added: " + message);
        model.addAttribute("pfragment01", "adminMessage");
        return "profile2/DashboardAdmin";
    }

    @GetMapping("/userlist")
    @Secured({"ROLE_ADMIN"})
    public String userList() {
        return "user_list";
    }

    @GetMapping("/baneduserlist")
    @Secured({"ROLE_ADMIN"})
    public String banedUserList() {
        return "user_list";
    }

    @GetMapping("/Dashboard/unAcceptedAdvs")
    @Secured({"ROLE_ADMIN"})
    public String unAcceptedAdvsGet() {
        return "profile2/unAcceptedAdvs";
    }

    @GetMapping("/Dashboard")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public String dashboard(Model model, HttpServletRequest request) {
        UserTo userTo = authenticationFacade.getCurrentUser();
        model.addAttribute("userTo", userTo);
        model.addAttribute("pfragment01", "profile");
        //if (request.isUserInRole("ROLE_ADMIN")) return "profile2/DashboardAdmin";
        //if (request.isUserInRole("ROLE_USER")) return "profile2/DashboardUser";
        return "profile2/Dashboard";
    }
}
