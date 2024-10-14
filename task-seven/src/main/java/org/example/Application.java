package org.example;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.example.config.JdbcConfig;
import org.example.dao.FileDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {

   private static final Logger logger = LoggerFactory.getLogger(Application.class);

   public static void main(String[] args) {
      ApplicationContext context = new AnnotationConfigApplicationContext(JdbcConfig.class);
      FileDao fileDao = context.getBean(FileDao.class);
      fileDao.createTablaAndStoredProcedures();

      InputStream fileData = new ByteArrayInputStream(new byte[10]);

      fileDao.saveFile("test.txt", fileData);
      fileDao.getFile(1);
   }

}
