package org.example.dao;

import org.example.model.Like;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class LikeDao {

   private final JdbcTemplate jdbcTemplate;

   public LikeDao(JdbcTemplate jdbcTemplate) {
      this.jdbcTemplate = jdbcTemplate;
   }

   public void save(Like like) {
      String sql = "INSERT INTO likes (postid, userid, timestamp) VALUES (?, ?, ?)";
      jdbcTemplate.update(sql, like.getPostId(), like.getUserId(), like.getTimestamp());
   }

   public List<Like> findAll() {
      String sql = "SELECT * FROM likes";
      return jdbcTemplate.query(sql, new LikeRowMapper());
   }

   public void delete(int postId, int userId) {
      String sql = "DELETE FROM likes WHERE postid = ? AND userid = ?";
      jdbcTemplate.update(sql, postId, userId);
   }

   private static class LikeRowMapper implements RowMapper<Like> {
      @Override
      public Like mapRow(ResultSet rs, int rowNum) throws SQLException {
         Like like = new Like();
         like.setPostId(rs.getInt("postid"));
         like.setUserId(rs.getInt("userid"));
         like.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
         return like;
      }
   }
}

