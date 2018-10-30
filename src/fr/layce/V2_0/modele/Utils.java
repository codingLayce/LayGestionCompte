package fr.layce.V2_0.modele;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Month;

public class Utils {
  static double arrondi(double val){
    // Arrondi à 2 décimal
    BigDecimal bd = new BigDecimal(val);
    bd = bd.setScale(2, RoundingMode.HALF_UP);
    return bd.doubleValue();
  }

  public static Month toMonth(String fr){
    switch (fr){
      case "janvier":
        return Month.JANUARY;
      case "fevrier":
        return Month.FEBRUARY;
      case "mars":
        return Month.MARCH;
      case "avril":
        return Month.APRIL;
      case "mai":
        return Month.MAY;
      case "juin":
        return Month.JUNE;
      case "juillet":
        return Month.JULY;
      case "aout":
        return Month.AUGUST;
      case "septembre":
        return Month.SEPTEMBER;
      case "octobre":
        return Month.OCTOBER;
      case "novembre":
        return Month.NOVEMBER;
      case "decembre":
        return Month.DECEMBER;
      default:
        return null;
    }
  }
}
