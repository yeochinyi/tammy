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
  
  public static double randomDouble(double min, double max){
	  double range = max - min;
	  double ret = Math.random() * range + min;	  
	  return divide(ret * 100.0,100.0);
  }

  public static int randomInt(int min, int max){
	  int range = max - min;
	  double ret = Math.random() * (double) range + min;	  
	  return (int) Math.round(ret);
  }


}
