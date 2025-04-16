package com.eni.enchere.ihm;

import com.eni.enchere.bo.ArticleVendu;
import com.eni.enchere.bo.Categorie;
import com.eni.enchere.bo.Utilisateur;
import com.eni.enchere.dao.Utilisateur.UtilisateurDAOImpl;
import com.eni.enchere.services.ArticleVenduService;
import com.eni.enchere.services.CategorieService;
import com.eni.enchere.services.UtilisateurService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class EnchereController {

    @Autowired
    private CategorieService categorieService;
    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private ArticleVenduService ArticleVenduService;

    @GetMapping("/enchere")
    public String enchere(Model model) {
        List<Categorie> categories = categorieService.getAllCategories();
        model.addAttribute("categories", categories);
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

    @GetMapping("/enchere/add")
    public String enchereNew(Model model) {
        List<Categorie> categories = categorieService.getAllCategories();
        model.addAttribute("categories", categories);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Utilisateur utilisateurConnecte = utilisateurService.getUtilisateurDAO(auth.getName());

        model.addAttribute("utilisateur", utilisateurConnecte);

        model.addAttribute("article", new ArticleVendu());

        model.addAttribute("message", "Nouvelle vente");
        return "enchere_add";
    }

    @PostMapping("/vente/enregistrer")
    public String enregistrerArticle(@ModelAttribute ArticleVendu article,
                                     HttpSession session) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Utilisateur utilisateurConnecte = utilisateurService.getUtilisateurDAO(auth.getName());
        article.setNoUtilisateur(utilisateurConnecte);
        article.setPrixVente(article.getPrixInitial());

        ArticleVenduService.insertArticleVendu(article);
        return "redirect:/enchere"; // ou une autre vue après succès
    }


}
