package org.example.model;

import java.time.LocalDateTime;

public class Friendship {

   private int userId1;
   private int userId2;
   private LocalDateTime timestamp;

   // Getters and Setters
   public int getUserId1() {
      return userId1;
   }

   public void setUserId1(int userId1) {
      this.userId1 = userId1;
   }

   public int getUserId2() {
      return userId2;
   }

   public void setUserId2(int userId2) {
      this.userId2 = userId2;
   }

   public LocalDateTime getTimestamp() {
      return timestamp;
   }

   public void setTimestamp(LocalDateTime timestamp) {
      this.timestamp = timestamp;
   }
}

