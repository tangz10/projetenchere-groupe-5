package com.eni.enchere.ihm;

import com.eni.enchere.bo.Utilisateur;
import com.eni.enchere.services.UtilisateurService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final UtilisateurService utilisateurService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UtilisateurService utilisateurService, PasswordEncoder passwordEncoder) {
        this.utilisateurService = utilisateurService;
        this.passwordEncoder = passwordEncoder;
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
    public String changePassword(Utilisateur utilisateur, RedirectAttributes redirectAttributes) {
        if (!utilisateur.getMotDePasse().equals(utilisateur.getConfirmationMotDePasse())) {
            redirectAttributes.addFlashAttribute("message", "Les mots de passe ne sont pas identiques");

            return "redirect:/forgotten_password?error";
        }

        Utilisateur utilisateurToUpdate = utilisateurService.getUtilisateurByEmail(utilisateur.getEmail());

        if (utilisateurToUpdate == null) {
            redirectAttributes.addFlashAttribute("message", "Email incorrect");

            return "redirect:/forgotten_password?error";
        }

        utilisateurToUpdate.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));
        utilisateurService.updateUtilisateur(utilisateurToUpdate);

        return "redirect:/login";
    }

    @PostMapping("/register")
    public String registerUser(Utilisateur utilisateur, RedirectAttributes redirectAttributes) {
        if (!utilisateur.getMotDePasse().equals(utilisateur.getConfirmationMotDePasse())) {
            redirectAttributes.addFlashAttribute("message", "Les mots de passe ne sont pas identiques");

            return "redirect:/register?error";
        }

        if (utilisateurService.getUtilisateurByPseudo(utilisateur.getPseudo()) != null) {
            redirectAttributes.addFlashAttribute("message", "Ce pseudo est déjà utilisé");

            return "redirect:/register?error";
        }

        if (utilisateurService.getUtilisateurByEmail(utilisateur.getEmail()) != null) {
            redirectAttributes.addFlashAttribute("message", "Cet email est déjà utilisé");

            return "redirect:/register?error";
        }

        utilisateur.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));
        utilisateurService.insertUtilisateur(utilisateur);

        return "redirect:/login?registered";
    }

}
