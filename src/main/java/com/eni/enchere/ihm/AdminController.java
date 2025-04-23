package com.eni.enchere.ihm;

import com.eni.enchere.bo.Utilisateur;
import com.eni.enchere.services.UtilisateurService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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

}
