package fr.layce.V2_0.modele.config;

public class StringConfig extends ConfigProperty {
  private String config;

  StringConfig(String key, String string){
    super(key);

    this.config = string;
  }

  public String getConfig(){
    return this.config;
  }

  public void setConfig(String string){
    this.config = string;
  }
}
