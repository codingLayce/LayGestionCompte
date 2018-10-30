package fr.layce.V2_0.modele;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Utils {
  public static double arrondi(double val){
    // Arrondi � 2 d�cimal
    BigDecimal bd = new BigDecimal(val);
    bd = bd.setScale(2, RoundingMode.HALF_UP);
    return bd.doubleValue();
  }
}
