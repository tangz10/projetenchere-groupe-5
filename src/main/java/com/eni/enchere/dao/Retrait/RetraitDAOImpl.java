package com.eni.enchere.dao.Retrait;

import com.eni.enchere.bo.ArticleVendu;
import com.eni.enchere.bo.Retrait;


import java.util.HashMap;
import java.util.Map;

public class RetraitDAOImpl implements RetraitDAO {

    private final Map<Long, Retrait> retraits = new HashMap<>();

    public RetraitDAOImpl() {
        ArticleVendu article = new ArticleVendu();
        article.setNoArticle(1);
        article.setNom_article("Téléviseur");

        Retrait r1 = new Retrait();
        r1.setNoArticle(article);
        r1.setRue("10 avenue des Champs");
        r1.setCodePostal("75008");
        r1.setVille("Paris");

        retraits.put(article.getNoArticle(), r1);
    }

    @Override
    public void insert(Retrait retrait) {
        retraits.put(retrait.getNoArticle().getNoArticle(), retrait);
    }

    @Override
    public Retrait selectByArticle(long noArticle) {
        return retraits.get(noArticle);
    }
}
