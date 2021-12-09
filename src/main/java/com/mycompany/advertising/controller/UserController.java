package com.mycompany.advertising.controller;

import com.mycompany.advertising.service.api.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Amir on 8/21/2020.
 */
@Controller
@RequestMapping("User")
public class UserController {
    private final static Logger logger = Logger.getLogger(UserController.class);

    @Autowired
    UserService userService;

    /*@GetMapping("/DashboardUser")
    @Secured("ROLE_USER")
    public String userDashboard() {
        return "DashboardUser";
    }

    @GetMapping("/DashboardAdmin")
    @Secured("ROLE_ADMIN")
    public String adminDashboard() {
        return "DashboardAdmin";
    }*/
    @Autowired
    private UserDetailsService userDetailsService;

    @GetMapping("/profile")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public String profile() {
        return "profile";
    }

    @GetMapping("/unaccepted_adverteses")
    @Secured({"ROLE_ADMIN"})
    public String unacceptedAdverteses(Model model, @PathVariable String search, @PathVariable int pagenumber) {
        return "unaccepted_adverteses";
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

    @GetMapping("/Dashboard")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public String dashboard(HttpServletRequest request) {
        if (request.isUserInRole("ROLE_ADMIN")) return "profile2/DashboardAdmin";
        if (request.isUserInRole("ROLE_USER")) return "profile2/DashboardUser";
        return "index";
    }
}
