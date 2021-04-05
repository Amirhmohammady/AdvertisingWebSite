package com.mycompany.advertising.controller;

import com.mycompany.advertising.api.AuthenticationFacade;
import com.mycompany.advertising.api.MessageService;
import com.mycompany.advertising.api.UserService;
import com.mycompany.advertising.entity.Role;
import com.mycompany.advertising.model.to.MessageTo;
import com.mycompany.advertising.model.to.UserTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.List;

/**
 * Created by Amir on 6/6/2020.
 */
@Controller
public class TestController {
    @Autowired
    UserService userService;
    @Autowired
    AuthenticationFacade authenticationFacade;

    @GetMapping("/test1")//username and user_role
    public String test(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username;
        Object principal = auth.getPrincipal();
        if (principal instanceof UserDetails) {
            username = "Amir2: " + ((UserDetails) principal).getUsername();
        } else {
            username = "Amir3: " + principal.toString();
        }
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();

        model.addAttribute("var1", username);
        model.addAttribute("var2", authorities);
        return "test1";
    }

    @GetMapping("/test2")//userID username hashedpassword user_role
    public String test2(Model model) {
        UserTo userTo = authenticationFacade.getUserToDetails();
        model.addAttribute("var1", userTo);
        return "test2";
    }

    @GetMapping(value = "/test3")//sessionID
    public String test3(HttpSession session, Model model) {
        String jsessionid = session.getId();
        System.out.println(jsessionid + "&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
        model.addAttribute("var1", jsessionid);
        return "test3";
    }

    @GetMapping(value = "/test4")//language test
    public String test4(Model model) {
        return "test4";
    }

    @GetMapping("/test/createAdminUser")
    public String createAdminUser(Model model) {
        UserTo user = new UserTo();
        user.setUsername("amir");
        user.setPassword("1234");
        user.grantAuthority(Role.ROLE_ADMIN);
        try {
            userService.svaeUser(user);
        } catch (Exception e) {
        }
        return "test1";
    }

    @Autowired
    MessageService messageService;

    @GetMapping("/test5")//check first page for changing language
    public String test5(Model model) {
        return "redirect:/test5/search=/page=1";
    }

    @GetMapping(value = "/test5/search={search}/page={pagenumber}")//, produces = "text/plain;charset=UTF-8")
//change language test
    public String mainTest5(Model model, @PathVariable String search, @PathVariable int pagenumber,
                            @RequestParam(required = false, name = "search") String search02,
                            @RequestParam(required = false, name = "lan") String language,
                            HttpServletRequest request, HttpServletResponse response) {

        List<MessageTo> messageTos;
        System.out.println(search02 + "444444444444444444444444444444");
        System.out.println(search + "444444444444444444444444444444");
        boolean hasparam = false;
        if (language != null) {
            hasparam = true;
            LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
            localeResolver.setLocale(request, response, StringUtils.parseLocaleString(language));
        }
        if (search02 != null) {
            hasparam = true;
            try {
                search = URLEncoder.encode(search02, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        if (hasparam) return "redirect:/test5/search=" + search + "/page=" + pagenumber;
        if (pagenumber < 1) pagenumber = 1;
        if (search == null | search.equals("")) {
            messageTos = messageService.getPageMessages(pagenumber).getContent();
        } else {
            messageTos = messageService.getPageMessages(pagenumber, search).getContent();
        }
        model.addAttribute("advertises", messageTos);
        UserTo userTo = authenticationFacade.getUserToDetails();
        if (userTo != null)
            model.addAttribute("userId", userTo.getId());
        else
            model.addAttribute("userId", -1);
        return "test5";
    }

    @GetMapping("/deletetest/id={id}")//delete test
    public String delTest(Model model, @PathVariable Long id) {

        return "null";
    }

    @GetMapping("/test6")//get current url
    public String test6() {
        return "test6";
    }
}
