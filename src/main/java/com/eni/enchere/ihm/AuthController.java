package com.eni.enchere.ihm;

import com.eni.enchere.bo.Utilisateur;
import com.eni.enchere.services.CustomUserDetailsService;
import com.eni.enchere.services.UtilisateurService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final UtilisateurService utilisateurService;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService userDetailsService;

    public AuthController(UtilisateurService utilisateurService, PasswordEncoder passwordEncoder, CustomUserDetailsService userDetailsService) {
        this.utilisateurService = utilisateurService;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/login")
    public String viewLogin() {
        return "login";
    }

    @GetMapping("/forgotten_password")
    public String viewForgottenPassword() {
        return "forgotten_password";
    }

    @GetMapping("/register")
    public String viewRegister() {
        return "register";
    }

    @PostMapping("/change_password")
    public String changePassword(Utilisateur utilisateur, Model model) {
        Utilisateur utilisateurToUpdate = utilisateurService.getUtilisateurByEmail(utilisateur.getEmail());

        if (utilisateurToUpdate == null) {
            model.addAttribute("message", "Email incorrect");

            return "forgotten_password";
        }

        utilisateurToUpdate.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));
        utilisateurService.updateUtilisateur(utilisateurToUpdate);

        return "redirect:/login";
    }

    @PostMapping("/register")
    public String registerUser(Utilisateur utilisateur, Model model) {
        if (utilisateurService.getUtilisateurByPseudo(utilisateur.getPseudo()) != null) {
            model.addAttribute("message", "Ce pseudo est déjà utilisé");

            return "register?error";
        }

        if (utilisateurService.getUtilisateurByEmail(utilisateur.getEmail()) != null) {
            model.addAttribute("message", "Cet email est déjà utilisé");

            return "register?error";
        }

        utilisateur.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));
        utilisateurService.insertUtilisateur(utilisateur);

        /*
        UserDetails userDetails = userDetailsService.loadUserByUsername(utilisateur.getPseudo());
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        auth.setAuthenticated(true);

        SecurityContextHolder.getContext().setAuthentication(auth);
        */

        return "redirect:/login?registered";
    }

}
