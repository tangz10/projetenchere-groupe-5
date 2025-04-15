package com.eni.enchere.dao.Retrait;

import com.eni.enchere.bo.ArticleVendu;
import com.eni.enchere.bo.Retrait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RetraitDAOImpl implements RetraitDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Retrait> retraitRowMapper = (rs, rowNum) -> {
        Retrait retrait = new Retrait();
        ArticleVendu article = new ArticleVendu();
        article.setNoArticle(rs.getInt("no_article"));

        retrait.setNoArticle(article);
        retrait.setRue(rs.getString("rue"));
        retrait.setCodePostal(rs.getString("code_postal"));
        retrait.setVille(rs.getString("ville"));

        return retrait;
    };

    @Override
    public void insert(Retrait retrait) {
        String sql = "INSERT INTO Retraits (no_article, rue, code_postal, ville) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                retrait.getNoArticle().getNoArticle(),
                retrait.getRue(),
                retrait.getCodePostal(),
                retrait.getVille());
    }

    @Override
    public Retrait selectByArticle(long noArticle) {
        String sql = "SELECT * FROM Retraits WHERE no_article = ?";
        List<Retrait> result = jdbcTemplate.query(sql, retraitRowMapper, noArticle);
        return result.isEmpty() ? null : result.get(0);
    }
}
