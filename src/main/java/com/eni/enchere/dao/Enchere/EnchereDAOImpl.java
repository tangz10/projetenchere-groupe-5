package com.eni.enchere.dao.Enchere;

import com.eni.enchere.bo.ArticleVendu;
import com.eni.enchere.bo.Enchere;
import com.eni.enchere.bo.Utilisateur;


import java.time.LocalDate;
import java.util.*;

public class EnchereDAOImpl implements EnchereDAO {

    private final List<Enchere> encheres = new ArrayList<>();

    public EnchereDAOImpl() {
        // Mock Utilisateur & Article
        Utilisateur u1 = new Utilisateur();
        u1.setnoUtilisateur(2);
        u1.setPseudo("Alice44");

        ArticleVendu a1 = new ArticleVendu();
        a1.setNoArticle(1);
        a1.setNom_article("Imprimante");

        Enchere e1 = new Enchere();
        e1.setNoUtilisateur(u1);
        e1.setNoArticle(a1);
        e1.setDateEnchere(LocalDate.now());
        e1.setMontantEnchere(150);

        encheres.add(e1);
    }

    @Override
    public void insert(Enchere enchere) {
        encheres.add(enchere);
    }

    @Override
    public List<Enchere> selectByArticle(long noArticle) {
        List<Enchere> result = new ArrayList<>();
        for (Enchere e : encheres) {
            if (e.getNoArticle() != null && e.getNoArticle().getNoArticle() == noArticle) {
                result.add(e);
            }
        }
        return result;
    }

    @Override
    public List<Enchere> selectByUtilisateur(long noUtilisateur) {
        List<Enchere> result = new ArrayList<>();
        for (Enchere e : encheres) {
            if (e.getNoUtilisateur() != null && e.getNoUtilisateur().getnoUtilisateur() == noUtilisateur) {
                result.add(e);
            }
        }
        return result;
    }
}
