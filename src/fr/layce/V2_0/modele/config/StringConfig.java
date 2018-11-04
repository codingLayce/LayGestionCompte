package fr.layce.V2_0.modele.config;

import java.io.FileNotFoundException;

public class StringConfig extends ConfigProperty {
  private String config;

  StringConfig(String key, String string){
    super(key);

    this.config = string;
  }

  public String getConfig(){
    return this.config;
  }

  public void setConfig(String string) throws FileNotFoundException {
    this.config = string;
    Config.getInstance().saveConfig();
  }
}
