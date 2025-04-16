package com.eni.enchere.services;

import com.eni.enchere.bo.ArticleVendu;
import com.eni.enchere.bo.Utilisateur;
import com.eni.enchere.dao.ArticleVendu.ArticleVenduDAO;
import com.eni.enchere.dao.Utilisateur.UtilisateurDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleVenduService {

    @Autowired
    private ArticleVenduDAO articleVenduDAO;

    public void insertArticleVendu(ArticleVendu articleVendu) {
        articleVenduDAO.insert(articleVendu);
    }
}
