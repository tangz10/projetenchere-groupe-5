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

    @GetMapping("/register")
    public String viewRegister() {
        return "register";
    }

    @GetMapping("/forgottenPassword")
    public String viewForgottenPassword() {
        return "forgotten_password";
    }

    @PostMapping("/register")
    public String registerUser(Utilisateur utilisateur, Model model) {
        if (utilisateurService.getUtilisateurByPseudo(utilisateur.getPseudo()) != null) {
            model.addAttribute("message", "Ce pseudo est déjà utilisé");

            return "register";
        }

        if (utilisateurService.getUtilisateurByEmail(utilisateur.getEmail()) != null) {
            model.addAttribute("message", "Cet email est déjà utilisé");

            return "register";
        }

        utilisateur.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));
        utilisateur.setCredit(0);
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
