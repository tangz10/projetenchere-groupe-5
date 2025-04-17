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

    public void ajouterEnchere(long idUtilisateur, long idArticle, long montant) {
        Utilisateur utilisateur = utilisateurDAO.selectById(idUtilisateur);
        ArticleVendu article = articleDAO.selectById(idArticle);

        Enchere enchere = new Enchere();
        enchere.setNoUtilisateur(utilisateur);
        enchere.setNoArticle(article);
        enchere.setDateEnchere(LocalDate.now());
        enchere.setMontantEnchere(montant);

        enchereDAO.insert(enchere);
    }
    public Enchere getMeilleureEnchereParArticleId(long idArticle) {
        return enchereDAO.findBestOfferByArticleId(idArticle);
    }

    public List<Enchere> getClassementEncheres(long articleId) {
        List<Enchere> encheres = enchereDAO.selectByArticle(articleId);
        encheres.sort((e1, e2) -> Long.compare(e2.getMontantEnchere(), e1.getMontantEnchere())); // tri décroissant
        return encheres;
    }

    public List<Enchere>getEncheresByArticleEtUtilisateur(long noArticle, long noUtilisateur) {
        return enchereDAO.selectByUtilisateurEtArticle(noArticle,noUtilisateur);
    }


}
