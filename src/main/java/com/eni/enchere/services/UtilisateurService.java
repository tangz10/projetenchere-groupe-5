package com.eni.enchere.services;

import com.eni.enchere.bo.Utilisateur;
import com.eni.enchere.dao.Categorie.CategorieDAO;
import com.eni.enchere.dao.Utilisateur.UtilisateurDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class UtilisateurService {
    @Autowired
    private UtilisateurDAO utilisateurDAO;


    public Utilisateur getUtilisateurByPseudo(String pseudo) {
        try {
            return utilisateurDAO.selectByPseudo(pseudo);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Utilisateur getUtilisateurByEmail(String email) {
        try {
            return utilisateurDAO.selectByEmail(email);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void insertUtilisateur(Utilisateur utilisateur) {
        utilisateurDAO.insert(utilisateur);
    }

    public void updateUtilisateur(Utilisateur utilisateur) {
        utilisateurDAO.update(utilisateur);
    }

    public void deleteUtilisateur(long id) {
        utilisateurDAO.delete(id);
    }
}
