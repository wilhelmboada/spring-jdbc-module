package org.example.model;

import java.time.LocalDateTime;

public class PostPopularityReport {
   private Long postId;
   private String postText;
   private String authorName;
   private String authorSurname;
   private int totalLikes;
   private LocalDateTime postTimestamp;

   // Getters and setters
   public Long getPostId() {
      return postId;
   }

   public void setPostId(Long postId) {
      this.postId = postId;
   }

   public String getPostText() {
      return postText;
   }

   public void setPostText(String postText) {
      this.postText = postText;
   }

   public String getAuthorName() {
      return authorName;
   }

   public void setAuthorName(String authorName) {
      this.authorName = authorName;
   }

   public String getAuthorSurname() {
      return authorSurname;
   }

   public void setAuthorSurname(String authorSurname) {
      this.authorSurname = authorSurname;
   }

   public int getTotalLikes() {
      return totalLikes;
   }

   public void setTotalLikes(int totalLikes) {
      this.totalLikes = totalLikes;
   }

   public LocalDateTime getPostTimestamp() {
      return postTimestamp;
   }

   public void setPostTimestamp(LocalDateTime postTimestamp) {
      this.postTimestamp = postTimestamp;
   }
}
