package com.mycompany.advertising.controller;

import com.mycompany.advertising.api.MessageService;
import com.mycompany.advertising.api.StorageService;
import com.mycompany.advertising.model.to.MessageTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by Amir on 11/2/2019.
 */
@Controller
public class MessageController {

    @Autowired
    private StorageService storageService;
    @Autowired
    private MessageService messageService;

    @GetMapping("/showMessage")
    public String showMessage(Model model) {
        List<MessageTo> messages = messageService.getPageMessages(1);
        model.addAttribute("messages", messages);
        return "show_product";
    }

    @GetMapping("/addMessage")
    public String addMessage() {
        return "add_product";
    }

    @PostMapping("/addMessage")//PathVariable
    //@ResponseBody
    public String addMessage(Model model, @RequestParam(required = false, name = "pic") MultipartFile file,
                         @RequestParam(required = false, name = "description") String description,
                         @RequestParam(required = false, name = "tel_link") String telegramlink) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println(filename);
        System.out.println(description);
        System.out.println(telegramlink);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        if (file != null) {
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            System.out.println("add_product with param");
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            storageService.store(file);
            MessageTo messageTo = new MessageTo();
            messageTo.setTelegramlink(telegramlink);
            messageTo.setImagename(filename);
            messageTo.setText(description);
            messageService.addMessage(messageTo);
        } else {
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            System.out.println("add_product no param");
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        }
        return "add_product";
    }
}
