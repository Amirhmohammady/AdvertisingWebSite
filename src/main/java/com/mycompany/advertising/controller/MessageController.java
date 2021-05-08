package com.mycompany.advertising.controller;

import com.mycompany.advertising.api.AuthenticationFacade;
import com.mycompany.advertising.api.MessageService;
import com.mycompany.advertising.api.StorageService;
import com.mycompany.advertising.entity.MessageStatus;
import com.mycompany.advertising.model.to.MessageTo;
import com.mycompany.advertising.model.to.UserTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

/**
 * Created by Amir on 11/2/2019.
 */
@Controller
public class MessageController {

    @Autowired
    private StorageService storageService;
    @Autowired
    private MessageService messageService;
    @Autowired
    AuthenticationFacade authenticationFacade;
    //private UserService userService;

    @GetMapping("/showMessage")
    public String showMessage(Model model) {
        List<MessageTo> messages = messageService.getPageMessages(1).getContent();
        model.addAttribute("messages", messages);
        return "show_product";
    }

    @GetMapping("/addMessage")
    public String addMessage() {
        return "add_advertise";
    }

    @PostMapping("/addMessage")
    //@ResponseBody
    public String addMessage(Model model, @RequestParam(required = false, name = "pic") MultipartFile file,
                             @RequestParam(required = false, name = "description") String description,
                             @RequestParam(required = false, name = "tel_link") String telegramlink) {
        //String filename = StringUtils.cleanPath(file.getOriginalFilename());
        UserTo userTo = authenticationFacade.getUserToDetails();
        if (file != null) {
            List<String> files = storageService.storeImage(file);
            MessageTo messageTo = new MessageTo();
            messageTo.setTelegramlink(telegramlink);
            messageTo.setImagename(files.get(0));
            messageTo.setStatus(MessageStatus.Not_Accepted);
            messageTo.setSmallimagename(files.get(1));
            messageTo.setText(description);
            messageTo.setOwnerid(userTo.getId());
            messageService.addMessage(messageTo);
            model.addAttribute("succsessmessage","Successfully added your advertise");
        } else {
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            System.out.println("add_advertise no param");
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        }
        return "add_advertise";
    }

    @GetMapping("/delete/id={id}")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    //@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public String delete(Model model, @PathVariable Long id, //Principal principal,
                         @RequestParam(required = true, name = "pLink") String previouslink) {
        Optional<MessageTo> messageoptl = messageService.getMessageById(id);
        if (messageoptl.isPresent()) {
            UserTo userTo = authenticationFacade.getUserToDetails();
            boolean isAuthenticated = false;
            System.out.println(messageoptl.get().getOwnerid() + "-------------------------");
            System.out.println(userTo.getId() + "-------------------------");
            System.out.println("testtttttttttttttttttttttttt");
            if (authenticationFacade.hasRole("ROLE_ADMIN")) {
                isAuthenticated = true;
                System.out.println("admin-------------------------");
            } else if (userTo != null & userTo.getId() == messageoptl.get().getOwnerid()) {
                isAuthenticated = true;
                System.out.println("not admin-------------------------");
            }
            if (isAuthenticated) messageService.deleteMessageById(id);
        }
        return "redirect:" + previouslink;
    }

    @GetMapping("/edit/id={id}")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    //@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public String edit(Model model, @PathVariable Long id,
                       @RequestParam(required = true, name = "pLink") String previouslink) {
        Optional<MessageTo> messageoptl = messageService.getMessageById(id);
        UserTo userTo = authenticationFacade.getUserToDetails();
        if (messageoptl.isPresent())
            if (userTo != null)
                if (userTo.getId() == messageoptl.get().getId()) ;

        return "redirect:" + previouslink;
    }
}
