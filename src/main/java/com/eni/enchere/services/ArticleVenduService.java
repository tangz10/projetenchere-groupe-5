package com.eni.enchere.services;

import com.eni.enchere.bo.ArticleVendu;
import com.eni.enchere.bo.Utilisateur;
import com.eni.enchere.dao.ArticleVendu.ArticleVenduDAO;
import com.eni.enchere.dao.Utilisateur.UtilisateurDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleVenduService {

    @Autowired
    private ArticleVenduDAO articleVenduDAO;

    public void insertArticleVendu(ArticleVendu articleVendu) {
        articleVenduDAO.insert(articleVendu);
    }
    public void updateArticleVendu(ArticleVendu articleVendu){
        articleVenduDAO.update(articleVendu);
    }

    public List<ArticleVendu> getAllArticleVendu() {return articleVenduDAO.selectAll();}

    public List<ArticleVendu> getAllArticleVenduByName(String Name) {return articleVenduDAO.selectByName(Name);}
    public ArticleVendu getArticleVenduById(long id) {return articleVenduDAO.selectById(id);}

    public List<ArticleVendu> getArticleVenduByCategorie(long noCategorie) {return articleVenduDAO.selectByCategorie(noCategorie);}
    public List<ArticleVendu> getArticleVenduByUser(long noUtilisateur) {return articleVenduDAO.selectByUtilisateur(noUtilisateur);}
}
