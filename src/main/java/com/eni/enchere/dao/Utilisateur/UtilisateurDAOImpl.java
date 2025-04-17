package com.eni.enchere.dao.Utilisateur;

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
public class UtilisateurDAOImpl implements UtilisateurDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Utilisateur> utilisateurRowMapper = (rs, rowNum) -> {
        Utilisateur u = new Utilisateur();
        u.setNoUtilisateur(rs.getLong("no_utilisateur"));
        u.setPseudo(rs.getString("pseudo"));
        u.setNom(rs.getString("nom"));
        u.setPrenom(rs.getString("prenom"));
        u.setEmail(rs.getString("email"));
        u.setTelephone(rs.getString("telephone"));
        u.setRue(rs.getString("rue"));
        u.setCodePostal(rs.getString("code_postal"));
        u.setVille(rs.getString("ville"));
        u.setMotDePasse(rs.getString("mot_de_passe"));
        u.setCredit(rs.getInt("credit"));
        u.setAdmin(rs.getBoolean("administrateur"));
        return u;
    };

    @Override
    public void insert(Utilisateur utilisateur) {
        String sql = "INSERT INTO Utilisateurs (pseudo, nom, prenom, email, telephone, rue, code_postal, ville, mot_de_passe, credit, administrateur) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, utilisateur.getPseudo());
            ps.setString(2, utilisateur.getNom());
            ps.setString(3, utilisateur.getPrenom());
            ps.setString(4, utilisateur.getEmail());
            ps.setString(5, utilisateur.getTelephone());
            ps.setString(6, utilisateur.getRue());
            ps.setString(7, utilisateur.getCodePostal());
            ps.setString(8, utilisateur.getVille());
            ps.setString(9, utilisateur.getMotDePasse());
            ps.setLong(10, utilisateur.getCredit());
            ps.setBoolean(11, utilisateur.getAdmin());
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            utilisateur.setNoUtilisateur(keyHolder.getKey().longValue());
        }
    }

    @Override
    public void update(Utilisateur utilisateur) {
        String sql = "UPDATE Utilisateurs SET pseudo=?, nom=?, prenom=?, email=?, telephone=?, rue=?, code_postal=?, ville=?, mot_de_passe=?, credit=?, administrateur=? " +
                "WHERE no_utilisateur=?";
        jdbcTemplate.update(sql,
                utilisateur.getPseudo(),
                utilisateur.getNom(),
                utilisateur.getPrenom(),
                utilisateur.getEmail(),
                utilisateur.getTelephone(),
                utilisateur.getRue(),
                utilisateur.getCodePostal(),
                utilisateur.getVille(),
                utilisateur.getMotDePasse(),
                utilisateur.getCredit(),
                utilisateur.getAdmin(),
                utilisateur.getNoUtilisateur());
    }

    @Override
    public void delete(long id) {
        String sql = "DELETE FROM Utilisateurs WHERE no_utilisateur=?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Utilisateur selectById(long id) {
        String sql = "SELECT * FROM Utilisateurs WHERE no_utilisateur=?";
        return jdbcTemplate.queryForObject(sql, utilisateurRowMapper, id);
    }

    @Override
    public List<Utilisateur> selectAll() {
        String sql = "SELECT * FROM Utilisateurs";
        return jdbcTemplate.query(sql, utilisateurRowMapper);
    }

    @Override
    public Utilisateur selectByPseudo(String pseudo) {
        String sql = "SELECT * FROM Utilisateurs WHERE pseudo=?";
        List<Utilisateur> result = jdbcTemplate.query(sql, utilisateurRowMapper, pseudo);
        return result.isEmpty() ? null : result.get(0);
    }

    @Override
    public Utilisateur selectByEmail(String email) {
        String sql = "SELECT * FROM Utilisateurs WHERE email=?";
        List<Utilisateur> result = jdbcTemplate.query(sql, utilisateurRowMapper, email);
        return result.isEmpty() ? null : result.get(0);
    }
}
