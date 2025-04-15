package com.eni.enchere.bo;


import java.time.LocalDate;

public class Enchere {
    private Utilisateur noUtilisateur;
    private ArticleVendu noArticle;
    private LocalDate dateEnchere;
    private long montantEnchere;

    public Enchere() {
    }

    public Enchere(Utilisateur noUtilisateur, ArticleVendu noArticle, LocalDate dateEnchere, long montantEnchere) {
        this.noUtilisateur = noUtilisateur;
        this.noArticle = noArticle;
        this.dateEnchere = dateEnchere;
        this.montantEnchere = montantEnchere;
    }

    public Utilisateur getNoUtilisateur() {
        return noUtilisateur;
    }

    public void setNoUtilisateur(Utilisateur noUtilisateur) {
        this.noUtilisateur = noUtilisateur;
    }

    public ArticleVendu getNoArticle() {
        return noArticle;
    }

    public void setNoArticle(ArticleVendu noArticle) {
        this.noArticle = noArticle;
    }

    public LocalDate getDateEnchere() {
        return dateEnchere;
    }

    public void setDateEnchere(LocalDate dateEnchere) {
        this.dateEnchere = dateEnchere;
    }

    public long getMontantEnchere() {
        return montantEnchere;
    }

    public void setMontantEnchere(long montantEnchere) {
        this.montantEnchere = montantEnchere;
    }
}
