package com.mycompany.advertising.controller;

import com.mycompany.advertising.api.MessageService;
import com.mycompany.advertising.api.UserService;
import com.mycompany.advertising.entity.Role;
import com.mycompany.advertising.entity.UserEmailIsAvailableException;
import com.mycompany.advertising.model.to.MessageTo;
import com.mycompany.advertising.model.to.UserTo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Amir on 1/30/2020.
 */
@Controller
public class MainController {
    private final static Logger logger = Logger.getLogger(MainController.class);

    @Autowired
    ApplicationEventPublisher eventPublisher;
    @Autowired
    MessageService messageService;
    @Autowired
    private UserService userService;

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
                         @RequestParam(required = false, name = "confirm_password") String confirm_password,
                         @RequestParam(required = true, name = "email") String email) {
        UserTo user = new UserTo();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.grantAuthority(Role.ROLE_USER);
        logger.info("signup controller called with " + user.toString());
        try {
            userService.svaeUser(user);
            logger.info(user.toString() + "is saved successfully");
        } catch (UserEmailIsAvailableException uia) {
            logger.info("can not save " + user.toString() + " the user name is exist");
        }
        return "signup";
    }

    @GetMapping("/")
    public String defualt() {
        return "redirect:/index/search=/page=1";
    }

    @GetMapping("/index")
    public String index(Model model) {
        return "redirect:/index/search=/page=1";
    }

    @GetMapping(value = "/index/search={search}/page={pagenumber}")//, produces = "text/plain;charset=UTF-8")
    public String mainIndex(Model model, @PathVariable String search, @PathVariable int pagenumber,
                            @RequestParam(required = false, name = "search") String search02,
                            @RequestParam(required = false, name = "lan") String language,
                            HttpServletRequest request, HttpServletResponse response) {

        List<MessageTo> messageTos;
        boolean hasparam = false;
        if (language != null) {
            hasparam = true;
            LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
            localeResolver.setLocale(request, response, StringUtils.parseLocaleString(language));
        }
        if (search02 != null) {
            hasparam = true;
            pagenumber = 1;
            search = search02;
        }
        if (hasparam) return "redirect:/index/search=" + search + "/page=" + pagenumber;
        if (pagenumber < 1) pagenumber = 1;
        if (search == null | search.equals("")) {
            messageTos = messageService.getPageMessages(pagenumber).getContent();
        } else {
            messageTos = messageService.getPageMessages(pagenumber, search).getContent();
        }
        model.addAttribute("advertises", messageTos);
        return "index";
    }

    /*@GetMapping("/index/page={pagenumber}")
    public String indexByPage(Model model, @PathVariable int pagenumber) {
        System.out.println("222222222222222222!!!!!!!!!!!!!!!");
        model.addAttribute("advertises", messageService.getPageMessages(pagenumber));
        return "index";
    }*/

}
