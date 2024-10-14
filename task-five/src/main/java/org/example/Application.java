package org.example;

import java.util.List;

import org.example.config.JdbcConfig;
import org.example.dao.ReportDao;
import org.example.model.PostPopularityReport;
import org.example.model.UserFriendPostReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {

   private static final Logger logger = LoggerFactory.getLogger(Application.class);

   public static void main(String[] args) {
      ApplicationContext context = new AnnotationConfigApplicationContext(JdbcConfig.class);
      ReportDao reportDao = context.getBean(ReportDao.class);
      List<PostPopularityReport> postPopularityReport = reportDao.getPopularPostsReport();
      List<UserFriendPostReport> userFriendPostReport = reportDao.getUsersWithFriendsAndPostsReport();

      reportDao.createStoredProcedures();

      List<PostPopularityReport> postPopularityReport2 = reportDao.getPostLikesReport();
      List<UserFriendPostReport> userFriendPostReport3 = reportDao.getUserActivityReport();
      reportDao.dropStoredProcedures();
   }

}
