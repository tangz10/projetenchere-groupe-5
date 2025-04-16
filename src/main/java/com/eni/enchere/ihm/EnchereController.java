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
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
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

        List<ArticleVendu> articleVendus = ArticleVenduService.getAllArticleVendu();
        model.addAttribute("articleVendus", articleVendus);
        model.addAttribute("message", "Consulter les enchères");
        return "enchere";
    }

    @GetMapping("/enchere/recherche")
    public String rechercheArticles(@RequestParam("searchQuery") String searchQuery, Model model) {
        // Effectuer la recherche dans la base de données
        List<ArticleVendu> articlesTrouves = ArticleVenduService.getAllArticleVenduByName(searchQuery);
        model.addAttribute("articleVendus", articlesTrouves);
        List<Categorie> categories = categorieService.getAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("message", "Résultats de recherche pour : " + searchQuery);
        return "enchere"; // Retourne la page des enchères avec les résultats
    }

    @GetMapping("/enchere/filtrerParCategorie")
    public String filtrerParCategorie(@RequestParam("categorie") long categorieId, Model model) {
        // Si la valeur de la catégorie est 0, cela signifie "Toutes", donc on récupère tous les articles
        List<ArticleVendu> articles = (categorieId == 0) ? ArticleVenduService.getAllArticleVendu()
                : ArticleVenduService.getArticleVenduByCategorie(categorieId);

        // On passe les articles, les catégories, et la catégorie sélectionnée au modèle
        model.addAttribute("articleVendus", articles);
        model.addAttribute("categories", categorieService.getAllCategories()); // Pour afficher les catégories dans le formulaire
        model.addAttribute("categorieId", categorieId); // On passe la catégorie sélectionnée au modèle

        return "enchere"; // Retourne la vue avec les résultats filtrés
    }

    @GetMapping("/enchere/mes-ventes")
    public String afficherMesVentes(
            @RequestParam(value = "enCours", required = false) String enCours,
            @RequestParam(value = "nonDebutees", required = false) String nonDebutees,
            @RequestParam(value = "terminees", required = false) String terminees,
            HttpSession session,
            Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Utilisateur utilisateurConnecte = utilisateurService.getUtilisateurDAO(auth.getName());

        if (utilisateurConnecte == null) {
            return "redirect:/login";
        }

        List<ArticleVendu> toutesMesVentes = ArticleVenduService.getArticleVenduByUser(utilisateurConnecte.getNoUtilisateur());
        List<ArticleVendu> articlesFiltres = new ArrayList<>();

        LocalDate aujourdHui = LocalDate.now();

        // Si aucun filtre spécifique n’est coché → on affiche tout
        if (enCours == null && nonDebutees == null && terminees == null) {
            articlesFiltres = toutesMesVentes;
        } else {
            for (ArticleVendu article : toutesMesVentes) {
                boolean ajouter = false;

                if (enCours != null && !article.getDebut_encheres().isAfter(aujourdHui) && article.getFin_encheres().isAfter(aujourdHui)) {
                    ajouter = true;
                }

                if (nonDebutees != null && article.getDebut_encheres().isAfter(aujourdHui)) {
                    ajouter = true;
                }

                if (terminees != null && article.getFin_encheres().isBefore(aujourdHui)) {
                    ajouter = true;
                }

                if (ajouter) {
                    articlesFiltres.add(article);
                }
            }
        }

        model.addAttribute("articleVendus", articlesFiltres);
        model.addAttribute("categories", categorieService.getAllCategories());
        model.addAttribute("activeFilter", "ventes");

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
