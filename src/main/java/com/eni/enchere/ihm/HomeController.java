package com.eni.enchere.ihm;

import com.eni.enchere.bo.ArticleVendu;
import com.eni.enchere.bo.Categorie;
import com.eni.enchere.services.ArticleVenduService;
import com.eni.enchere.services.CategorieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private CategorieService categorieService;

    @Autowired
    private ArticleVenduService ArticleVenduService;


    @GetMapping("/")
    public String index(
            @RequestParam(value = "searchQuery", required = false) String searchQuery,
            Model model
    ) {
        List<ArticleVendu> articleVendus;

        if (searchQuery != null && !searchQuery.isBlank()) {
            articleVendus = ArticleVenduService.getAllArticleVenduByName(searchQuery);
            model.addAttribute("message", "Résultats pour : " + searchQuery);
        } else {
            articleVendus = ArticleVenduService.getAllArticleVendu();
            model.addAttribute("message", "Bienvenue sur mon site Spring Boot + Bootstrap !");
        }

        List<Categorie> categories = categorieService.getAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("articleVendus", articleVendus);

        return "index"; // Toujours la page d'accueil
    }


}
