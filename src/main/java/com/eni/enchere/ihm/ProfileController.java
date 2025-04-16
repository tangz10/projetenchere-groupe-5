package com.eni.enchere.ihm;

import com.eni.enchere.bo.Utilisateur;
import com.eni.enchere.services.CustomUserDetailsService;
import com.eni.enchere.services.UtilisateurService;
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

    @PostMapping("/my_profile/edit")
    public String editMyProfile(Utilisateur utilisateur) {
        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();

        Utilisateur utilisateurConnecte = utilisateurService.getUtilisateurDAO(currentAuth.getName());

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

    @PostMapping("/my_profile/delete")
    public String deleteMyProfile(Utilisateur utilisateur) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Utilisateur utilisateurConnecte = utilisateurService.getUtilisateurDAO(auth.getName());

        utilisateurService.deleteUtilisateur(utilisateurConnecte.getNoUtilisateur());

        return "redirect:/";
    }
}