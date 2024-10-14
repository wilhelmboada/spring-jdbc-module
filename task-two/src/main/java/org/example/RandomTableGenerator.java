package org.example;

import org.apache.commons.lang3.RandomStringUtils;
import java.util.Random;

public class RandomTableGenerator {

   private static final String[] COLUMN_TYPES = {"INTEGER", "TEXT", "BOOLEAN", "NUMERIC"};

   public String generateRandomTableName() {
      return "table_" + RandomStringUtils.randomAlphanumeric(10);
   }

   public String[] generateRandomColumns(int columnCount) {
      Random random = new Random();
      String[] columns = new String[columnCount];

      for (int i = 0; i < columnCount; i++) {
         String columnName = "column_" + RandomStringUtils.randomAlphanumeric(5);
         String columnType = COLUMN_TYPES[random.nextInt(COLUMN_TYPES.length)];
         columns[i] = columnName + " " + columnType;
      }

      return columns;
   }

   public String generateInsertRow(int columnCount) {
      Random random = new Random();
      StringBuilder row = new StringBuilder("(");

      for (int i = 0; i < columnCount; i++) {
         if (i > 0) row.append(", ");
         row.append(random.nextInt(100));
      }
      row.append(")");

      return row.toString();
   }
}
