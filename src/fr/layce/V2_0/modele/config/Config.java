package fr.layce.V2_0.modele.config;

import java.io.*;
import java.util.ArrayList;

public class Config {
  private static Config instance;

  private ArrayList<ConfigProperty> configs;

  public static void create() throws IOException {
    if (instance == null)
      instance = new Config();
  }

  public StringConfig getProperty(String key) throws FileNotFoundException {
    for (ConfigProperty p : this.configs){
      if (p.getKey().equals(key)){
        return (StringConfig)p;
      }
    }
    StringConfig conf = new StringConfig(key, null);
    this.configs.add(conf);
    saveConfig();
    return conf;
  }

  public static Config getInstance(){
    return instance;
  }

  private Config() throws IOException {
    this.configs = new ArrayList<>();
    loadConfig();
  }

  void saveConfig() throws FileNotFoundException {
    File configFile = new File("config.cfg");

    if (configFile.exists()){
      PrintWriter writer = new PrintWriter(configFile);
      for (ConfigProperty conf : this.configs){
        writer.write(conf.getKey() + "=" + ((StringConfig)conf).getConfig());
        writer.write(System.getProperty("line.separator"));
      }
      writer.close();
    }
  }

  // PRIVATE
  private void loadConfig() throws IOException {
    File configFile = new File("config.cfg");

    if (configFile.exists()){
      BufferedReader reader = new BufferedReader(new FileReader(configFile));
      String line;
      while ((line = reader.readLine()) != null){
        String arr[] = line.split("=");
        if (arr[1].equals("null"))
          this.configs.add(new StringConfig(arr[0], null));
        else
          this.configs.add(new StringConfig(arr[0], arr[1]));
      }
      reader.close();
    } else { // Première fois que application ouverte
      if (configFile.createNewFile()){
        initDefaultConfigs();
        saveConfig();
      }
    }
  }

  private void initDefaultConfigs(){
    String dirPath = "c:/Users/" + System.getProperty("user.name") + "/Desktop/";
    configs.add(new StringConfig("DefaultDirectory", dirPath));
  }
}
