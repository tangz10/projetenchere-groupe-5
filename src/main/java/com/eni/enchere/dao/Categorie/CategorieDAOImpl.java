package com.eni.enchere.dao.Categorie;

import com.eni.enchere.bo.Categorie;

import java.util.*;

public class CategorieDAOImpl implements CategorieDAO {
    private final Map<Long, Categorie> categories = new HashMap<>();

    public CategorieDAOImpl() {
        Categorie c1 = new Categorie(1, "Informatique");
        Categorie c2 = new Categorie(2, "Meubles");
        Categorie c3 = new Categorie(3, "Sport");

        categories.put(c1.getNoCategorie(), c1);
        categories.put(c2.getNoCategorie(), c2);
        categories.put(c3.getNoCategorie(), c3);
    }

    @Override
    public List<Categorie> selectAll() {
        return new ArrayList<>(categories.values());
    }

    @Override
    public Categorie selectById(long id) {
        return categories.get(id);
    }
}
