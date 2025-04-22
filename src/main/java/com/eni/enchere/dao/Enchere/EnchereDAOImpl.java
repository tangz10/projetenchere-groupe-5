package com.eni.enchere.dao.Enchere;

import com.eni.enchere.bo.ArticleVendu;
import com.eni.enchere.bo.Enchere;
import com.eni.enchere.bo.Utilisateur;
import com.eni.enchere.dao.ArticleVendu.ArticleVenduDAO;
import com.eni.enchere.dao.Utilisateur.UtilisateurDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public class EnchereDAOImpl implements EnchereDAO {

    @Autowired
    private UtilisateurDAO utilisateurDAO;

    @Autowired
    private ArticleVenduDAO articleVenduDAO;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Enchere> enchereRowMapper = (rs, rowNum) -> {
        Enchere e = new Enchere();

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNoUtilisateur(rs.getInt("no_utilisateur"));

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
        String sql = "INSERT INTO encheres (no_utilisateur, no_article, date_enchere, montant_enchere) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                enchere.getNoUtilisateur().getNoUtilisateur(),
                enchere.getNoArticle().getNoArticle(),
                Date.valueOf(enchere.getDateEnchere()),
                enchere.getMontantEnchere());
    }

    @Override
    public void update(Enchere enchere) {
        String sql = "UPDATE encheres SET montant_enchere = ?, date_enchere = ? WHERE no_utilisateur = ? AND no_article = ?";
        jdbcTemplate.update(sql,
                enchere.getMontantEnchere(),
                Date.valueOf(enchere.getDateEnchere()),
                enchere.getNoUtilisateur().getNoUtilisateur(),
                enchere.getNoArticle().getNoArticle()
        );
    }

    @Override
    public boolean enchereExiste(long noUtilisateur, long noArticle) {
        String sql = "SELECT COUNT(*) FROM encheres WHERE no_utilisateur = ? AND no_article = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, noUtilisateur, noArticle);
        return count != null && count > 0;
    }



    @Override
    public List<Enchere> selectByArticle(long noArticle) {
        String sql = "SELECT * FROM encheres WHERE no_article = ?";
        return jdbcTemplate.query(sql, enchereRowMapper, noArticle);
    }

    @Override
    public List<Enchere> selectByUtilisateur(long noUtilisateur) {
        String sql = "SELECT * FROM encheres WHERE no_utilisateur = ?";
        return jdbcTemplate.query(sql, enchereRowMapper, noUtilisateur);
    }

    @Override
    public Enchere findBestOfferByArticleId(long noArticle) {
        String sql = """
        SELECT * FROM encheres 
        WHERE no_article = ? 
        ORDER BY montant_enchere DESC 
        LIMIT 1
    """;

        List<Enchere> result = jdbcTemplate.query(sql, (rs, rowNum) -> {
            Enchere e = new Enchere();

            // récupérer l'utilisateur complet
            int idUtilisateur = rs.getInt("no_utilisateur");
            Utilisateur utilisateur = utilisateurDAO.selectById(idUtilisateur);
            e.setNoUtilisateur(utilisateur);

            // récupérer l'article complet
            int idArticle = rs.getInt("no_article");
            ArticleVendu article = articleVenduDAO.selectById(idArticle);
            e.setNoArticle(article);

            e.setDateEnchere(rs.getDate("date_enchere").toLocalDate());
            e.setMontantEnchere(rs.getInt("montant_enchere"));

            return e;
        }, noArticle);

        return result.isEmpty() ? null : result.get(0);
    }
    @Override
    public List<Enchere> findAllOffersByArticleId(long noArticle) {
        String sql = """
    SELECT * FROM encheres 
    WHERE no_article = ? 
    ORDER BY montant_enchere DESC
    """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Enchere e = new Enchere();

            // récupérer l'utilisateur complet
            int idUtilisateur = rs.getInt("no_utilisateur");
            Utilisateur utilisateur = utilisateurDAO.selectById(idUtilisateur);
            e.setNoUtilisateur(utilisateur);

            // récupérer l'article complet
            int idArticle = rs.getInt("no_article");
            ArticleVendu article = articleVenduDAO.selectById(idArticle);
            e.setNoArticle(article);

            e.setDateEnchere(rs.getDate("date_enchere").toLocalDate());
            e.setMontantEnchere(rs.getInt("montant_enchere"));

            return e;
        }, noArticle);
    }

    @Override
    public List<Enchere> selectByUtilisateurEtArticle(long noArticle, long noUtilisateur) {
        String sql = """
        SELECT * FROM encheres 
        WHERE no_article = ? AND no_utilisateur = ?        
    """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Enchere e = new Enchere();

            // récupérer l'utilisateur complet
            int idUtilisateur = rs.getInt("no_utilisateur");
            Utilisateur utilisateur = utilisateurDAO.selectById(idUtilisateur);
            e.setNoUtilisateur(utilisateur);

            // récupérer l'article complet
            int idArticle = rs.getInt("no_article");
            ArticleVendu article = articleVenduDAO.selectById(idArticle);
            e.setNoArticle(article);

            e.setDateEnchere(rs.getDate("date_enchere").toLocalDate());
            e.setMontantEnchere(rs.getInt("montant_enchere"));

            return e;
        }, noArticle,noUtilisateur);
    }

}
