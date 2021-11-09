package com.mycompany.advertising.controller;

import com.mycompany.advertising.controller.events.OnSigningUpCompleteEvent;
import com.mycompany.advertising.entity.Role;
import com.mycompany.advertising.entity.UserAlreadyExistException;
import com.mycompany.advertising.model.to.AvertiseTo;
import com.mycompany.advertising.model.to.UserTo;
import com.mycompany.advertising.service.api.AvertiseService;
import com.mycompany.advertising.service.api.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
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
    private ApplicationEventPublisher eventPublisher;
    @Autowired
    private AvertiseService messageService;
    @Autowired
    private UserService userService;
    @Autowired
    private MessageSource messages;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signUp() {
        return "signup";
    }

    @PostMapping("/signup")
    @Validated
    public String signUp(Model model, @RequestParam(required = true, name = "username") String username,
                         @RequestParam(required = true, name = "password") String password,
                         @RequestParam(required = true, name = "confirm_password") String confirm_password,
                         @RequestParam(required = true, name = "phonenumber") String phonenumber,
                         @RequestParam(required = false, name = "email") String email) {
        UserTo user = new UserTo();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setPhonenumber(phonenumber);
        user.grantAuthority(Role.ROLE_USER);
        logger.info("signup controller called with " + user.toString());
        try {
            userService.createUser(user);
            eventPublisher.publishEvent(new OnSigningUpCompleteEvent(user));
            logger.info(user.toString() + "is registered successfully");
        } catch (UserAlreadyExistException uia) {
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

        List<AvertiseTo> avertiseTos;
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
            avertiseTos = messageService.getPageAvertises(pagenumber).getContent();
        } else {
            avertiseTos = messageService.getPageAvertises(pagenumber, search).getContent();
        }
        model.addAttribute("advertises", avertiseTos);
        return "index";
    }

    /*@GetMapping("/index/page={pagenumber}")
    public String indexByPage(Model model, @PathVariable int pagenumber) {
        System.out.println("222222222222222222!!!!!!!!!!!!!!!");
        model.addAttribute("advertises", messageService.getPageMessages(pagenumber));
        return "index";
    }*/

    @GetMapping("/regitrationConfirm/phonenumber={phonenumber}")
    public String confirmRegistration(Model model, @PathVariable String phonenumber) {
        model.addAttribute("phonenumber", phonenumber);
        return "confirmRegistration";
    }
}
