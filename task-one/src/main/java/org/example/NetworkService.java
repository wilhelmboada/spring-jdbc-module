package org.example;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.github.javafaker.Faker;

@Service
public class NetworkService {

   private final JdbcTemplate jdbcTemplate;
   private final Faker faker;

   private static final Logger logger = LoggerFactory.getLogger(NetworkService.class);

   @Autowired
   public NetworkService(JdbcTemplate jdbcTemplate) {
      this.jdbcTemplate = jdbcTemplate;
      this.faker = new Faker();
   }

   public void createTables() {
      createUsersTable();
      createFriendshipsTable();
      createPostsTable();
      createLikesTable();
      logger.info("Tables created");
   }

   public void propagateData() {
      populateUsers(1000);
      populateFriendships(1000, 1000);
      populatePosts(1000, 100000, 1000);
      populateLikes(100000, 1000, 1000);
      logger.info("Data inserted");
   }

   public List<String> findUsersWithMoreThan100FriendsAndLikes() {
      String sql = """
            SELECT DISTINCT u.name
            FROM Users u
            JOIN Friendships f ON u.id = f.userid1 OR u.id = f.userid2
            JOIN Likes l ON u.id = l.userid
            WHERE l.timestamp >= '2025-03-01' AND l.timestamp < '2025-04-01'
            GROUP BY u.id
            HAVING COUNT(DISTINCT f.userid1) + COUNT(DISTINCT f.userid2) > 100
            AND COUNT(DISTINCT l.postid) > 100;
        """;

      return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getString("name"));
   }

   private void createUsersTable() {
      logger.info("Creating users table");
      String sql = """
                CREATE TABLE IF NOT EXISTS users (
                    id SERIAL PRIMARY KEY,
                    name VARCHAR(50) NOT NULL,
                    surname VARCHAR(50) NOT NULL,
                    birthdate DATE NOT NULL
                );
                """;
      jdbcTemplate.execute(sql);
   }

   private void createFriendshipsTable() {
      logger.info("Creating friendships table");
      String sql = """
                CREATE TABLE IF NOT EXISTS friendships (
                    userid1 INT NOT NULL,
                    userid2 INT NOT NULL,
                    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    PRIMARY KEY (userid1, userid2),
                    CONSTRAINT fk_user1 FOREIGN KEY (userid1) REFERENCES Users (id) ON DELETE CASCADE,
                    CONSTRAINT fk_user2 FOREIGN KEY (userid2) REFERENCES Users (id) ON DELETE CASCADE
                );
                """;
      jdbcTemplate.execute(sql);
   }

   private void createPostsTable() {
      logger.info("Creating posts table");
      String sql = """
                CREATE TABLE IF NOT EXISTS Posts (
                    id SERIAL PRIMARY KEY,
                    userId INT NOT NULL,
                    text TEXT NOT NULL,
                    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    CONSTRAINT fk_user FOREIGN KEY (userId) REFERENCES Users (id) ON DELETE CASCADE
                );
                """;
      jdbcTemplate.execute(sql);
   }

   private void createLikesTable() {
      logger.info("Creating liKes table");
      String sql = """
                CREATE TABLE IF NOT EXISTS likes (
                    postid INT NOT NULL,
                    userid INT NOT NULL,
                    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    PRIMARY KEY (postid, userid),
                    CONSTRAINT fk_post FOREIGN KEY (postid) REFERENCES Posts (id) ON DELETE CASCADE,
                    CONSTRAINT fk_user FOREIGN KEY (userid) REFERENCES Users (id) ON DELETE CASCADE
                );
                """;
      jdbcTemplate.execute(sql);
   }

   private void populateUsers(int count) {
      for (int i = 0; i < count; i++) {
         String name = faker.name().firstName();
         String surname = faker.name().lastName();
         LocalDate birthdate = faker.date().birthday(18, 65).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
         String sql = "INSERT INTO Users (name, surname, birthdate) VALUES (?, ?, ?)";
         jdbcTemplate.update(sql, name, surname, birthdate);
      }
   }

   private void populateFriendships(int userCount, int batchSize) {
      Random random = new Random();
      Set<String> friendships = new HashSet<>();
      List<Object[]> batchArgs = new ArrayList<>();

      while (friendships.size() < 70000) {
         int userId1 = random.nextInt(userCount) + 1;
         int userId2 = random.nextInt(userCount) + 1;

         if (userId1 != userId2) {
            String friendshipKey = userId1 < userId2 ? userId1 + "," + userId2 : userId2 + "," + userId1;

            if (!friendships.contains(friendshipKey)) {
               friendships.add(friendshipKey);


               // Generate a random date between 2024 and 2026
               LocalDate randomDate = generateRandomDateBetween(2025, 2025);
               // Convert LocalDate to Timestamp
               Timestamp timestamp = Timestamp.valueOf(randomDate.atStartOfDay(ZoneId.systemDefault()).toLocalDateTime());
               batchArgs.add(new Object[]{userId1, userId2, timestamp});

               if (batchArgs.size() == batchSize) {
                  String sql = "INSERT INTO friendships (userid1, userid2, timestamp) VALUES (?, ?, ?)";
                  jdbcTemplate.batchUpdate(sql, batchArgs);
                  batchArgs.clear();
               }
            }
         }
      }
   }

   private void populatePosts(int userCount, int postCount, int batchSize) {
      Random random = new Random();
      List<Object[]> batchArgs = new ArrayList<>();

      for (int i = 0; i < postCount; i++) {
         int userId = random.nextInt(userCount) + 1;
         String text = "This is a sample post text for user " + userId + " - Post number " + (i + 1);

         batchArgs.add(new Object[]{userId, text});

         if (batchArgs.size() == batchSize) {
            String sql = "INSERT INTO posts (userId, text) VALUES (?, ?)";
            jdbcTemplate.batchUpdate(sql, batchArgs);
            batchArgs.clear();
         }
      }

      if (!batchArgs.isEmpty()) {
         String sql = "INSERT INTO posts (userId, text) VALUES (?, ?)";
         jdbcTemplate.batchUpdate(sql, batchArgs);
      }
   }

   private void populateLikes(int postCount, int userCount, int batchSize) {
      Random random = new Random();
      List<Object[]> batchArgs = new ArrayList<>();
      Set<String> existingLikes = new HashSet<>(); // Conjunto para rastrear combinaciones únicas

      for (int i = 0; i < 300000; i++) {
         int postId = random.nextInt(postCount) + 1; // IDs basados en 1
         int userId = random.nextInt(userCount) + 1; // IDs basados en 1

         // Crear una clave única para el par (postId, userId)
         String key = postId + "-" + userId;

         // Verificar si el par ya existe
         if (!existingLikes.contains(key)) {
            existingLikes.add(key); // Agregar al conjunto de existentes

            // Generate a random date between 2024 and 2026
            LocalDate randomDate = generateRandomDateBetween(2025, 2025);
            // Convert LocalDate to Timestamp
            Timestamp timestamp = Timestamp.valueOf(randomDate.atStartOfDay(ZoneId.systemDefault()).toLocalDateTime());
            batchArgs.add(new Object[]{postId, userId, timestamp});

            // Ejecutar la inserción en batch si alcanzamos el tamaño del lote
            if (batchArgs.size() == batchSize) {
               String sql = "INSERT INTO likes (postid, userid, timestamp) VALUES (?, ?, ?)";
               jdbcTemplate.batchUpdate(sql, batchArgs);
               batchArgs.clear();
            }
         }
      }

      // Inserta cualquier resto de registros que no hayan sido insertados
      if (!batchArgs.isEmpty()) {
         String sql = "INSERT INTO likes (postid, userid, timestamp) VALUES (?, ?, ?)";
         jdbcTemplate.batchUpdate(sql, batchArgs);
      }
   }

   private LocalDate generateRandomDateBetween(int startYear, int endYear) {
      Random random = new Random();

      // Generate a random year, month, and day
      int year = startYear + random.nextInt(endYear - startYear + 1);
      int month = 1 + random.nextInt(12); // Month is 1-12
      int day = 1 + random.nextInt(LocalDate.of(year, month, 1).lengthOfMonth()); // Get the length of the month

      return LocalDate.of(year, month, day);
   }

}
