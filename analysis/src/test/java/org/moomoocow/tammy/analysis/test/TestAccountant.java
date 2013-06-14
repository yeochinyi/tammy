package org.moomoocow.tammy.analysis.test;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.moomoocow.tammy.analysis.Accountant;
import org.moomoocow.tammy.analysis.Deal.Action;

public class TestAccountant {
  
  Accountant dm;

  @Before
  public void setUp() throws Exception {
    dm = new Accountant(110, 0.1);    
  }

  @After
  public void tearDown() throws Exception {
  }


  @Test
  public void testBuyAll() {
    dm.buyAll(1.0, new Date(), Action.BUY);
    assertEquals(100,dm.getStock());
    assertEquals(0.0,dm.getCash(),0.0);
  }

  @Test
  public void testSellAll() {
    dm.buyAll(1.0, new Date(), Action.BUY);
    dm.sellAll(1.0, new Date(), Action.SELL);
    assertEquals(0,dm.getStock());
    assertEquals(90.0,dm.getCash(),0.0);
  }


  @Test
  public void testGetAbsolutePnl() {
    dm.buyAll(1.0, new Date(), Action.BUY);
    assertEquals(0.0,dm.getAbsolutePnl(1.1),0.0);
  }


  @Test
  public void testGetPnlSinceLastTransaction() {   
    dm.buyAll(1.0, new Date(), Action.BUY);
    assertEquals(0.1,dm.getPnlSinceLastTransaction(1.1),0.0);
    assertEquals(-0.1,dm.getPnlSinceLastTransaction(0.9),0.0);
  }
  

  @Test
  public void testGetPeriodAfterLastDealExclWeekends() {    
    dm.buyAll(1.0, new GregorianCalendar(2013,0,3).getTime(), Action.BUY);    
    int periodAfterLastDealExclWeekends = dm.getPeriodAfterLastDealExclWeekends(new GregorianCalendar(2013,0,9).getTime());
    
    assertEquals(5,periodAfterLastDealExclWeekends);
  }
  
}
