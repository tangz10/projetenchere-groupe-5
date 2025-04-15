package com.eni.enchere.services;

import com.eni.enchere.bo.Categorie;
import com.eni.enchere.dao.Categorie.CategorieDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategorieService {

    @Autowired
    private CategorieDAO categorieDAO;

    public List<Categorie> getAllCategories() {
        return categorieDAO.selectAll();
    }
}
