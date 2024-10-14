package org.example.dao;

import org.example.model.PostPopularityReport;
import org.example.model.UserFriendPostReport;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class ReportDao {

   private final JdbcTemplate jdbcTemplate;

   public ReportDao(JdbcTemplate jdbcTemplate) {
      this.jdbcTemplate = jdbcTemplate;
   }

   public void createStoredProcedures() {
      String createUserActivityReportProcedure = "CREATE OR REPLACE FUNCTION get_user_activity_report() " +
            "RETURNS TABLE(user_id INT, name VARCHAR, surname VARCHAR, total_friends bigint, total_posts bigint) AS $$ " +
            "BEGIN " +
            "RETURN QUERY " +
            "SELECT u.id AS user_id, u.name, u.surname, COUNT(DISTINCT f.userid2) AS total_friends, " +
            "COUNT(DISTINCT p.id) AS total_posts " +
            "FROM users u " +
            "LEFT JOIN friendships f ON u.id = f.userid1 " +
            "LEFT JOIN posts p ON u.id = p.userid " +
            "GROUP BY u.id, u.name, u.surname " +
            "ORDER BY total_friends DESC, total_posts DESC; " +
            "END; $$ LANGUAGE plpgsql;";

      String createPostLikesReportProcedure = "CREATE OR REPLACE FUNCTION get_post_likes_report() " +
            "RETURNS TABLE(post_id INT, post_text TEXT, author_name VARCHAR, author_surname VARCHAR, " +
            "total_likes INT8, post_timestamp TIMESTAMP) AS $$ " +
            "BEGIN " +
            "RETURN QUERY " +
            "SELECT p.id AS post_id, p.text AS post_text, u.name AS author_name, u.surname AS author_surname, " +
            "COUNT(l.userid) AS total_likes, p.timestamp AS post_timestamp " +
            "FROM posts p " +
            "JOIN users u ON p.userid = u.id " +
            "LEFT JOIN likes l ON p.id = l.postid " +
            "GROUP BY p.id, p.text, u.name, u.surname, p.timestamp " +
            "HAVING COUNT(l.userid) > 0 " +
            "ORDER BY total_likes DESC, p.timestamp DESC; " +
            "END; $$ LANGUAGE plpgsql;";

      jdbcTemplate.execute(createUserActivityReportProcedure);
      jdbcTemplate.execute(createPostLikesReportProcedure);
   }

   public void dropStoredProcedures() {
      jdbcTemplate.execute("DROP function get_user_activity_report();");
      jdbcTemplate.execute("DROP function get_post_likes_report();");
   }

   public List<UserFriendPostReport> getUsersWithFriendsAndPostsReport() {
      String sql = """
                SELECT
                    u.id AS user_id,
                    u.name,
                    u.surname,
                    COUNT(DISTINCT f.userid2) AS total_friends,
                    COUNT(DISTINCT p.id) AS total_posts
                FROM
                    users u
                LEFT JOIN
                    friendships f ON u.id = f.userid1
                LEFT JOIN
                    posts p ON u.id = p.userid
                GROUP BY
                    u.id, u.name, u.surname
                ORDER BY
                    total_friends DESC, total_posts DESC;
                """;

      return jdbcTemplate.query(sql, new UserFriendPostReportRowMapper());
   }

   public List<PostPopularityReport> getPopularPostsReport() {
      String sql = """
                SELECT
                    p.id AS post_id,
                    p.text AS post_text,
                    u.name AS author_name,
                    u.surname AS author_surname,
                    COUNT(l.userid) AS total_likes,
                    p.timestamp AS post_timestamp
                FROM
                    posts p
                JOIN
                    users u ON p.userid = u.id
                LEFT JOIN
                    likes l ON p.id = l.postid
                GROUP BY
                    p.id, p.text, u.name, u.surname, p.timestamp
                HAVING
                    COUNT(l.userid) > 0
                ORDER BY
                    total_likes DESC, p.timestamp DESC;
                """;

      return jdbcTemplate.query(sql, new PostPopularityReportRowMapper());
   }

   public List<PostPopularityReport> getPostLikesReport() {
      String sql = "SELECT * FROM get_post_likes_report()";

      return jdbcTemplate.query(sql, (rs, rowNum) -> {
         PostPopularityReport report = new PostPopularityReport();
         report.setPostId(rs.getLong("post_id"));
         report.setPostText(rs.getString("post_text"));
         report.setAuthorName(rs.getString("author_name"));
         report.setAuthorSurname(rs.getString("author_surname"));
         report.setTotalLikes(rs.getInt("total_likes"));
         report.setPostTimestamp(rs.getTimestamp("post_timestamp").toLocalDateTime());
         return report;
      });
   }

   public List<UserFriendPostReport> getUserActivityReport() {
      String sql = "SELECT * FROM get_user_activity_report()";

      return jdbcTemplate.query(sql, (rs, rowNum) -> {
         UserFriendPostReport report = new UserFriendPostReport();
         report.setUserId(rs.getLong("user_id"));
         report.setName(rs.getString("name"));
         report.setSurname(rs.getString("surname"));
         report.setTotalFriends(rs.getInt("total_friends"));
         report.setTotalPosts(rs.getInt("total_posts"));
         return report;
      });
   }

   private static class PostPopularityReportRowMapper implements RowMapper<PostPopularityReport> {
      @Override
      public PostPopularityReport mapRow(ResultSet rs, int rowNum) throws SQLException {
         PostPopularityReport report = new PostPopularityReport();
         report.setPostId(rs.getLong("post_id"));
         report.setPostText(rs.getString("post_text"));
         report.setAuthorName(rs.getString("author_name"));
         report.setAuthorSurname(rs.getString("author_surname"));
         report.setTotalLikes(rs.getInt("total_likes"));
         report.setPostTimestamp(rs.getTimestamp("post_timestamp").toLocalDateTime());
         return report;
      }
   }

   private static class UserFriendPostReportRowMapper implements RowMapper<UserFriendPostReport> {
      @Override
      public UserFriendPostReport mapRow(ResultSet rs, int rowNum) throws SQLException {
         UserFriendPostReport report = new UserFriendPostReport();
         report.setUserId(rs.getLong("user_id"));
         report.setName(rs.getString("name"));
         report.setSurname(rs.getString("surname"));
         report.setTotalFriends(rs.getInt("total_friends"));
         report.setTotalPosts(rs.getInt("total_posts"));
         return report;
      }
   }
}
