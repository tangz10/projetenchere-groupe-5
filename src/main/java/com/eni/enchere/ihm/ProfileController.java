package com.eni.enchere.ihm;

import com.eni.enchere.bo.Utilisateur;
import com.eni.enchere.services.CustomUserDetailsService;
import com.eni.enchere.services.EnchereService;
import com.eni.enchere.services.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProfileController {


    private final UtilisateurService utilisateurService;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService userDetailsService;


    public ProfileController(UtilisateurService utilisateurService, PasswordEncoder passwordEncoder, CustomUserDetailsService userDetailsService) {
        this.utilisateurService = utilisateurService;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/my_profile")
    public String viewMyProfile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Utilisateur utilisateurConnecte = utilisateurService.getUtilisateurByPseudo(auth.getName());

        model.addAttribute("utilisateur", utilisateurConnecte);

        return "profile_read";
    }

    @GetMapping("/my_profile/edit")
    public String viewMyProfileEdit(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Utilisateur utilisateurConnecte = utilisateurService.getUtilisateurByPseudo(auth.getName());

        model.addAttribute("utilisateur", utilisateurConnecte);

        return "profile_edit";
    }

    @GetMapping("/user/{pseudo}")
    public String viewUser(@PathVariable String pseudo, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Utilisateur utilisateur = utilisateurService.getUtilisateurByPseudo(pseudo);

        if (utilisateur.getPseudo().equals(auth.getName()))
        {
            return "redirect:/my_profile";
        }

        model.addAttribute("utilisateur", utilisateur);

        return "profile_read";
    }

    @PostMapping("/my_profile/edit")
    public String editMyProfile(Utilisateur utilisateur) {
        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();

        Utilisateur utilisateurConnecte = utilisateurService.getUtilisateurByPseudo(currentAuth.getName());

        utilisateur.setNoUtilisateur(utilisateurConnecte.getNoUtilisateur());

        if (utilisateur.getMotDePasse() != null && !utilisateur.getMotDePasse().isEmpty()) {
            utilisateur.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));
        } else {
            utilisateur.setMotDePasse(utilisateurConnecte.getMotDePasse());
        }

        utilisateurService.updateUtilisateur(utilisateur);

        UserDetails userDetails = userDetailsService.loadUserByUsername(utilisateur.getPseudo());
        UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(newAuth);

        return "redirect:/my_profile";
    }
    @PostMapping("/my_profile/credit")
    public String updateCredits(@RequestParam("credit") int newCredit, Model model) {

        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();

        Utilisateur utilisateurConnecte = utilisateurService.getUtilisateurByPseudo(currentAuth.getName());
        // Mettez à jour les crédits dans l'utilisateur
        utilisateurConnecte.setCredit(newCredit);
        utilisateurService.updateUtilisateur(utilisateurConnecte);

        model.addAttribute("user", utilisateurConnecte);  // Passer l'utilisateur mis à jour au modèle
        return "redirect:/my_profile";  // Redirection vers le profil après modification
    }
    @GetMapping("/my_profile/credit")
    public String creditEdit(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Utilisateur utilisateurConnecte = utilisateurService.getUtilisateurByPseudo(auth.getName());

        model.addAttribute("utilisateur", utilisateurConnecte); // Ajout de l'utilisateur au modèle pour afficher ses informations

        return "profile_editCredit"; // Redirige vers la page profile_editCredit.html
    }

    @PostMapping("/my_profile/delete")
    public String deleteMyProfile(Utilisateur utilisateur) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Utilisateur utilisateurConnecte = utilisateurService.getUtilisateurByPseudo(auth.getName());

        utilisateurService.deleteUtilisateur(utilisateurConnecte.getNoUtilisateur());

        return "redirect:/";
    }

    @GetMapping("/my_profile/forgot_password")
    public String forgotPassword(Utilisateur utilisateur) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Utilisateur utilisateurConnecte = utilisateurService.getUtilisateurByPseudo(auth.getName());

        return "forgotten_password";
    }
}