package com.eni.enchere.ihm;

import com.eni.enchere.bo.Utilisateur;
import com.eni.enchere.services.UtilisateurService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class AdminController {

    private final UtilisateurService utilisateurService;

    public AdminController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @GetMapping("/admin/users")
    public String viewUsers(Model model) {
        List<Utilisateur> utilisateurs = utilisateurService.getUtilisateurs();

        model.addAttribute("utilisateurs", utilisateurs);

        return "admin_users";
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

}
