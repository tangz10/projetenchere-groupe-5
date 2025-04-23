package com.eni.enchere.services;

import com.eni.enchere.bo.Categorie;
import com.eni.enchere.bo.Utilisateur;
import com.eni.enchere.dao.Categorie.CategorieDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategorieService {

    @Autowired
    private CategorieDAO categorieDAO;

    public Categorie getCategorieById(long id) {
        try {
            return categorieDAO.selectById(id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Categorie> getAllCategories() {
        return categorieDAO.selectAll();
    }

    public void insertCategorie(Categorie categorie) {
        categorieDAO.insert(categorie);
    }

    public void updateCategorie(Categorie categorie) {
        categorieDAO.update(categorie);
    }

    public void deleteCategorie(long id) { categorieDAO.delete(id); }
}
