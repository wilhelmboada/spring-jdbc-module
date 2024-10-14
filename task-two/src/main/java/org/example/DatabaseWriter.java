package org.example;

import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class DatabaseWriter {

   private final JdbcTemplate jdbcTemplate;

   public DatabaseWriter(ConfigurationLoader config) {
      DataSource dataSource = getDataSource(config);
      this.jdbcTemplate = new JdbcTemplate(dataSource);
   }

   private DataSource getDataSource(ConfigurationLoader config) {
      DriverManagerDataSource dataSource = new DriverManagerDataSource();
      dataSource.setDriverClassName("org.postgresql.Driver");
      dataSource.setUrl(config.getProperty("db.url"));
      dataSource.setUsername(config.getProperty("db.username"));
      dataSource.setPassword(config.getProperty("db.password"));
      return dataSource;
   }

   public void createTable(String tableName, String[] columns) {
      StringBuilder sql = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
      sql.append(tableName).append(" (id SERIAL PRIMARY KEY, ");

      for (int i = 0; i < columns.length; i++) {
         sql.append(columns[i]);
         if (i < columns.length - 1) {
            sql.append(", ");
         }
      }
      sql.append(")");

      jdbcTemplate.execute(sql.toString());
   }

   public void insertRow(String tableName, String row) {
      String sql = "INSERT INTO " + tableName + " VALUES " + row;
      jdbcTemplate.update(sql);
   }
}
