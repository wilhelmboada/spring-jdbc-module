package org.example.model;

import java.time.LocalDateTime;

public class Like {

   private int postId;
   private int userId;
   private LocalDateTime timestamp;

   // Getters and Setters
   public int getPostId() {
      return postId;
   }

   public void setPostId(int postId) {
      this.postId = postId;
   }

   public int getUserId() {
      return userId;
   }

   public void setUserId(int userId) {
      this.userId = userId;
   }

   public LocalDateTime getTimestamp() {
      return timestamp;
   }

   public void setTimestamp(LocalDateTime timestamp) {
      this.timestamp = timestamp;
   }
}

