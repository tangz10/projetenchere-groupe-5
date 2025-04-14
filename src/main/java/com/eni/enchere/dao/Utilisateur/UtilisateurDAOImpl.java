package com.eni.enchere.dao.Utilisateur;

import com.eni.enchere.bo.Utilisateur;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UtilisateurDAOImpl implements UtilisateurDAO {

    private final Map<Long, Utilisateur> utilisateurs = new HashMap<>();
    private long currentId = 1;

    public UtilisateurDAOImpl() {
        // Données simulées
        Utilisateur u1 = new Utilisateur();
        u1.setNoutilisateur(currentId++);
        u1.setPseudo("JeanDu75");
        u1.setNom("Dupont");
        u1.setPrenom("Jean");
        u1.setEmail("jean@mail.com");
        u1.setTelephone("0601020304");
        u1.setRue("1 rue de Paris");
        u1.setCodePostal("75000");
        u1.setVille("Paris");
        u1.setMotDePasse("mdp123");
        u1.setCredit(100);
        u1.setIsadmin(false);

        utilisateurs.put(u1.getNoutilisateur(), u1);
    }

    @Override
    public void insert(Utilisateur utilisateur) {
        utilisateur.setNoutilisateur(currentId++);
        utilisateurs.put(utilisateur.getNoutilisateur(), utilisateur);
    }

    @Override
    public void update(Utilisateur utilisateur) {
        utilisateurs.put(utilisateur.getNoutilisateur(), utilisateur);
    }

    @Override
    public void delete(long id) {
        utilisateurs.remove(id);
    }

    @Override
    public Utilisateur selectById(long id) {
        return utilisateurs.get(id);
    }

    @Override
    public List<Utilisateur> selectAll() {
        return new ArrayList<>(utilisateurs.values());
    }

    @Override
    public Utilisateur selectByPseudo(String pseudo) {
        return utilisateurs.values().stream()
                .filter(u -> u.getPseudo().equalsIgnoreCase(pseudo))
                .findFirst()
                .orElse(null);
    }
}
