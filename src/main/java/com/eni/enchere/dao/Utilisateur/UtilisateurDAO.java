package com.eni.enchere.dao.Utilisateur;


import com.eni.enchere.bo.Utilisateur;
import java.util.List;

public interface UtilisateurDAO {
    void insert(Utilisateur utilisateur);
    void update(Utilisateur utilisateur);
    void delete(long id);
    Utilisateur selectById(long id);
    List<Utilisateur> selectAll();
    Utilisateur selectByPseudo(String pseudo);
}

