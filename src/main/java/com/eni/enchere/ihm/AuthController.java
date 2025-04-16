package com.eni.enchere.ihm;

import com.eni.enchere.bo.Utilisateur;
import com.eni.enchere.dao.Utilisateur.UtilisateurDAOImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.crypto.password.PasswordEncoder;

@Controller
public class AuthController {

    private final UtilisateurDAOImpl utilisateurDAO;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UtilisateurDAOImpl utilisateurDAO, PasswordEncoder passwordEncoder) {
        this.utilisateurDAO = utilisateurDAO;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String viewLogin() {
        return "login";
    }

    @GetMapping("/register")
    public String viewRegister() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(Utilisateur utilisateur, Model model) {/*
        // vérifier que l'utilisateur n'existe pas déjà
        if (utilisateurDAO.selectByPseudo(utilisateur.getPseudo())) {
            model.addAttribute("error", "Ce nom d'utilisateur existe déjà.");
            return "register";
        }*/

        utilisateur.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));
        utilisateur.setCredit(0);
        utilisateur.setAdmin(false);
        utilisateurDAO.insert(utilisateur);

        return "redirect:/login?registered";
    }

    @GetMapping("/my_profile")
    public String myProfile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Utilisateur utilisateurConnecte = utilisateurDAO.selectByPseudo(auth.getName());

        model.addAttribute("utilisateur", utilisateurConnecte);

        return "profile_read";
    }

    @GetMapping("/my_profile/edit")
    public String profileEdit(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Utilisateur utilisateurConnecte = utilisateurDAO.selectByPseudo(auth.getName());

        model.addAttribute("utilisateur", utilisateurConnecte);

        return "profile_edit";
    }

    @GetMapping("/user/{pseudo}")
    public String user(@PathVariable String pseudo, Model model) {
        Utilisateur utilisateur = utilisateurDAO.selectByPseudo(pseudo);

        model.addAttribute("utilisateur", utilisateur);

        return "profile_read";
    }

}
