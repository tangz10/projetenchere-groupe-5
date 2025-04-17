package com.eni.enchere.dao.Categorie;

import com.eni.enchere.bo.Categorie;

import java.util.*;

public interface CategorieDAO {
    List<Categorie> selectAll();
    Categorie selectById(long id);
}
