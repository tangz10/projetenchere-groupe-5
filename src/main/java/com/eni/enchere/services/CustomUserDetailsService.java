package com.eni.enchere.services;

import com.eni.enchere.bo.Utilisateur;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UtilisateurService utilisateurService;

    public CustomUserDetailsService(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Utilisateur utilisateur;

        utilisateur = utilisateurService.getUtilisateurByPseudo(username);

        if (utilisateur == null) {
            utilisateur = utilisateurService.getUtilisateurByEmail(username);
        }

        if (utilisateur == null || !utilisateur.getIsActive()) {
            throw new UsernameNotFoundException("Utilisateur introuvable : " + username);
        }

        return org.springframework.security.core.userdetails.User.builder()
                .username(utilisateur.getPseudo())
                .password(utilisateur.getMotDePasse())
                .authorities(utilisateur.getIsAdmin() ? "ADMIN" : "USER")
                .build();
    }
}