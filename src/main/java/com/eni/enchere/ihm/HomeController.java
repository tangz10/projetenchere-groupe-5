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
            @RequestParam(value = "categorie", required = false, defaultValue = "0") Long categorieId,
            Model model
    ) {
        List<ArticleVendu> articleVendus;

        if (searchQuery != null && !searchQuery.isBlank()) {
            // Recherche par nom uniquement
            articleVendus = ArticleVenduService.getAllArticleVenduByName(searchQuery);
            model.addAttribute("message", "Résultats pour : " + searchQuery);
        } else if (categorieId != null && categorieId != 0) {
            // Filtrage par catégorie
            articleVendus = ArticleVenduService.getArticleVenduByCategorie(categorieId);
            model.addAttribute("message", "Articles de la catégorie sélectionnée");
        } else {
            // Tous les articles
            articleVendus = ArticleVenduService.getAllArticleVendu();
            model.addAttribute("message", "Bienvenue sur mon site Spring Boot + Bootstrap !");
        }

        model.addAttribute("articleVendus", articleVendus);
        model.addAttribute("categories", categorieService.getAllCategories());
        model.addAttribute("categorieId", categorieId); // Pour garder la sélection côté front

        return "index";
    }


}
