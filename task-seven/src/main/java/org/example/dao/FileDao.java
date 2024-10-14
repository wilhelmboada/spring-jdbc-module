package org.example.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;

import javax.sql.DataSource;

@Service
public class FileDao {

   private final JdbcTemplate jdbcTemplate;
   private final DataSource dataSource;

   public FileDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
      this.jdbcTemplate = jdbcTemplate;
      this.dataSource = dataSource;
   }

   public void createTablaAndStoredProcedures() {
      String sql = """
                CREATE TABLE IF NOT EXISTS files (
                      id SERIAL PRIMARY KEY,
                      file_name VARCHAR(255) NOT NULL,
                      file_data BYTEA NOT NULL,
                      upload_time TIMESTAMP NOT NULL DEFAULT NOW()
                  );
                """;

      String saveFile = """
                  CREATE OR REPLACE PROCEDURE save_file(
                      p_file_name VARCHAR,
                      p_file_data BYTEA
                  )
                  LANGUAGE plpgsql
                  AS $$
                  BEGIN
                      INSERT INTO files (file_name, file_data)\s
                      VALUES (p_file_name, p_file_data);
                  END;
                  $$;
                """;

      String getFile = """
                  CREATE OR REPLACE PROCEDURE get_file(
                      p_file_id INT,
                      OUT p_file_name VARCHAR,
                      OUT p_file_data BYTEA
                  )
                  LANGUAGE plpgsql
                  AS $$
                  BEGIN
                      SELECT file_name, file_data INTO p_file_name, p_file_data
                      FROM files WHERE id = p_file_id;
                  END;
                  $$;
                """;

      jdbcTemplate.execute(sql);
      jdbcTemplate.execute(saveFile);
      jdbcTemplate.execute(getFile);
   }

   public void saveFile(String fileName, InputStream fileData) {
      try (Connection conn = DataSourceUtils.getConnection(dataSource);
            CallableStatement stmt = conn.prepareCall("CALL save_file(?, ?)")) {
         stmt.setString(1, fileName);
         stmt.setBinaryStream(2, fileData);
         stmt.execute();
      } catch (Exception e) {
         throw new RuntimeException("Error saving file", e);
      }
   }

   public byte[] getFile(int fileId) {
      return jdbcTemplate.execute("{CALL get_file(?, ?, ?)}", (CallableStatement stmt) -> {
         stmt.setInt(1, fileId);
         stmt.registerOutParameter(2, Types.VARCHAR);
         stmt.registerOutParameter(3, Types.BINARY);

         stmt.execute();

         return stmt.getBytes(3);
      });
   }
}

