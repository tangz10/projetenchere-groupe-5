package com.eni.enchere.dao.Enchere;

import com.eni.enchere.bo.Enchere;

import java.util.List;

public interface EnchereDAO {
    void insert(Enchere enchere);
    void update(Enchere enchere);
    boolean enchereExiste(long noUtilisateur, long noArticle);

    List<Enchere> selectByArticle(long noArticle);

    List<Enchere> selectByUtilisateur(long noUtilisateur);
    Enchere findBestOfferByArticleId(long noArticle);
    List<Enchere> selectByUtilisateurEtArticle(long noArticle,long noUtilisateur);

}
