package com.uniform.ecommerce.controller.admin;

import com.uniform.ecommerce.model.Contact;
import com.uniform.ecommerce.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;


@RestController
@RequestMapping("/admin/messages")
public class AdminContactController {

    @Autowired
    private ContactService contactService;

    // Returns admin messages page and messages list
    @GetMapping
    public ModelAndView getAdminMessagesPage() {
        ModelAndView mav = new ModelAndView("admin/message/messages");
        mav.addObject("messages", contactService.getAllMessages());
        return mav;
    }

    // method for deleting messages, and reloading page
    @DeleteMapping("/{id}")
    public RedirectView deleteMessages(@PathVariable("id") Long id) {
        contactService.deleteMessage(id);
        return new RedirectView("/admin/messages");
    }

    // method for displaying a message by id
    @GetMapping("/show/{id}")
    public ModelAndView showMessage(@PathVariable("id") Long id) {
        ModelAndView mav = new ModelAndView("admin/message/messageDisplay");
        Contact contact = contactService.getMessageById(id);
        mav.addObject("messages", contact);
        return mav;
    }


}
