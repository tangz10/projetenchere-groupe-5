package com.eni.enchere.bo;

public class Categorie {
    private long noCategorie;
    private String libelle;

    public Categorie() {
    }

    public Categorie(long noCategorie, String libelle) {
        this.noCategorie = noCategorie;
        this.libelle = libelle;
    }

    public long getNoCategorie() {
        return noCategorie;
    }

    public void setNoCategorie(long noCategorie) {
        this.noCategorie = noCategorie;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
}
