package com.eni.enchere.ihm;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RegisterController {

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("message", "Veuillez vous enregistrer");
        return "register";
    }

}
