package com.uniform.ecommerce.controller.admin;

import com.uniform.ecommerce.model.Role;
import com.uniform.ecommerce.model.User;
import com.uniform.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/admin/user")
public class AdminUserController {

    @Autowired
    private UserService service;

    // Returns page of users
    @GetMapping
    public ModelAndView getAdminUsersPage() {
        ModelAndView mav = new ModelAndView("admin/user/user");
        List<User> listUsers = service.listAll();
        mav.addObject("users", listUsers);
        return mav;
    }

    // returns edit user page
    @GetMapping("/edit/{id}")
    public ModelAndView editUser(@PathVariable("id") Integer id) {
        ModelAndView mav = new ModelAndView("admin/user/user_form");
        User user = service.get(id);
        List<Role> listRoles = service.getRoles();
        mav.addObject("user", user);
        mav.addObject("listRoles", listRoles);
        return mav;
    }

    // saves user
    @PostMapping("/save")
    public ModelAndView saveUser(User user) {
        ModelAndView mav = new ModelAndView("redirect:/admin/user");
        service.save(user);
        return mav;
    }

}