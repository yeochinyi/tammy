package org.moomoocow.tammy.analysis.test;

import static org.junit.Assert.assertEquals;

import java.util.GregorianCalendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.moomoocow.tammy.analysis.Action;
import org.moomoocow.tammy.analysis.Deal;
import org.moomoocow.tammy.analysis.MtmManager;
import org.moomoocow.tammy.analysis.Action.ActionType;

public class TestMtmManager {
  
  MtmManager mtm;

  @Before
  public void setUp() throws Exception {
    mtm = new MtmManager(100,new GregorianCalendar(2000,0,1).getTime());
  }

  @After
  public void tearDown() throws Exception {
  }


  @Test
  public void testMtm() {
    mtm.add(new Deal(100,1.0,0.0,new GregorianCalendar(2000,0,1).getTime(),new Action(ActionType.BUY,null,null)));
    assertEquals(11.77,mtm.commitMarkToMarket(new GregorianCalendar(2000,1,1).getTime(), 2.0),0.0);
    assertEquals(11.77,mtm.getTotalAnnualizedPnl(),0.0);
    assertEquals(-6.29,mtm.commitMarkToMarket(new GregorianCalendar(2000,2,1).getTime(), 1.0),0.0);
    assertEquals(0.0,mtm.getTotalAnnualizedPnl(),0.0);
    assertEquals(11.77,mtm.commitMarkToMarket(new GregorianCalendar(2000,3,1).getTime(), 2.0),0.0);
    assertEquals(4.01,mtm.getTotalAnnualizedPnl(),0.0);

  }
  

  
}
