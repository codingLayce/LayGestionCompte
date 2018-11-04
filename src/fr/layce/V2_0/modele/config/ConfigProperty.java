package fr.layce.V2_0.modele.config;

abstract class ConfigProperty {
  private String key;

  ConfigProperty(String key){
    this.key = key;
  }

  String getKey(){
    return this.key;
  }

}
