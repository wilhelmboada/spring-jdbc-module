package org.example;

import java.util.List;

import org.example.config.JdbcConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class Application {

   public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(JdbcConfig.class);
       NetworkService networkService = context.getBean(NetworkService.class);
       networkService.createTables();
       networkService.propagateData();
       List<String> users = networkService.findUsersWithMoreThan100FriendsAndLikes();
       users.forEach(System.out::println);
    }


}
