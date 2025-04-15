package com.eni.enchere.ihm;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EnchereController {

    @GetMapping("/enchere")
    public String enchere(Model model) {
        model.addAttribute("message", "Editez votre profile");
        return "enchere";
    }

}
