package com.eni.enchere.dao.Categorie;

import com.eni.enchere.bo.Categorie;
import com.eni.enchere.bo.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class CategorieDAOImpl implements CategorieDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Categorie> categorieRowMapper = (rs, rowNum) -> {
        Categorie c = new Categorie();
        c.setNoCategorie(rs.getLong("no_categorie"));
        c.setLibelle(rs.getString("libelle"));
        return c;
    };

    @Override
    public List<Categorie> selectAll() {
        String sql = "SELECT * FROM Categories ORDER BY no_categorie";
        return jdbcTemplate.query(sql, categorieRowMapper);
    }

    @Override
    public Categorie selectById(long id) {
        String sql = "SELECT * FROM Categories WHERE no_categorie = ?";
        List<Categorie> result = jdbcTemplate.query(sql, categorieRowMapper, id);
        return result.isEmpty() ? null : result.get(0);
    }

    @Override
    public void insert(Categorie categorie) {
        String sql = "INSERT INTO Categories (libelle) " +
                "VALUES (?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, categorie.getLibelle());
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            categorie.setNoCategorie(keyHolder.getKey().longValue());
        }
    }

    @Override
    public void update(Categorie categorie) {
        String sql = "UPDATE Categories SET libelle=? " +
                "WHERE no_categorie=?";
        jdbcTemplate.update(sql,
                categorie.getLibelle(),
                categorie.getNoCategorie());
    }

    @Override
    public void delete(long id) {
        String sql = "DELETE FROM Categories WHERE no_categorie=?";
        jdbcTemplate.update(sql, id);
    }
}
