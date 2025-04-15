package com.eni.enchere.services;

import com.eni.enchere.bo.Utilisateur;
import com.eni.enchere.dao.Utilisateur.UtilisateurDAOImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UtilisateurDAOImpl utilisateurDAO;

    public CustomUserDetailsService(UtilisateurDAOImpl utilisateurDAO) {
        this.utilisateurDAO = utilisateurDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Utilisateur utilisateur = utilisateurDAO.selectByPseudo(username);

        return org.springframework.security.core.userdetails.User.builder()
                .username(utilisateur.getPseudo())
                .password(utilisateur.getMotDePasse())
                .build();
    }
}