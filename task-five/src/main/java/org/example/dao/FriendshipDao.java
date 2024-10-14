package org.example.dao;

import org.example.model.Friendship;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class FriendshipDao {

   private final JdbcTemplate jdbcTemplate;

   public FriendshipDao(JdbcTemplate jdbcTemplate) {
      this.jdbcTemplate = jdbcTemplate;
   }

   public void save(Friendship friendship) {
      String sql = "INSERT INTO friendships (userid1, userid2, timestamp) VALUES (?, ?, ?)";
      jdbcTemplate.update(sql, friendship.getUserId1(), friendship.getUserId2(), friendship.getTimestamp());
   }

   public List<Friendship> findAll() {
      String sql = "SELECT * FROM friendships";
      return jdbcTemplate.query(sql, new FriendshipRowMapper());
   }

   public void delete(int userId1, int userId2) {
      String sql = "DELETE FROM friendships WHERE userid1 = ? AND userid2 = ?";
      jdbcTemplate.update(sql, userId1, userId2);
   }

   private static class FriendshipRowMapper implements RowMapper<Friendship> {
      @Override
      public Friendship mapRow(ResultSet rs, int rowNum) throws SQLException {
         Friendship friendship = new Friendship();
         friendship.setUserId1(rs.getInt("userid1"));
         friendship.setUserId2(rs.getInt("userid2"));
         friendship.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
         return friendship;
      }
   }
}

