package com.mycompany.advertising.controller;

import com.mycompany.advertising.api.MessageService;
import com.mycompany.advertising.api.UserService;
import com.mycompany.advertising.entity.Role;
import com.mycompany.advertising.model.to.MessageTo;
import com.mycompany.advertising.model.to.UserTo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by Amir on 1/30/2020.
 */
@Controller
public class MainController {
    private final static Logger logger = Logger.getLogger(MainController.class);

    @Autowired
    private UserService userService;

    @Autowired
    MessageService messageService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signUp() {
        return "signup";
    }

    @PostMapping("/signup")
    public String signUp(Model model, @RequestParam(required = false, name = "username") String username,
                         @RequestParam(required = false, name = "password") String password,
                         @RequestParam(required = false, name = "confirm_password") String confirm_password) {
        UserTo user = new UserTo();
        user.setUsername(username);
        user.setPassword(password);
        user.grantAuthority(Role.ROLE_USER);
        userService.svaeUser(user);

        return "signup";
    }

    @GetMapping("/")
    public String defualt() {
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String index(Model model) {
        List<MessageTo> messageTos = messageService.getPageMessages(1);
        model.addAttribute("advertises", messageTos);
        System.out.println("Amir index function!!!!!!!!!!!!!!!");
        logger.fatal("Amir index function");
        logger.fatal(messageTos);
        return "index";
    }

    @GetMapping("/index/page={pagenumber}")
    public String indexByPage(Model model, @PathVariable int pagenumber) {
        System.out.println("222222222222222222!!!!!!!!!!!!!!!");
        model.addAttribute("advertises", messageService.getPageMessages(pagenumber));
        return "index";
    }
}
