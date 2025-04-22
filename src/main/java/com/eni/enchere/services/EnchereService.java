package com.eni.enchere.services;

import com.eni.enchere.bo.ArticleVendu;
import com.eni.enchere.bo.Enchere;
import com.eni.enchere.bo.Utilisateur;
import com.eni.enchere.dao.ArticleVendu.ArticleVenduDAO;
import com.eni.enchere.dao.Enchere.EnchereDAO;
import com.eni.enchere.dao.Utilisateur.UtilisateurDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EnchereService {

    @Autowired
    private EnchereDAO enchereDAO;

    @Autowired
    private UtilisateurDAO utilisateurDAO;

    @Autowired
    private ArticleVenduDAO articleDAO;


    public void ajouterEnchere(long noUtilisateur, long noArticle, long montant) {
        Utilisateur nouvelEncherisseur = utilisateurDAO.selectById(noUtilisateur);
        ArticleVendu article = articleDAO.selectById(noArticle);

        // Récupération de l’ancienne meilleure enchère
        Enchere ancienneMeilleure = enchereDAO.findBestOfferByArticleId(noArticle);

        // ✅ Vérifie que le montant proposé est suffisant
        if (ancienneMeilleure != null && montant <= ancienneMeilleure.getMontantEnchere()) {
            throw new IllegalArgumentException("Votre offre doit être supérieure à la meilleure offre actuelle ("
                    + ancienneMeilleure.getMontantEnchere() + " pts).");
        }

        if (ancienneMeilleure == null && montant < article.getPrixInitial()) {
            throw new IllegalArgumentException("Votre offre doit être au moins égale à la mise à prix ("
                    + article.getPrixInitial() + " pts).");
        }

        // 💸 Remboursement éventuel
        if (ancienneMeilleure != null && ancienneMeilleure.getNoUtilisateur().getNoUtilisateur() == noUtilisateur) {
            nouvelEncherisseur.setCredit(nouvelEncherisseur.getCredit() + ancienneMeilleure.getMontantEnchere());
        } else if (ancienneMeilleure != null) {
            Utilisateur ancien = ancienneMeilleure.getNoUtilisateur();
            ancien.setCredit(ancien.getCredit() + ancienneMeilleure.getMontantEnchere());
            utilisateurDAO.update(ancien); // Mets à jour les crédits de l’ancien enchérisseur
        }

        // 🧮 Vérifie les crédits après remboursement éventuel
        if (nouvelEncherisseur.getCredit() < montant) {
            throw new IllegalArgumentException("Crédits insuffisants pour miser.");
        }

        // 🔻 Déduire les crédits
        nouvelEncherisseur.setCredit(nouvelEncherisseur.getCredit() - montant);
        utilisateurDAO.update(nouvelEncherisseur);

        // 📝 Enregistrer l’enchère
        Enchere enchere = new Enchere();
        enchere.setNoUtilisateur(nouvelEncherisseur);
        enchere.setNoArticle(article);
        enchere.setMontantEnchere(montant);
        enchere.setDateEnchere(LocalDate.now());

        if (enchereDAO.enchereExiste(noUtilisateur, noArticle)) {
            enchereDAO.update(enchere);
        } else {
            enchereDAO.insert(enchere);
        }
    }



    public Enchere getMeilleureEnchereParArticleId(long idArticle) {
        return enchereDAO.findBestOfferByArticleId(idArticle);
    }
    public List<Enchere> getEnchereByArticleId(long idArticle) {
        return enchereDAO.findAllOffersByArticleId(idArticle);
    }


    public List<Enchere> getClassementEncheres(long articleId) {
        List<Enchere> encheres = enchereDAO.selectByArticle(articleId);
        encheres.sort((e1, e2) -> Long.compare(e2.getMontantEnchere(), e1.getMontantEnchere())); // tri décroissant
        return encheres;
    }

    public List<Enchere>getEncheresByArticleEtUtilisateur(long noArticle, long noUtilisateur) {
        return enchereDAO.selectByUtilisateurEtArticle(noArticle,noUtilisateur);
    }

    public void deleteEnchere(long noArticle) {
        articleDAO.delete(noArticle);
    }


}
