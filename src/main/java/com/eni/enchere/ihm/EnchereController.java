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

    @GetMapping("/enchere/win")
    public String enchereWin(Model model) {
        model.addAttribute("message", "Vous avez remporté l'enchère");
        return "enchere_win";
    }

    @GetMapping("/enchere/delete")
    public String enchereDelete(Model model) {
        model.addAttribute("message", "Suppression de l'enchère");
        return "enchere_delete";
    }

    @GetMapping("/enchere/product")
    public String enchereProduct(Model model) {
        model.addAttribute("message", "Details de votre enchère");
        return "enchere_product";
    }

}
