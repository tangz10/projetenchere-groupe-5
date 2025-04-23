package com.eni.enchere.dao.Categorie;

import com.eni.enchere.bo.Categorie;
import com.eni.enchere.bo.Utilisateur;

import java.util.*;

public interface CategorieDAO {
    List<Categorie> selectAll();
    Categorie selectById(long id);
    void insert(Categorie categorie);
    void update(Categorie categorie);
    void delete(long id);
}
