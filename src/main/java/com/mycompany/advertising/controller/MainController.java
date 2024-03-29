package com.mycompany.advertising.controller;

import com.mycompany.advertising.controller.events.OnSigningUpCompleteEvent;
import com.mycompany.advertising.controller.utils.PageCalculator;
import com.mycompany.advertising.entity.UserAlreadyExistException;
import com.mycompany.advertising.model.to.AdvertiseTo;
import com.mycompany.advertising.model.to.UserTo;
import com.mycompany.advertising.model.to.enums.Role;
import com.mycompany.advertising.service.api.AdminMessageService;
import com.mycompany.advertising.service.api.AdvertiseService;
import com.mycompany.advertising.service.api.OnlineAdvertiseDataService;
import com.mycompany.advertising.service.api.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.lang.invoke.MethodHandles;
import java.net.URLEncoder;
//import org.springframework.context.MessageSource;

/**
 * Created by Amir on 1/30/2020.
 */
@Controller
public class MainController {
    private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());
    @Autowired
    OnlineAdvertiseDataService onlineService;
    /*@Autowired
    private MessageSource messages;*/
    @Autowired
    private AdminMessageService adminMessageService;
    @Value("${amir.error.folder}")
    private String errorfolder;
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

    /*
    @Autowired
    private LockerApiService lockerApiService;
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
    }*/

    /*@GetMapping("/login_error")
    public String loginErrorGet(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            AuthenticationException ex = (AuthenticationException) session
                    .getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            if (ex != null) {
                model.addAttribute("errorMessage", ex.getMessage());
            }
        }
        String username = request.getParameter("username");
        model.addAttribute("failedUsername", username);
        model.addAttribute("phoneNoStatus", userService.getUserStatuseByPhoneNumber(username));
        logger.info("user " + username + " failed to login");
        return "login";
    }*/

    @PostMapping("/login_error")
    public String loginErrorPost(Model model, HttpServletRequest request) {
        String username = request.getParameter("username");
        if (username != null) {
            if (username.charAt(0) != '0') username = '0' + username;
            model.addAttribute("failedUsername", username);
            model.addAttribute("phoneNoStatus", userService.getUserStatuseByPhoneNumber(username));
        }
        HttpSession session = request.getSession(false);
        if (session != null) {
            AuthenticationException ex = (AuthenticationException) session.getAttribute("exception");
            if (ex != null) {
                if (ex instanceof BadCredentialsException)
                    model.addAttribute("errorMessage", "UserName or Password is wrong");
                else model.addAttribute("errorMessage", ex.getMessage());
            }
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
