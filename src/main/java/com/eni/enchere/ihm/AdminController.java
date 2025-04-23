package com.eni.enchere.ihm;

import com.eni.enchere.bo.Categorie;
import com.eni.enchere.bo.Utilisateur;
import com.eni.enchere.services.CategorieService;
import com.eni.enchere.services.UtilisateurService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class AdminController {

    private final UtilisateurService utilisateurService;
    private final CategorieService categorieService;

    public AdminController(UtilisateurService utilisateurService, CategorieService categorieService) {
        this.utilisateurService = utilisateurService;
        this.categorieService = categorieService;
    }

    @GetMapping("/admin/users")
    public String viewUsers(Model model) {
        List<Utilisateur> utilisateurs = utilisateurService.getUtilisateurs();

        model.addAttribute("utilisateurs", utilisateurs);

        return "admin_users";
    }

    @GetMapping("/admin/categories")
    public String viewCategories(Model model) {
        List<Categorie> categories = categorieService.getAllCategories();

        model.addAttribute("categories", categories);

        return "admin_categories";
    }

    @GetMapping("/admin/add_category")
    public String addCategory() {
        return "admin_add_category";
    }

    @GetMapping("/admin/edit_category/{id}")
    public String addCategory(@PathVariable int id, Model model) {
        Categorie categorie = categorieService.getCategorieById(id);

        model.addAttribute("categorie", categorie);

        return "admin_edit_category";
    }

    @PostMapping("/admin/delete_user/{id}")
    public String deleteUser(@PathVariable long id) {
        utilisateurService.deleteUtilisateur(id);

        return "redirect:/admin/users";
    }

    @PostMapping("/admin/enable_disable_user/{id}/{is_enabled}")
    public String enableDisableUser(@PathVariable long id, @PathVariable boolean is_enabled) {
        Utilisateur utilisateur = utilisateurService.getUtilisateurById(id);

        utilisateur.setIsActive(!is_enabled);

        utilisateurService.updateUtilisateur(utilisateur);

        return "redirect:/admin/users";
    }

    @PostMapping("/admin/add_category")
    public String addCategory(Categorie categorie) {
        categorieService.insertCategorie(categorie);

        return "redirect:/admin/categories";
    }

    @PostMapping("/admin/edit_category/{id}")
    public String editCategory(@PathVariable long id, Categorie categorie) {
        Categorie categorieToUpdate = categorieService.getCategorieById(id);

        categorieToUpdate.setLibelle(categorie.getLibelle());

        categorieService.updateCategorie(categorieToUpdate);

        return "redirect:/admin/categories";
    }

    @PostMapping("/admin/delete_category/{id}")
    public String deleteCategory(@PathVariable long id) {
        categorieService.deleteCategorie(id);

        return "redirect:/admin/categories";
    }

}
