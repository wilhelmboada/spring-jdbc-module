package org.example.model;

public class UserFriendPostReport {
   private Long userId;
   private String name;
   private String surname;
   private int totalFriends;
   private int totalPosts;

   // Getters and setters
   public Long getUserId() {
      return userId;
   }

   public void setUserId(Long userId) {
      this.userId = userId;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getSurname() {
      return surname;
   }

   public void setSurname(String surname) {
      this.surname = surname;
   }

   public int getTotalFriends() {
      return totalFriends;
   }

   public void setTotalFriends(int totalFriends) {
      this.totalFriends = totalFriends;
   }

   public int getTotalPosts() {
      return totalPosts;
   }

   public void setTotalPosts(int totalPosts) {
      this.totalPosts = totalPosts;
   }
}

