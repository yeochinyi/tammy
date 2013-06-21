package org.moomoocow.tammy.analysis;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathHelper {

  public static double divide(double num, double divisor){
    return new BigDecimal(num).divide(new BigDecimal(divisor), 2,
        RoundingMode.HALF_EVEN).doubleValue();
  }
  
  public static long divideRoundUp(double num, double divisor){
    return new BigDecimal(num).divide(new BigDecimal(divisor), 0,RoundingMode.HALF_UP).longValue();
  }


}
