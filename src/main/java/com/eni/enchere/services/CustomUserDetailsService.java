package com.eni.enchere.services;

import com.eni.enchere.bo.Utilisateur;
import com.eni.enchere.dao.Utilisateur.UtilisateurDAOImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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

        return org.springframework.security.core.userdetails.User.builder()
                .username(utilisateur.getPseudo())
                .password(utilisateur.getMotDePasse())
                .build();
    }
}