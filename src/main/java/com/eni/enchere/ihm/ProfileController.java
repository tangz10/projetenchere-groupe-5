package com.eni.enchere.ihm;

import com.eni.enchere.bo.Utilisateur;
import com.eni.enchere.services.UtilisateurService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public class ProfileController {

    private final UtilisateurService utilisateurService;

    public ProfileController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @GetMapping("/my_profile")
    public String myProfile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Utilisateur utilisateurConnecte = utilisateurService.getUtilisateurDAO(auth.getName());

        model.addAttribute("utilisateur", utilisateurConnecte);

        return "profile_read";
    }

    @GetMapping("/my_profile/edit")
    public String myProfileEdit(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Utilisateur utilisateurConnecte = utilisateurService.getUtilisateurDAO(auth.getName());

        model.addAttribute("utilisateur", utilisateurConnecte);

        return "profile_edit";
    }

    @GetMapping("/user/{pseudo}")
    public String user(@PathVariable String pseudo, Model model) {
        Utilisateur utilisateur = utilisateurService.getUtilisateurDAO(pseudo);

        model.addAttribute("utilisateur", utilisateur);

        return "profile_read";
    }
}