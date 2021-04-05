package com.mycompany.advertising.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by Amir on 8/14/2020.
 */
@Controller
public class Dashboard {
    @GetMapping("/UserDashboard")
    @Secured("ROLE_USER")
    public String userDashboard(){
        return "DashboardUser";
    }
    @GetMapping("/AdminDashboard")
    @Secured("ROLE_ADMIN")
    public String adminDashboard(){
        return "DashboardAdmin";
    }
}
