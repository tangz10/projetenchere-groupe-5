package com.eni.enchere.ihm;

import com.eni.enchere.bo.Utilisateur;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UIController {

    @GetMapping("/test")
    public String forgotPassword(Utilisateur utilisateur) {

        return "test_ui";
    }
}
