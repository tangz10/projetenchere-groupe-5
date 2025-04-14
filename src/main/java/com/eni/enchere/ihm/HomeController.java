package com.eni.enchere.ihm;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

public class HomeController {

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("message", "Bienvenue sur mon site Spring Boot + Bootstrap !");
        return "index"; // correspond à templates/index.html
    }

}
