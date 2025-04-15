package com.eni.enchere.dao.Enchere;

import com.eni.enchere.bo.ArticleVendu;
import com.eni.enchere.bo.Enchere;
import com.eni.enchere.bo.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public class EnchereDAOImpl implements EnchereDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Enchere> enchereRowMapper = (rs, rowNum) -> {
        Enchere e = new Enchere();

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setnoUtilisateur(rs.getInt("no_utilisateur"));

        ArticleVendu article = new ArticleVendu();
        article.setNoArticle(rs.getInt("no_article"));

        e.setNoUtilisateur(utilisateur);
        e.setNoArticle(article);
        e.setDateEnchere(rs.getDate("date_enchere").toLocalDate());
        e.setMontantEnchere(rs.getInt("montant_enchere"));

        return e;
    };

    @Override
    public void insert(Enchere enchere) {
        String sql = "INSERT INTO Encheres (no_utilisateur, no_article, date_enchere, montant_enchere) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                enchere.getNoUtilisateur().getnoUtilisateur(),
                enchere.getNoArticle().getNoArticle(),
                Date.valueOf(enchere.getDateEnchere()),
                enchere.getMontantEnchere());
    }

    @Override
    public List<Enchere> selectByArticle(long noArticle) {
        String sql = "SELECT * FROM Encheres WHERE no_article = ?";
        return jdbcTemplate.query(sql, enchereRowMapper, noArticle);
    }

    @Override
    public List<Enchere> selectByUtilisateur(long noUtilisateur) {
        String sql = "SELECT * FROM Encheres WHERE no_utilisateur = ?";
        return jdbcTemplate.query(sql, enchereRowMapper, noUtilisateur);
    }
}
