package com.eni.enchere.bo;

import java.time.LocalDate;


public class ArticleVendu {
    private long noArticle;
    private String nom_article;
    private String description;
    private LocalDate debut_encheres;
    private LocalDate fin_encheres;
    private long prixInitial;
    private long prixVente;
    private String url;
    private Utilisateur noUtilisateur;
    private Categorie noCategorie;

    public ArticleVendu() {
    }

    public ArticleVendu(String nom_article, String description, LocalDate debut_encheres, LocalDate fin_encheres, long prixInitial, long prixVente, Utilisateur noUtilisateur, Categorie noCategorie, String url) {
        this.nom_article = nom_article;
        this.description = description;
        this.debut_encheres = debut_encheres;
        this.fin_encheres = fin_encheres;
        this.prixInitial = prixInitial;
        this.prixVente = prixVente;
        this.noUtilisateur = noUtilisateur;
        this.noCategorie = noCategorie;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getNoArticle() {
        return noArticle;
    }

    public void setNoArticle(long noArticle) {
        this.noArticle = noArticle;
    }

    public Categorie getNoCategorie() {
        return noCategorie;
    }

    public void setNoCategorie(Categorie noCategorie) {
        this.noCategorie = noCategorie;
    }

    public Utilisateur getNoUtilisateur() {
        return noUtilisateur;
    }

    public void setNoUtilisateur(Utilisateur noUtilisateur) {
        this.noUtilisateur = noUtilisateur;
    }

    public long getPrixVente() {
        return prixVente;
    }

    public void setPrixVente(long prixVente) {
        this.prixVente = prixVente;
    }

    public long getPrixInitial() {
        return prixInitial;
    }

    public void setPrixInitial(long prixInitial) {
        this.prixInitial = prixInitial;
    }

    public LocalDate getFin_encheres() {
        return fin_encheres;
    }

    public void setFin_encheres(LocalDate fin_encheres) {
        this.fin_encheres = fin_encheres;
    }

    public LocalDate getDebut_encheres() {
        return debut_encheres;
    }

    public void setDebut_encheres(LocalDate debut_encheres) {
        this.debut_encheres = debut_encheres;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNom_article() {
        return nom_article;
    }

    public void setNom_article(String nom_article) {
        this.nom_article = nom_article;
    }
}
