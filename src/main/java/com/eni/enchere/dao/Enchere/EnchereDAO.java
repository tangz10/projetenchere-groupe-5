package com.eni.enchere.dao.Enchere;

import com.eni.enchere.bo.Enchere;
import java.util.List;

public interface EnchereDAO {
    void insert(Enchere enchere);
    List<Enchere> selectByArticle(long noArticle);
    List<Enchere> selectByUtilisateur(long noUtilisateur);
}
