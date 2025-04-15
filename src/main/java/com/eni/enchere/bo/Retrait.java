package com.eni.enchere.bo;

public class Retrait {
    private String rue;
    private String codePostal;
    private String ville;
    private ArticleVendu noArticle;

    public Retrait() {
    }

    public Retrait(String rue, String codePostal, String ville, ArticleVendu noArticle) {
        this.rue = rue;
        this.codePostal = codePostal;
        this.ville = ville;
        this.noArticle = noArticle;
    }

    public String getRue() {
        return rue;
    }

    public void setRue(String rue) {
        this.rue = rue;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public ArticleVendu getNoArticle() {
        return noArticle;
    }

    public void setNoArticle(ArticleVendu noArticle) {
        this.noArticle = noArticle;
    }
}
