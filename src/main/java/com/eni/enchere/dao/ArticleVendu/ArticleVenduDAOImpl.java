package com.eni.enchere.dao.ArticleVendu;

import com.eni.enchere.bo.ArticleVendu;
import com.eni.enchere.bo.Categorie;
import com.eni.enchere.bo.Utilisateur;
import com.eni.enchere.dao.Categorie.CategorieDAO;
import com.eni.enchere.dao.Utilisateur.UtilisateurDAO;
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

    @Autowired
    private UtilisateurDAO utilisateurDAO;

    @Autowired
    private CategorieDAO categorieDAO;

    private final RowMapper<ArticleVendu> articleRowMapper = (rs, rowNum) -> {
        ArticleVendu article = new ArticleVendu();
        article.setNoArticle(rs.getInt("no_article"));
        article.setNom_article(rs.getString("nom_article"));
        article.setDescription(rs.getString("description"));
        article.setDebut_encheres(rs.getDate("date_debut_encheres").toLocalDate());
        article.setFin_encheres(rs.getDate("date_fin_encheres").toLocalDate());
        article.setPrixInitial(rs.getInt("prix_initial"));
        article.setPrixVente(rs.getInt("prix_vente"));
        article.setUrl(rs.getString("url"));

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNoUtilisateur(rs.getInt("no_utilisateur"));
        article.setNoUtilisateur(utilisateur);

        Categorie categorie = new Categorie();
        categorie.setNoCategorie(rs.getInt("no_categorie"));
        article.setNoCategorie(categorie);

        return article;
    };

    @Override
    public void insert(ArticleVendu article) {
        if (article == null || article.getNom_article() == null || article.getNom_article().isEmpty()) {
            throw new IllegalArgumentException("Le nom de l'article est obligatoire");
        }
        if (article.getNoUtilisateur() == null) {
            throw new IllegalArgumentException("L'utilisateur connecté est obligatoire");
        }
        if (article.getNoCategorie() == null) {
            throw new IllegalArgumentException("La catégorie est obligatoire");
        }

        // Vérification des dates, si elles sont null, on les met à null pour les insérer dans la base de données
        java.sql.Date dateDebut = (article.getDebut_encheres() != null) ? java.sql.Date.valueOf(article.getDebut_encheres()) : null;
        java.sql.Date dateFin = (article.getFin_encheres() != null) ? java.sql.Date.valueOf(article.getFin_encheres()) : null;

        String sql = "INSERT INTO articles_vendus(nom_article, description, date_debut_encheres, date_fin_encheres, prix_initial, prix_vente, no_utilisateur, no_categorie, url) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?,?)";

        jdbcTemplate.update(sql,
                article.getNom_article(),
                article.getDescription(),
                dateDebut,  // Si null, la base de données devrait accepter
                dateFin,    // Idem
                article.getPrixInitial(),
                article.getPrixVente(),
                article.getNoUtilisateur() != null ? article.getNoUtilisateur().getNoUtilisateur() : null, // Assure que ce n'est pas null
                article.getNoCategorie() != null ? article.getNoCategorie().getNoCategorie() : null, // Idem pour la catégorie
                article.getUrl()
        );
    }

    @Override
    public void update(ArticleVendu article) {
        if (article == null || article.getNom_article() == null || article.getNom_article().isEmpty()) {
            throw new IllegalArgumentException("Le nom de l'article est obligatoire");
        }
        if (article.getNoUtilisateur() == null) {
            throw new IllegalArgumentException("L'utilisateur connecté est obligatoire");
        }
        if (article.getNoCategorie() == null) {
            throw new IllegalArgumentException("La catégorie est obligatoire");
        }

        // Conversion des LocalDate vers java.sql.Date
        java.sql.Date dateDebut = (article.getDebut_encheres() != null) ? java.sql.Date.valueOf(article.getDebut_encheres()) : null;
        java.sql.Date dateFin = (article.getFin_encheres() != null) ? java.sql.Date.valueOf(article.getFin_encheres()) : null;

        String sql = "UPDATE articles_vendus " +
                "SET nom_article = ?, description = ?, date_debut_encheres = ?, date_fin_encheres = ?, " +
                "prix_initial = ?, prix_vente = ?, no_utilisateur = ?, no_categorie = ?, url = ? " +
                "WHERE no_article = ?";

        jdbcTemplate.update(sql,
                article.getNom_article(),
                article.getDescription(),
                dateDebut,
                dateFin,
                article.getPrixInitial(),
                article.getPrixVente(),
                article.getNoUtilisateur().getNoUtilisateur(),
                article.getNoCategorie().getNoCategorie(),
                article.getUrl(),
                article.getNoArticle()
        );
    }


    @Override
    public void delete(long id) {
        jdbcTemplate.update("DELETE FROM articles_vendus WHERE no_article = ?", id);
    }


    @Override
    public List<ArticleVendu> selectAll() {
        String sql = "SELECT * FROM articles_vendus";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            ArticleVendu article = new ArticleVendu();
            article.setNoArticle(rs.getInt("no_article"));
            article.setNom_article(rs.getString("nom_article"));
            article.setDescription(rs.getString("description"));
            article.setDebut_encheres(rs.getDate("date_debut_encheres").toLocalDate());
            article.setFin_encheres(rs.getDate("date_fin_encheres").toLocalDate());
            article.setPrixInitial(rs.getLong("prix_initial"));
            article.setPrixVente(rs.getLong("prix_vente"));
            article.setUrl(rs.getString("url"));

            int idUtilisateur = rs.getInt("no_utilisateur");
            Utilisateur utilisateur = utilisateurDAO.selectById(idUtilisateur);
            article.setNoUtilisateur(utilisateur);

            int idCategorie = rs.getInt("no_categorie");
            Categorie categorie = categorieDAO.selectById(idCategorie);
            article.setNoCategorie(categorie);

            return article;
        });
    }

    @Override
    public ArticleVendu selectById(long id) {
        String sql = "SELECT * FROM articles_vendus WHERE no_article = ?";

        List<ArticleVendu> result = jdbcTemplate.query(sql, (rs, rowNum) -> {
            ArticleVendu article = new ArticleVendu();
            article.setNoArticle(rs.getInt("no_article"));
            article.setNom_article(rs.getString("nom_article"));
            article.setDescription(rs.getString("description"));
            article.setDebut_encheres(rs.getDate("date_debut_encheres").toLocalDate());
            article.setFin_encheres(rs.getDate("date_fin_encheres").toLocalDate());
            article.setPrixInitial(rs.getLong("prix_initial"));
            article.setPrixVente(rs.getLong("prix_vente"));
            article.setUrl(rs.getString("url"));

            int idUtilisateur = rs.getInt("no_utilisateur");
            Utilisateur utilisateur = utilisateurDAO.selectById(idUtilisateur);
            article.setNoUtilisateur(utilisateur);

            int idCategorie = rs.getInt("no_categorie");
            Categorie categorie = categorieDAO.selectById(idCategorie);
            article.setNoCategorie(categorie);

            return article;
        }, id);

        return result.isEmpty() ? null : result.get(0);
    }


    @Override
    public List<ArticleVendu> selectByUtilisateur(long noUtilisateur) {
        String sql = "SELECT * FROM articles_vendus WHERE no_utilisateur = ?";
        return jdbcTemplate.query(sql, new Object[]{noUtilisateur}, (rs, rowNum) -> {
            ArticleVendu article = new ArticleVendu();
            article.setNoArticle(rs.getLong("no_article"));
            article.setNom_article(rs.getString("nom_article"));
            article.setDescription(rs.getString("description"));
            article.setDebut_encheres(rs.getDate("date_debut_encheres").toLocalDate());
            article.setFin_encheres(rs.getDate("date_fin_encheres").toLocalDate());
            article.setPrixInitial(rs.getLong("prix_initial"));
            article.setPrixVente(rs.getLong("prix_vente"));
            article.setUrl(rs.getString("url"));

            int idUtilisateur = rs.getInt("no_utilisateur");
            Utilisateur utilisateur = utilisateurDAO.selectById(idUtilisateur);
            article.setNoUtilisateur(utilisateur);

            int idCategorie = rs.getInt("no_categorie");
            Categorie categorie = categorieDAO.selectById(idCategorie);
            article.setNoCategorie(categorie);

            return article;
        });
    }


    @Override
    public List<ArticleVendu> selectByCategorie(long noCategorie) {
        String sql = "SELECT * FROM articles_vendus WHERE no_categorie = ?";
        return jdbcTemplate.query(sql, new Object[]{noCategorie}, (rs, rowNum) -> {
            // Récupération des informations de l'article
            ArticleVendu article = new ArticleVendu();
            article.setNoArticle(rs.getLong("no_article"));
            article.setNom_article(rs.getString("nom_article"));
            article.setDescription(rs.getString("description"));
            article.setDebut_encheres(rs.getDate("date_debut_encheres").toLocalDate());
            article.setFin_encheres(rs.getDate("date_fin_encheres").toLocalDate());
            article.setPrixInitial(rs.getLong("prix_initial"));
            article.setPrixVente(rs.getLong("prix_vente"));
            article.setUrl(rs.getString("url"));

            // Récupération des informations de l'utilisateur
            int idUtilisateur = rs.getInt("no_utilisateur");
            Utilisateur utilisateur = utilisateurDAO.selectById(idUtilisateur); // Assurez-vous que cette méthode existe dans UtilisateurDAO
            article.setNoUtilisateur(utilisateur);

            // Récupération des informations de la catégorie
            int idCategorie = rs.getInt("no_categorie");
            Categorie categorie = categorieDAO.selectById(idCategorie); // Assurez-vous que cette méthode existe dans CategorieDAO
            article.setNoCategorie(categorie);

            return article;
        });
    }


    @Override
    public List<ArticleVendu> selectByName(String Name) {
        String sql = "SELECT * FROM articles_vendus WHERE nom_article LIKE ?";
        return jdbcTemplate.query(sql, new Object[]{"%" + Name + "%"}, (rs, rowNum) -> {
            // Récupération des informations de l'article
            ArticleVendu article = new ArticleVendu();
            article.setNoArticle(rs.getLong("no_article"));
            article.setNom_article(rs.getString("nom_article"));
            article.setDescription(rs.getString("description"));
            article.setDebut_encheres(rs.getDate("date_debut_encheres").toLocalDate());
            article.setFin_encheres(rs.getDate("date_fin_encheres").toLocalDate());
            article.setPrixInitial(rs.getLong("prix_initial"));
            article.setPrixVente(rs.getLong("prix_vente"));
            article.setUrl(rs.getString("url"));

            // Récupération des informations de l'utilisateur
            int idUtilisateur = rs.getInt("no_utilisateur");
            Utilisateur utilisateur = utilisateurDAO.selectById(idUtilisateur); // Assurez-vous que cette méthode existe dans UtilisateurDAO
            article.setNoUtilisateur(utilisateur);

            // Récupération des informations de la catégorie
            int idCategorie = rs.getInt("no_categorie");
            Categorie categorie = categorieDAO.selectById(idCategorie); // Assurez-vous que cette méthode existe dans CategorieDAO
            article.setNoCategorie(categorie);

            return article;
        });
    }

}
