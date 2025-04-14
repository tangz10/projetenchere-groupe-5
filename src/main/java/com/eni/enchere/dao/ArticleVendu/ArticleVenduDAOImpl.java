package com.eni.enchere.dao.ArticleVendu;

import com.eni.enchere.bo.ArticleVendu;
import com.eni.enchere.bo.Categorie;
import com.eni.enchere.bo.Utilisateur;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArticleVenduDAOImpl implements ArticleVenduDAO {
    private final Map<Long, ArticleVendu> articles = new HashMap<>();
    private long currentId = 1;

    public ArticleVenduDAOImpl() {
        // Données fictives
        Utilisateur u1 = new Utilisateur();
        u1.setnoUtilisateur(1);
        u1.setPseudo("Jean75");

        Categorie c1 = new Categorie();
        c1.setNoCategorie(1);
        c1.setLibelle("Informatique");

        ArticleVendu a1 = new ArticleVendu();
        a1.setNoArticle(currentId++);
        a1.setNom_article("Ordinateur Portable");
        a1.setDescription("En parfait état");
        a1.setDebut_encheres(LocalDate.of(2025, 4, 1));
        a1.setFin_encheres(LocalDate.of(2025, 4, 30));
        a1.setPrixInitial(300);
        a1.setPrixVente(400);
        a1.setNoUtilisateur(u1);
        a1.setNoCategorie(c1);

        articles.put(a1.getNoArticle(), a1);
    }

    @Override
    public void insert(ArticleVendu article) {
        article.setNoArticle(currentId++);
        articles.put(article.getNoArticle(), article);
    }

    @Override
    public List<ArticleVendu> selectAll() {
        return new ArrayList<>(articles.values());
    }

    @Override
    public ArticleVendu selectById(long id) {
        return articles.get(id);
    }

    @Override
    public List<ArticleVendu> selectByUtilisateur(long noUtilisateur) {
        List<ArticleVendu> result = new ArrayList<>();
        for (ArticleVendu a : articles.values()) {
            if (a.getNoUtilisateur() != null && a.getNoUtilisateur().getnoUtilisateur() == noUtilisateur) {
                result.add(a);
            }
        }
        return result;
    }
}
