package com.eni.enchere.ihm;

import com.eni.enchere.services.CustomUserDetailsService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    private final CustomUserDetailsService userDetailsService;

    public AdminController(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/admin/users")
    public String viewUsers() {
        return "admin_users";
    }

}
