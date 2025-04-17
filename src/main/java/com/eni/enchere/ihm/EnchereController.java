package com.eni.enchere.ihm;

import com.eni.enchere.bo.ArticleVendu;
import com.eni.enchere.bo.Categorie;
import com.eni.enchere.bo.Enchere;
import com.eni.enchere.bo.Utilisateur;
import com.eni.enchere.services.ArticleVenduService;
import com.eni.enchere.services.CategorieService;
import com.eni.enchere.services.*;
import com.eni.enchere.services.UtilisateurService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class EnchereController {

    @Autowired
    private CategorieService categorieService;
    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private ArticleVenduService ArticleVenduService;

    @Autowired
    private EnchereService enchereService;

    private void ajouterInfosEncheres(List<ArticleVendu> articles, Utilisateur utilisateurConnecte, Model model) {
        Map<Long, Enchere> meilleuresEncheres = new HashMap<>();
        Map<Long, Integer> classementUtilisateur = new HashMap<>();

        long userId = utilisateurConnecte.getNoUtilisateur();
        System.out.println("Utilisateur connecté ID: " + userId);

        for (ArticleVendu article : articles) {
            long noArticle = article.getNoArticle();
            System.out.println("\n--- Article traité : " + noArticle + " ---");

            // Meilleure enchère
            Enchere meilleure = enchereService.getMeilleureEnchereParArticleId(noArticle);
            if (meilleure != null) {
                System.out.println("Meilleure enchère trouvée : " + meilleure.getMontantEnchere());
                meilleuresEncheres.put(noArticle, meilleure);
            } else {
                System.out.println("Aucune enchère pour cet article.");
            }

            // Classement personnel
            List<Enchere> encheres = enchereService.getClassementEncheres(noArticle);
            System.out.println("Enchères pour l'article " + noArticle + " : ");
            for (Enchere enchere : encheres) {
                System.out.println("  Utilisateur : " + enchere.getNoUtilisateur().getNoUtilisateur() +
                        ", Montant : " + enchere.getMontantEnchere());
            }

            for (int i = 0; i < encheres.size(); i++) {
                long enchereUserId = encheres.get(i).getNoUtilisateur().getNoUtilisateur();
                System.out.println("Comparaison : enchérisseur ID = " + enchereUserId + " vs utilisateur connecté = " + userId);
                if (enchereUserId == userId) {
                    classementUtilisateur.put(noArticle, i + 1); // commence à 1
                    System.out.println("→ Classement utilisateur pour l'article " + noArticle + " : " + (i + 1));
                    break;
                }
            }
        }

        System.out.println("→ Meilleures enchères envoyées à la vue : " + meilleuresEncheres.keySet());
        System.out.println("→ Classements envoyés à la vue : " + classementUtilisateur);

        model.addAttribute("meilleuresEncheres", meilleuresEncheres);
        model.addAttribute("classements", classementUtilisateur);
    }



    @GetMapping("/enchere")
    public String enchere(Model model) {
        List<Categorie> categories = categorieService.getAllCategories();
        model.addAttribute("categories", categories);

        List<ArticleVendu> articleVendus = ArticleVenduService.getAllArticleVendu();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Utilisateur utilisateurConnecte = utilisateurService.getUtilisateurByPseudo(auth.getName());
        ajouterInfosEncheres(articleVendus, utilisateurConnecte, model);

        model.addAttribute("articleVendus", articleVendus);
        model.addAttribute("categories", categorieService.getAllCategories());

        return "enchere";
    }



    @GetMapping("/enchere/recherche")
    public String rechercheArticles(@RequestParam("searchQuery") String searchQuery, Model model) {
        // Effectuer la recherche dans la base de données
        List<ArticleVendu> articlesTrouves = ArticleVenduService.getAllArticleVenduByName(searchQuery);
        model.addAttribute("articleVendus", articlesTrouves);
        model.addAttribute("categories", categorieService.getAllCategories());
        model.addAttribute("message", "Résultats de recherche pour : " + searchQuery);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Utilisateur utilisateurConnecte = utilisateurService.getUtilisateurByPseudo(auth.getName());
        ajouterInfosEncheres(articlesTrouves, utilisateurConnecte, model);

        return "enchere";
    }

    @GetMapping("/enchere/filtrerParCategorie")
    public String filtrerParCategorie(@RequestParam("categorie") long categorieId, Model model) {
        List<ArticleVendu> articles = (categorieId == 0)
                ? ArticleVenduService.getAllArticleVendu()
                : ArticleVenduService.getArticleVenduByCategorie(categorieId);

        // On passe les articles, les catégories, et la catégorie sélectionnée au modèle
        model.addAttribute("articleVendus", articles);
        model.addAttribute("categories", categorieService.getAllCategories());
        model.addAttribute("categorieId", categorieId);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Utilisateur utilisateurConnecte = utilisateurService.getUtilisateurByPseudo(auth.getName());
        ajouterInfosEncheres(articles, utilisateurConnecte, model);

        return "enchere";
    }


    @GetMapping("/enchere/mes-ventes")
    public String afficherMesVentes(
            @RequestParam(value = "enCours", required = false) String enCours,
            @RequestParam(value = "nonDebutees", required = false) String nonDebutees,
            @RequestParam(value = "terminees", required = false) String terminees,
            HttpSession session,
            Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Utilisateur utilisateurConnecte = utilisateurService.getUtilisateurByPseudo(auth.getName());

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


        ajouterInfosEncheres(articlesFiltres, utilisateurConnecte, model);

        model.addAttribute("articleVendus", articlesFiltres);
        model.addAttribute("categories", categorieService.getAllCategories());
        model.addAttribute("activeFilter", "ventes");

        return "enchere";
    }
    @GetMapping("/enchere/achats")
    public String afficherAchats(
            @RequestParam(value = "ouvertes", required = false) String ouvertes,
            @RequestParam(value = "AenCours", required = false) String AenCours,
            @RequestParam(value = "remportees", required = false) String remportees,
            Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Utilisateur utilisateurConnecte = utilisateurService.getUtilisateurByPseudo(auth.getName());

        if (utilisateurConnecte == null) {
            return "redirect:/login";
        }

        long userId = utilisateurConnecte.getNoUtilisateur();
        LocalDate aujourdHui = LocalDate.now();

        // Tous les articles sauf ceux créés par l'utilisateur
        List<ArticleVendu> articles = ArticleVenduService.getAllArticleVendu().stream()
                .filter(article -> article.getNoUtilisateur().getNoUtilisateur() != userId)
                .toList();

        List<ArticleVendu> articlesFiltres = new ArrayList<>();

        // Aucun filtre sélectionné => tout afficher par défaut
        boolean aucunFiltre = ouvertes == null && AenCours == null && remportees == null;

        for (ArticleVendu article : articles) {
            boolean ajouter = false;

            if (aucunFiltre) {
                ajouter = true; // si rien n'est coché, on ajoute tout
            }

            // Enchères ouvertes (débuté mais pas fini)
            if (ouvertes != null &&
                    !article.getDebut_encheres().isAfter(aujourdHui) &&
                    article.getFin_encheres().isAfter(aujourdHui)) {
                ajouter = true;
            }

            // Mes enchères en cours
            if (AenCours != null) {
                List<Enchere> encheresUser = enchereService.getEncheresByArticleEtUtilisateur(article.getNoArticle(), userId);
                if (!encheresUser.isEmpty() &&
                        !article.getDebut_encheres().isAfter(aujourdHui) &&
                        article.getFin_encheres().isAfter(aujourdHui)) {
                    ajouter = true;
                }
            }

            // Mes enchères remportées
            if (remportees != null) {
                Enchere meilleure = enchereService.getMeilleureEnchereParArticleId(article.getNoArticle());
                if (meilleure != null &&
                        meilleure.getNoUtilisateur().getNoUtilisateur() == userId &&
                        article.getFin_encheres().isBefore(aujourdHui)) {
                    ajouter = true;
                }
            }

            if (ajouter) {
                articlesFiltres.add(article);
            }
        }

        ajouterInfosEncheres(articlesFiltres, utilisateurConnecte, model);
        model.addAttribute("articleVendus", articlesFiltres);
        model.addAttribute("categories", categorieService.getAllCategories());
        model.addAttribute("activeFilter", "achats");

        return "enchere";
    }

    @GetMapping("/enchere/vente/{id}")
    public String afficherDetailArticle(@PathVariable long id, Model model) {
        ArticleVendu article = ArticleVenduService.getArticleVenduById(id);

        if (article == null) {
            model.addAttribute("message", "Article introuvable");
            return "redirect:/enchere";
        }

        Enchere meilleureEnchere = enchereService.getMeilleureEnchereParArticleId(id);
        model.addAttribute("article", article);
        model.addAttribute("meilleureEnchere", meilleureEnchere);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Utilisateur utilisateurConnecte = utilisateurService.getUtilisateurByPseudo(auth.getName());
        model.addAttribute("utilisateurConnecte", utilisateurConnecte);

        // Récupérer la date de début de l'enchère
        LocalDate debutEnchere = article.getDebut_encheres();

        // Récupérer la date actuelle (sans l'heure)
        LocalDate today = LocalDate.now();

        // La vente est considérée commencée si la date de début de l'enchère est égale ou avant aujourd'hui
        boolean venteCommencee = !debutEnchere.isAfter(today);  // Si la date de début est avant ou égale à aujourd'hui
        model.addAttribute("venteCommencee", venteCommencee);

        return "enchere_vente";
    }


    @PostMapping("/encherir")
    public String enchirirArticle(@RequestParam long montant,
                                  @RequestParam long noArticle,
                                  HttpSession session) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Utilisateur utilisateurConnecte = utilisateurService.getUtilisateurByPseudo(auth.getName());

        if (utilisateurConnecte == null) {
            return "redirect:/login";
        }

        enchereService.ajouterEnchere(utilisateurConnecte.getNoUtilisateur(), noArticle, montant);

        return "redirect:/enchere";
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

        Utilisateur utilisateurConnecte = utilisateurService.getUtilisateurByPseudo(auth.getName());

        model.addAttribute("utilisateur", utilisateurConnecte);

        model.addAttribute("article", new ArticleVendu());

        model.addAttribute("message", "Nouvelle vente");
        return "enchere_add";
    }

    @PostMapping("/vente/enregistrer")
    public String enregistrerArticle(@ModelAttribute ArticleVendu article,
                                     HttpSession session) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Utilisateur utilisateurConnecte = utilisateurService.getUtilisateurByPseudo(auth.getName());
        article.setNoUtilisateur(utilisateurConnecte);
        article.setPrixVente(article.getPrixInitial());

        ArticleVenduService.insertArticleVendu(article);
        return "redirect:/enchere"; // ou une autre vue après succès
    }


}
