package org.example;

public class App {

   public static void main(String[] args) {
      if (args.length < 1) {
         System.out.println("Please provide a configuration file path.");
         System.exit(1);
      }

      String configFilePath = args[0];
      ConfigurationLoader config = new ConfigurationLoader(configFilePath);
      ThreadPoolManager threadPool = new ThreadPoolManager(config);

      threadPool.start();
   }
}
