package com.eni.enchere.ihm;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EnchereController {

    @GetMapping("/enchere")
    public String enchere(Model model) {
        model.addAttribute("message", "Consulter les enchères");
        return "enchere";
    }

    @GetMapping("/enchere/vente")
    public String enchereVente(Model model) {
        model.addAttribute("message", "Consulter une vente aux enchères");
        return "enchere_vente";
    }

}
