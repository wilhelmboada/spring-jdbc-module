package org.example.dao;

import org.example.model.Post;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PostDao {

   private final JdbcTemplate jdbcTemplate;

   public PostDao(JdbcTemplate jdbcTemplate) {
      this.jdbcTemplate = jdbcTemplate;
   }

   public void save(Post post) {
      String sql = "INSERT INTO posts (userId, text, timestamp) VALUES (?, ?, ?)";
      jdbcTemplate.update(sql, post.getUserId(), post.getText(), post.getTimestamp());
   }

   public Post findById(Long id) {
      String sql = "SELECT * FROM posts WHERE id = ?";
      return jdbcTemplate.queryForObject(sql, new PostRowMapper(), id);
   }

   public List<Post> findAll() {
      String sql = "SELECT * FROM posts";
      return jdbcTemplate.query(sql, new PostRowMapper());
   }

   public void deleteById(Long id) {
      String sql = "DELETE FROM posts WHERE id = ?";
      jdbcTemplate.update(sql, id);
   }

   private static class PostRowMapper implements RowMapper<Post> {
      @Override
      public Post mapRow(ResultSet rs, int rowNum) throws SQLException {
         Post post = new Post();
         post.setId(rs.getLong("id"));
         post.setUserId(rs.getInt("userId"));
         post.setText(rs.getString("text"));
         post.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
         return post;
      }
   }
}

