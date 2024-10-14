package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class StoredProcedureTest {

   private JdbcTemplate jdbcTemplate;

   @BeforeEach
   public void setUp() {
      DataSource dataSource = new DriverManagerDataSource("jdbc:postgresql://localhost:5432/postgres", "postgres", "secret");
      this.jdbcTemplate = new JdbcTemplate(dataSource);
   }

   @Test
   public void testDropAndCreateProcedures() {
      // Drop procedures
      String dropSql = "DROP PROCEDURE IF EXISTS add_user";
      assertDoesNotThrow(() -> jdbcTemplate.execute(dropSql));
      // Recreate procedures
      String createSql = "CREATE OR REPLACE PROCEDURE add_user(p_name VARCHAR, p_surname VARCHAR, p_birthdate TIMESTAMP) LANGUAGE plpgsql AS $$ BEGIN INSERT INTO users (name, surname, birthdate) VALUES (p_name, p_surname, p_birthdate); END; $$";
      assertDoesNotThrow(() -> jdbcTemplate.execute(createSql));
   }
}

