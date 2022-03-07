package com.mycompany.advertising.controller;

import com.mycompany.advertising.controller.events.OnSigningUpCompleteEvent;
import com.mycompany.advertising.entity.Role;
import com.mycompany.advertising.entity.UserAlreadyExistException;
import com.mycompany.advertising.model.to.AdminMessageTo;
import com.mycompany.advertising.model.to.AdvertiseTo;
import com.mycompany.advertising.model.to.UserTo;
import com.mycompany.advertising.service.api.AdminMessageService;
import com.mycompany.advertising.service.api.AdvertiseService;
import com.mycompany.advertising.service.api.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
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

//import org.springframework.context.MessageSource;

/**
 * Created by Amir on 1/30/2020.
 */
@Controller
public class MainController {
    private final static Logger logger = Logger.getLogger(MainController.class);
    /*@Autowired
    private MessageSource messages;*/
    @Autowired
    AdminMessageService adminMessageService;
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    @Autowired
    private AdvertiseService messageService;
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginGet() {
        return "login";
    }

    @PostMapping("/login")
    public String loginPost(HttpServletRequest request) {//Model model, HttpServletRequest request
        //System.out.println("+++++++++++++++++++++++++" + request.getAttribute("phonenumber"));
        //Object username = request.getAttribute("SPRING_SECURITY_LAST_USERNAME_KEY");
        return "login";
    }

    /*@GetMapping("/login_error")
    public String login_error() {
        return "login";
    }*/

    @PostMapping("/login_error")
    public String loginError(Model model, HttpServletRequest request) {
        String lastphonenumber = (String) request.getParameter("phonenumber");
        if (lastphonenumber != null) {
            if (lastphonenumber.charAt(0) != '0') lastphonenumber = '0' + lastphonenumber;
            model.addAttribute("lastPhoneNumber", lastphonenumber);
            model.addAttribute("phoneNoStatus", userService.getUserStatuseByPhoneNumber(lastphonenumber));
        }
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
                         @RequestParam(required = true, name = "phonenumber") String phonenumber,
                         @RequestParam(required = false, name = "email") String email) {
        UserTo user = new UserTo();
        user.setProfilename(username);
        user.setPassword(password);
        user.setEmail(email);
        if (phonenumber.charAt(0) != '0') phonenumber = '0' + phonenumber;
        user.setUsername(phonenumber);
        user.grantAuthority(Role.ROLE_USER);
        logger.info("signup controller called with " + user.toString());
        try {
            userService.createUser(user);
            eventPublisher.publishEvent(new OnSigningUpCompleteEvent(user));
            logger.info(user.toString() + "is registered successfully");
            model.addAttribute("msg", user.toString() + "is registered successfully");
            //return "redirect:/regitrationConfirm/phonenumber=" + phonenumber;
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
        List<AdvertiseTo> advertiseTos;
        if (search == null | search.equals("")) {
            advertiseTos = messageService.getPageAdvertises(pagenumber).getContent();
        } else {
            advertiseTos = messageService.getPageAdvertises(pagenumber, search).getContent();
        }
        AdminMessageTo adminMessageTo = adminMessageService.getLastMessage();
        if (adminMessageTo != null) model.addAttribute("adminMessage", adminMessageTo);
        /*else {
            adminMessageTo = new AdminMessageTo(new UserTo(), "There is no message yet", new Date(1000));
            adminMessageTo.setId((long) 0);
            model.addAttribute("adminMessage", adminMessageTo);
        }*/
        model.addAttribute("advertises", advertiseTos);
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
