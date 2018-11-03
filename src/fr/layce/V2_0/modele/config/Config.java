package fr.layce.V2_0.modele.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Config {
  private static Config instance;

  private ArrayList<ConfigProperty> configs;

  public static void create() throws IOException {
    if (instance == null)
      instance = new Config();
  }

  public static Config getInstance(){
    return instance;
  }

  private Config() throws IOException {
    this.configs = new ArrayList<>();
    loadConfig();
  }

  // PRIVATE
  private void loadConfig() throws IOException {
    File configFile = new File("config.cfg");
    if (configFile.exists()){
      // TODO: Lire le fichier et charger les configs.
    } else { // Première fois que application ouverte
      if (configFile.createNewFile()){
        initDefaultConfigs();
        final GsonBuilder builder = new GsonBuilder();
        final Gson gson = builder.create();
        PrintWriter writer = new PrintWriter(configFile);
        writer.write("{");
        for (ConfigProperty conf : this.configs){
          writer.write(System.getProperty("line.separator"));
          writer.write(gson.toJson(conf));
        }
        writer.write(System.getProperty("line.separator") + "}");
        writer.close();
      }
    }
  }

  private void initDefaultConfigs(){
    String dirPath = "c:/Users/" + System.getProperty("user.name") + "/Desktop/";
    configs.add(new StringConfig("defaultDirectory", dirPath));
  }
}
