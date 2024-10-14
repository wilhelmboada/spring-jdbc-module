package org.example;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigurationLoader {

   private Properties properties = new Properties();

   public ConfigurationLoader(String configFilePath) {
      try (FileInputStream input = new FileInputStream(configFilePath)) {
         properties.load(input);
      } catch (IOException e) {
         e.printStackTrace();
         throw new RuntimeException("Unable to load configuration file");
      }
   }

   public String getProperty(String key) {
      return properties.getProperty(key);
   }

   public int getIntProperty(String key) {
      return Integer.parseInt(properties.getProperty(key));
   }
}