package com.eni.enchere.ihm;

import com.eni.enchere.services.CustomUserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/admin/users")
    public String viewUsers() {
        return "admin_users";
    }

}
