package com.eni.enchere.ihm;

import com.eni.enchere.bo.Categorie;
import com.eni.enchere.services.CategorieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private CategorieService categorieService;


    @GetMapping("/")
    public String index(Model model) {
        List<Categorie> categories = categorieService.getAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("message", "Bienvenue sur mon site Spring Boot + Bootstrap !");
        return "index"; // Retourne "index.html" depuis /templates
    }

}
