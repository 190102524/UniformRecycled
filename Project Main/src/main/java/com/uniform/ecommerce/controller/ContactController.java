package com.uniform.ecommerce.controller;

import com.uniform.ecommerce.model.Contact;
import com.uniform.ecommerce.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

/**
 * RESTful web service controller for managing contact messages.
 */
@RestController
public class ContactController {
    @Autowired
    private ContactRepository contactRepository;


    /**
     * This method returns the view for the Contact us page.
     * @return A ModelAndView object containing the contact view and a new Contact object.
     */
    @GetMapping("/contact")
    public ModelAndView viewContactPage() {
        ModelAndView modelAndView = new ModelAndView("contact");
        modelAndView.addObject("contact", new Contact());
        return modelAndView;
    }

    /**
     * This method handles the submission of the contact form and saves the data to the database.
     * @param contact A Contact object containing the form data.
     * @return A ModelAndView object containing the contact view.
     */
    @PostMapping("/submit-form")
    public ModelAndView submitForm(@ModelAttribute Contact contact) {
        contactRepository.save(contact);
        return new ModelAndView("contact");
        //return new ModelAndView("success"); // success page
    }

    /**
     * This method returns the view for the success message page.
     * @param model A Model object for holding the model data.
     * @param principal A Principal object representing the currently authenticated user.
     * @return A ModelAndView object containing the success message view.
     */
    @GetMapping("/successMessage")
    public ModelAndView viewSuccessMessage(Model model, Principal principal) {
        ModelAndView mav = new ModelAndView("successMessage");
        return mav;
    }
}
