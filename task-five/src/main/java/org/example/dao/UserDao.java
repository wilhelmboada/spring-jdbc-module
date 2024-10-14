package org.example.dao;

import org.example.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDao {

   private final JdbcTemplate jdbcTemplate;

   public UserDao(JdbcTemplate jdbcTemplate) {
      this.jdbcTemplate = jdbcTemplate;
   }

   public void save(User user) {
      String sql = "INSERT INTO users (name, surname, birthdate) VALUES (?, ?, ?)";
      jdbcTemplate.update(sql, user.getName(), user.getSurname(), user.getBirthdate());
   }

   public User findById(Long id) {
      String sql = "SELECT * FROM users WHERE id = ?";
      return jdbcTemplate.queryForObject(sql, new UserRowMapper(), id);
   }

   public List<User> findAll() {
      String sql = "SELECT * FROM users";
      return jdbcTemplate.query(sql, new UserRowMapper());
   }

   public void deleteById(Long id) {
      String sql = "DELETE FROM users WHERE id = ?";
      jdbcTemplate.update(sql, id);
   }

   private static class UserRowMapper implements RowMapper<User> {
      @Override
      public User mapRow(ResultSet rs, int rowNum) throws SQLException {
         User user = new User();
         user.setId(rs.getLong("id"));
         user.setName(rs.getString("name"));
         user.setSurname(rs.getString("surname"));
         user.setBirthdate(rs.getDate("birthdate").toLocalDate());
         return user;
      }
   }
}

