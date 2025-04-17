package com.eni.enchere.dao.Categorie;

import com.eni.enchere.bo.Categorie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

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
}
