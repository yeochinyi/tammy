package org.moomoocow.tammy.analysis.test;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.moomoocow.tammy.analysis.math.ExponentialMA;
import org.moomoocow.tammy.analysis.math.MA;
import org.moomoocow.tammy.analysis.math.SimpleMA;

public class TestMath {

  
  @Before
  public void setUp() throws Exception {    

  }

  @After
  public void tearDown() throws Exception {
  }
  
  @Test
  public void testMA() {
    int[] i = {1,2,3};
    MA ma = new SimpleMA(i).add(1.0).add(2.0).add(3.0);
    Assert.assertEquals(3.0, ma.getMA(1));
    Assert.assertEquals(2.5, ma.getMA(2));
    Assert.assertEquals(2.0, ma.getMA(3));
  }
  
  @Test
  public void testEMA() {
    int[] i = {1,2,3};
    ExponentialMA ma = new ExponentialMA(i).add(1.0).add(2.0).add(3.0);
    Assert.assertEquals(3.0, ma.getMA(1));
    Assert.assertEquals(2.505, ma.getMA(2));
  }

  

}
