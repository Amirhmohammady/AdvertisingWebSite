package com.mycompany.advertising.controller;

import com.mycompany.advertising.controller.events.OnSigningUpCompleteEvent;
import com.mycompany.advertising.controller.utils.PageCalculator;
import com.mycompany.advertising.controller.utils.annotations.LockApiByVariable;
import com.mycompany.advertising.controller.utils.annotations.LockerWaitType;
import com.mycompany.advertising.controller.utils.annotations.ReturnType;
import com.mycompany.advertising.controller.utils.annotations.TimeLimiter;
import com.mycompany.advertising.entity.UserAlreadyExistException;
import com.mycompany.advertising.model.to.AdvertiseTo;
import com.mycompany.advertising.model.to.UserTo;
import com.mycompany.advertising.model.to.enums.Role;
import com.mycompany.advertising.service.api.*;
import com.mycompany.advertising.service.util.OnlineAdvertiseData;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.stream.Collectors;
//import org.springframework.context.MessageSource;

/**
 * Created by Amir on 1/30/2020.
 */
@Controller
public class MainController {
    private final static Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());
    /*@Autowired
    private MessageSource messages;*/
    @Autowired
    private LockerApiService lockerApiService;
    @Autowired
    private AdminMessageService adminMessageService;
    @Value("${amir.error.folder}")
    private String errorfolder;
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    @Autowired
    private AdvertiseService messageService;
    @Autowired
    OnlineAdvertiseDataService onlineService;
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginGet() {
        return "login";
    }

    @PostMapping("/login")
    @LockApiByVariable(timeLimiter = @TimeLimiter(maxRequest = 2, inSeconds = 16), variableName = "username", waitOrErr = LockerWaitType.ERROR, returnType = ReturnType.HTML)
    public String loginPost(Model model, HttpServletRequest request, String username, String password) {//Model model, HttpServletRequest request
        try {
            request.login(username, password);
            logger.info("user " + username + " successfully logged in");
            lockerApiService.removeLastLockByVariable(new Object(){}.getClass().getEnclosingMethod(), username);
            return "redirect:/";
        } catch (ServletException e) {
            model.addAttribute("lastPhoneNumber", username);
            model.addAttribute("phoneNoStatus", userService.getUserStatuseByPhoneNumber(username));
            logger.info("user " + username + " failed to login");
            return "login";
        }
    }

    /*@GetMapping("/login_error")
    public String login_error() {
        return "login";
    }*/

    /*@PostMapping("/login_error")
    public String loginError(Model model, HttpServletRequest request) {
        System.out.println("loginError++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        String lastphonenumber = (String) request.getParameter("username");
        if (lastphonenumber != null) {
            if (lastphonenumber.charAt(0) != '0') lastphonenumber = '0' + lastphonenumber;
            model.addAttribute("lastPhoneNumber", lastphonenumber);
            model.addAttribute("phoneNoStatus", userService.getUserStatuseByPhoneNumber(lastphonenumber));
        }
        return "login";
    }*/

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

    @GetMapping(value = {"/index"})
    public String indexSearchGet(@RequestParam(required = false, name = "search") String search) {
        if (search == null) search = "";
        try {
            search = URLEncoder.encode(search, "UTF-8");
            logger.info("encoded search is: " + search);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        logger.info("index page request search for " + search);
        return "redirect:/index/search=" + search + "/page=1";
    }

    @GetMapping(value = {"/", "/index/search={search}/page={pagenumber}"})
//, produces = "text/plain;charset=UTF-8")
    public String indexGet(Model model, @PathVariable(required = false) String search,
                           @PathVariable(required = false) Integer pagenumber) {
        logger.info("index page request for page number " + pagenumber + " search " + search);
        if (pagenumber == null || pagenumber < 1) pagenumber = 1;
        Page<AdvertiseTo> advertiseTos;
        if (search == null || search.equals("")) {
            advertiseTos = messageService.getPageAcceptedAdvertises(pagenumber);
        } else {
            advertiseTos = messageService.getPageAcceptedAdvertises(pagenumber, search);
        }
        List<OnlineAdvertiseData> onlineAdvertiseDatas = advertiseTos.getContent().stream().map(adv -> {
            OnlineAdvertiseData rslt;
            try {
                rslt = onlineService.getData(new URL(adv.getWebSiteLink()));
            } catch (IOException e) {
                e.printStackTrace();
                logger.info("can not get data for ");
                return null;
            }
            return rslt;
        }).collect(Collectors.toList());
            model.addAttribute("onlineadvs", onlineAdvertiseDatas);
            model.addAttribute("search", search);
            model.addAttribute("currentPage", pagenumber);
            model.addAttribute("adminMessage", adminMessageService.getLastMessage());
            if (pagenumber > advertiseTos.getTotalPages()) return "index";
            model.addAttribute("advertises", advertiseTos);//.getContent());
            model.addAttribute("pages", PageCalculator.getMyPage(advertiseTos.getTotalPages(), pagenumber, 7));
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

    @GetMapping("/allPages")
    public String allPagesget() {
        return "allPages";
    }
}
