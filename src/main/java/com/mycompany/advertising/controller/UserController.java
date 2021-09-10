package com.mycompany.advertising.controller;

import com.mycompany.advertising.api.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Created by Amir on 8/21/2020.
 */
@Controller
public class UserController {
    private final static Logger logger = Logger.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @GetMapping("/DashboardUser")
    @Secured("ROLE_USER")
    public String userDashboard() {
        return "DashboardUser";
    }

    @GetMapping("/DashboardAdmin")
    @Secured("ROLE_ADMIN")
    public String adminDashboard() {
        return "DashboardAdmin";
    }

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

}
