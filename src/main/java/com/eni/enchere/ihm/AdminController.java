package com.eni.enchere.ihm;

import com.eni.enchere.bo.Categorie;
import com.eni.enchere.bo.Utilisateur;
import com.eni.enchere.services.CategorieService;
import com.eni.enchere.services.UtilisateurService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/admin")
@Controller
public class AdminController {

    private final UtilisateurService utilisateurService;
    private final CategorieService categorieService;

    public AdminController(UtilisateurService utilisateurService, CategorieService categorieService) {
        this.utilisateurService = utilisateurService;
        this.categorieService = categorieService;
    }

    @GetMapping("/users")
    public String viewUsers(Model model) {
        List<Utilisateur> utilisateurs = utilisateurService.getUtilisateurs();

        model.addAttribute("utilisateurs", utilisateurs);

        return "admin_users";
    }

    @GetMapping("/categories")
    public String viewCategories(Model model) {
        List<Categorie> categories = categorieService.getAllCategories();

        model.addAttribute("categories", categories);

        return "admin_categories";
    }

    @GetMapping("/add_category")
    public String addCategory() {
        return "admin_add_category";
    }

    @GetMapping("/edit_category/{id}")
    public String addCategory(@PathVariable int id, Model model) {
        Categorie categorie = categorieService.getCategorieById(id);

        model.addAttribute("categorie", categorie);

        return "admin_edit_category";
    }

    @PostMapping("/delete_user/{id}")
    public String deleteUser(@PathVariable long id) {
        utilisateurService.deleteUtilisateur(id);

        return "redirect:/admin/users";
    }

    @PostMapping("/enable_disable_user/{id}/{is_enabled}")
    public String enableDisableUser(@PathVariable long id, @PathVariable boolean is_enabled) {
        Utilisateur utilisateur = utilisateurService.getUtilisateurById(id);

        utilisateur.setIsActive(!is_enabled);

        utilisateurService.updateUtilisateur(utilisateur);

        return "redirect:/admin/users";
    }

    @PostMapping("/add_category")
    public String addCategory(Categorie categorie) {
        categorieService.insertCategorie(categorie);

        return "redirect:/admin/categories";
    }

    @PostMapping("/edit_category/{id}")
    public String editCategory(@PathVariable long id, Categorie categorie) {
        Categorie categorieToUpdate = categorieService.getCategorieById(id);

        categorieToUpdate.setLibelle(categorie.getLibelle());

        categorieService.updateCategorie(categorieToUpdate);

        return "redirect:/admin/categories";
    }

    @PostMapping("/delete_category/{id}")
    public String deleteCategory(@PathVariable long id) {
        categorieService.deleteCategorie(id);

        return "redirect:/admin/categories";
    }

}
