package com.mycompany.advertising.controller;

import com.mycompany.advertising.model.to.AdminMessageTo;
import com.mycompany.advertising.service.api.AdminMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Created by Amir on 2/11/2022.
 */
@Controller
public class AdminMessageController {
    @Autowired
    AdminMessageService adminMessageService;
    @Value("${amir.error.folder}")
    String errorfolder;

    @GetMapping("/adminMessages/{msgId}")
    public String adminMessages(Model model, @PathVariable long msgId) {
        AdminMessageTo adminMessageTo = adminMessageService.getAdminMessageById(msgId);
        adminMessageTo.getComments().size();
        if (adminMessageTo == null){
            return errorfolder + "error-404";
        }
        else model.addAttribute("adminMessage", adminMessageTo);
        return "adminMessage";
    }
}
