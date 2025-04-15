package com.eni.enchere.ihm;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileController {
    @GetMapping("/profile")
    public String login(Model model) {
        model.addAttribute("message", "Veuillez vous connecter");
        return "profile"; // Retourne "login.html" depuis /templates
    }
}
