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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    @GetMapping("/enchere/vente")
    public String afficherDetailArticle(@RequestParam("id") long id, Model model) {
        ArticleVendu article = ArticleVenduService.getArticleVenduById(id);

        if (article == null) {
            model.addAttribute("message", "Article introuvable");
            return "redirect:/enchere";
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Utilisateur utilisateurConnecte = utilisateurService.getUtilisateurByPseudo(auth.getName());

        // Récupérer meilleure enchère UNE SEULE FOIS
        Enchere meilleureEnchere = enchereService.getMeilleureEnchereParArticleId(id);

        LocalDate dateFin = article.getFin_encheres();
        LocalDate today = LocalDate.now();

        if (!dateFin.isAfter(today)) {
            // L'enchère est terminée

            // Cas vendeur
            if (article.getNoUtilisateur().getNoUtilisateur() == utilisateurConnecte.getNoUtilisateur()) {
                return "redirect:/enchere/product?id=" + article.getNoArticle();
            }

            // Cas gagnant
            if (meilleureEnchere != null &&
                    meilleureEnchere.getNoUtilisateur().getNoUtilisateur() == utilisateurConnecte.getNoUtilisateur()) {
                return "redirect:/enchere/win?id=" + article.getNoArticle();
            }

            // Cas perdu
            model.addAttribute("article", article);
            model.addAttribute("meilleureEnchere", meilleureEnchere);
            model.addAttribute("utilisateurConnecte", utilisateurConnecte);
            return "enchere_perdu";
        }

        // Enchère en cours
        boolean venteCommencee = !article.getDebut_encheres().isAfter(today);
        model.addAttribute("article", article);
        model.addAttribute("meilleureEnchere", meilleureEnchere);
        model.addAttribute("utilisateurConnecte", utilisateurConnecte);
        model.addAttribute("venteCommencee", venteCommencee);

        return "enchere_vente";
    }
    @GetMapping("/enchere/modifier")
    public String modifierDetailArticle(@RequestParam("id") long id, Model model) {
        ArticleVendu article = ArticleVenduService.getArticleVenduById(id);

        if (article == null) {
            model.addAttribute("message", "Article introuvable");
            return "redirect:/enchere";
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Utilisateur utilisateurConnecte = utilisateurService.getUtilisateurByPseudo(auth.getName());

        // Récupérer meilleure enchère UNE SEULE FOIS
        Enchere meilleureEnchere = enchereService.getMeilleureEnchereParArticleId(id);
        model.addAttribute("article", article);
        model.addAttribute("meilleureEnchere", meilleureEnchere);
        model.addAttribute("categories", categorieService.getAllCategories());
        model.addAttribute("utilisateur", utilisateurConnecte);

        return "enchere_edit";
    }


    @PostMapping("/encherir")
    public String enchirirArticle(@RequestParam long montant,
                                  @RequestParam long noArticle,
                                  RedirectAttributes redirectAttributes) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Utilisateur utilisateurConnecte = utilisateurService.getUtilisateurByPseudo(auth.getName());

        if (utilisateurConnecte == null) {
            return "redirect:/login";
        }

        try {
            enchereService.ajouterEnchere(utilisateurConnecte.getNoUtilisateur(), noArticle, montant);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("erreur", e.getMessage());
            return "redirect:/enchere/vente?id=" + noArticle;
        }

        return "redirect:/enchere";
    }




    @GetMapping("/enchere/win")
    public String enchereWin(@RequestParam("id") long id, Model model) {
        ArticleVendu article = ArticleVenduService.getArticleVenduById(id);
        Enchere meilleureEnchere = enchereService.getMeilleureEnchereParArticleId(id);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Utilisateur utilisateurConnecte = utilisateurService.getUtilisateurByPseudo(auth.getName());

        // Comparaison sécurisée par ID
        if (meilleureEnchere == null ||
                meilleureEnchere.getNoUtilisateur().getNoUtilisateur() != utilisateurConnecte.getNoUtilisateur()) {
            return "redirect:/enchere"; // Redirige si ce n'est pas le gagnant
        }

        model.addAttribute("article", article);
        model.addAttribute("meilleureEnchere", meilleureEnchere);
        model.addAttribute("utilisateurConnecte", utilisateurConnecte);

        return "enchere_win";
    }


    @PostMapping("/enchere/delete")
    public String deleteArticle(@RequestParam("id") long id) {
        enchereService.deleteEnchere(id);
        return "redirect:/enchere?success=suppression"; // Redirige avec un petit message si tu veux
    }


    @GetMapping("/enchere/product")
    public String enchereProduct(@RequestParam("id") long id, Model model) {
        ArticleVendu article = ArticleVenduService.getArticleVenduById(id);
        if (article == null) {
            model.addAttribute("message", "Article introuvable");
            return "redirect:/enchere";
        }

        Enchere meilleureEnchere = enchereService.getMeilleureEnchereParArticleId(id);

        model.addAttribute("article", article);
        model.addAttribute("meilleureEnchere", meilleureEnchere);

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
                                     @RequestParam("photo") MultipartFile photo,
                                     HttpSession session) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Utilisateur utilisateurConnecte = utilisateurService.getUtilisateurByPseudo(auth.getName());

        article.setNoUtilisateur(utilisateurConnecte);
        article.setPrixVente(article.getPrixInitial());

        // 📂 Gestion du fichier photo
        if (!photo.isEmpty()) {
            try {
                // Utilisation de la racine du projet + "uploads" (répertoire à côté de src)
                String basePath = System.getProperty("user.dir");  // Obtient le répertoire de travail actuel
                Path uploadPath = Paths.get(basePath, "src/main/resources/static/uploads");  // Crée un répertoire 'uploads' à la racine

                // Vérifie si le dossier existe, sinon, crée-le
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // Génère le nom du fichier avec un timestamp unique
                String originalFilename = photo.getOriginalFilename();
                Path destinationPath = uploadPath.resolve(System.currentTimeMillis() + "_" + originalFilename);

                // Sauvegarde le fichier dans le dossier 'uploads'
                photo.transferTo(destinationPath.toFile());

                // Stockage de l'URL relative en BDD (accessible depuis la racine de l'application)
                article.setUrl("/uploads/" + destinationPath.getFileName().toString());

            } catch (IOException e) {
                e.printStackTrace();
                return "redirect:/vente/enregistrer"; // Redirige ou affiche une erreur en cas d'exception
            }
        }

        ArticleVenduService.insertArticleVendu(article);
        return "redirect:/enchere"; // Redirige vers la page des enchères
    }

    @PostMapping("/vente/modifier")
    public String modifierArticle(
            @RequestParam("id") Long id,
            @ModelAttribute("article") ArticleVendu articleModifie,
            @RequestParam(value = "photo", required = false) MultipartFile photo
    ) {
        ArticleVendu articleExistant = ArticleVenduService.getArticleVenduById(id);
        if (articleExistant == null) {
            return "redirect:/enchere?erreur=articleIntrouvable";
        }

        // Champs toujours remplacés
        articleExistant.setNom_article(articleModifie.getNom_article());
        articleExistant.setDescription(articleModifie.getDescription());
        articleExistant.setPrixInitial(articleModifie.getPrixInitial());
        articleExistant.setNoCategorie(articleModifie.getNoCategorie());

        // Début d'enchère seulement si renseigné
        if (articleModifie.getDebut_encheres() != null) {
            articleExistant.setDebut_encheres(articleModifie.getDebut_encheres());
        }

        // Fin d'enchère seulement si renseigné
        if (articleModifie.getFin_encheres() != null) {
            articleExistant.setFin_encheres(articleModifie.getFin_encheres());
        }

        // Photo seulement si une nouvelle est uploadée
        if (photo != null && !photo.isEmpty()) {
            String nomFichier = photo.getOriginalFilename();
            articleExistant.setUrl(nomFichier);
        }

        ArticleVenduService.updateArticleVendu(articleExistant);

        return "redirect:/enchere";
    }



}
