package com.eni.enchere.dao.ArticleVendu;

import com.eni.enchere.bo.ArticleVendu;
import com.eni.enchere.bo.Categorie;
import com.eni.enchere.bo.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public class ArticleVenduDAOImpl implements ArticleVenduDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<ArticleVendu> articleRowMapper = (rs, rowNum) -> {
        ArticleVendu article = new ArticleVendu();
        article.setNoArticle(rs.getInt("no_article"));
        article.setNom_article(rs.getString("nom_article"));
        article.setDescription(rs.getString("description"));
        article.setDebut_encheres(rs.getDate("date_debut_encheres").toLocalDate());
        article.setFin_encheres(rs.getDate("date_fin_encheres").toLocalDate());
        article.setPrixInitial(rs.getInt("prix_initial"));
        article.setPrixVente(rs.getInt("prix_vente"));

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setnoUtilisateur(rs.getInt("no_utilisateur"));
        article.setNoUtilisateur(utilisateur);

        Categorie categorie = new Categorie();
        categorie.setNoCategorie(rs.getInt("no_categorie"));
        article.setNoCategorie(categorie);

        return article;
    };

    @Override
    public void insert(ArticleVendu article) {
        String sql = "INSERT INTO ArticlesVendus (nom_article, description, date_debut_encheres, date_fin_encheres, prix_initial, prix_vente, no_utilisateur, no_categorie) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                article.getNom_article(),
                article.getDescription(),
                Date.valueOf(article.getDebut_encheres()),
                Date.valueOf(article.getFin_encheres()),
                article.getPrixInitial(),
                article.getPrixVente(),
                article.getNoUtilisateur().getnoUtilisateur(),
                article.getNoCategorie().getNoCategorie()
        );
    }

    @Override
    public List<ArticleVendu> selectAll() {
        String sql = "SELECT * FROM ArticlesVendus";
        return jdbcTemplate.query(sql, articleRowMapper);
    }

    @Override
    public ArticleVendu selectById(long id) {
        String sql = "SELECT * FROM ArticlesVendus WHERE no_article = ?";
        List<ArticleVendu> result = jdbcTemplate.query(sql, articleRowMapper, id);
        return result.isEmpty() ? null : result.get(0);
    }

    @Override
    public List<ArticleVendu> selectByUtilisateur(long noUtilisateur) {
        String sql = "SELECT * FROM ArticlesVendus WHERE no_utilisateur = ?";
        return jdbcTemplate.query(sql, articleRowMapper, noUtilisateur);
    }
}
