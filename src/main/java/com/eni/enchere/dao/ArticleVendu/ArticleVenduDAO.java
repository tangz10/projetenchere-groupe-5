package com.eni.enchere.dao.ArticleVendu;

import com.eni.enchere.bo.ArticleVendu;
import java.util.List;

public interface ArticleVenduDAO {
    void insert(ArticleVendu article);
    void update(ArticleVendu article);
    void delete(long id);
    List<ArticleVendu> selectAll();
    ArticleVendu selectById(long id);
    List<ArticleVendu> selectByUtilisateur(long noUtilisateur);
    List<ArticleVendu> selectByCategorie(long noCategorie);
    List<ArticleVendu> selectByName(String Name);
}
