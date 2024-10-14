package org.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolManager {

   private final ConfigurationLoader config;
   private final DatabaseWriter writer;
   private final RandomTableGenerator generator;

   public ThreadPoolManager(ConfigurationLoader config) {
      this.config = config;
      this.writer = new DatabaseWriter(config);
      this.generator = new RandomTableGenerator();
   }

   public void start() {
      int threadCount = config.getIntProperty("db.pool.size");
      ExecutorService executor = Executors.newFixedThreadPool(threadCount);

      for (int i = 0; i < threadCount; i++) {
         executor.submit(() -> {
            String tableName = generator.generateRandomTableName();
            String[] columns = generator.generateRandomColumns(5);

            writer.createTable(tableName, columns);

            for (int j = 0; j < 100; j++) {
               String row = generator.generateInsertRow(5);
               writer.insertRow(tableName, row);
            }
         });
      }

      executor.shutdown();
   }
}
