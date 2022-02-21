package com.mycompany.advertising.controller;

import com.mycompany.advertising.model.to.AdminMessageTo;
import com.mycompany.advertising.model.to.UserCommentTo;
import com.mycompany.advertising.service.api.AdminMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.Optional;

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
    public String adminMessagesGet(Model model, @PathVariable long msgId) {
        Optional<AdminMessageTo> adminMessageTo = adminMessageService.getAdminMessageById(msgId);
        if (!adminMessageTo.isPresent()) {
            return errorfolder + "error-404";
        }
        model.addAttribute("adminMessage", adminMessageTo.get());
        return "adminMessage";
    }

    @GetMapping("/adminMessgesList/{page}")
    public String adminMessgesList(Model model, @PathVariable int page) {
        model.addAttribute("adminMessages", adminMessageService.getPageAdminMessage(page));
        return "allAdminMessages";
    }

    @PostMapping("/adminMessages/{msgId}")
    public String adminMessagesPost(Model model, @PathVariable long msgId,
                                    @RequestParam(required = false, name = "name") String name,
                                    @RequestParam(required = false, name = "message") String message) {
        Optional<AdminMessageTo> adminMessageTo = adminMessageService.getAdminMessageById(msgId);
        if (!adminMessageTo.isPresent()) {
            return errorfolder + "error-404";
        }
        model.addAttribute("adminMessage", adminMessageTo.get());
        if (adminMessageTo.get().getMessageCnt() >= 20) return "adminMessage";
        UserCommentTo userCommentTo = new UserCommentTo();
        userCommentTo.setDate(new Date());
        userCommentTo.setMessage(message);
        userCommentTo.setName(name);
        userCommentTo.setMsgowner(adminMessageTo.get());
        adminMessageService.addUserComment(userCommentTo);
        return "adminMessage";
    }
}
