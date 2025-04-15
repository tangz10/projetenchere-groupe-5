package com.eni.enchere.ihm;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileController {

    @GetMapping("/profile/edit")
    public String profileEdit(Model model) {
        model.addAttribute("message", "Editez votre profile");
        return "profile"; // Retourne "login.html" depuis /templates
    }

    @GetMapping("/profile/read")
    public String profileRead(Model model) {
        model.addAttribute("message", "Voici votre profile");
        return "profile"; // Retourne "login.html" depuis /templates
    }


}
