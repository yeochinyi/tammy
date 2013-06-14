package org.moomoocow.tammy.analysis.test;

import static org.junit.Assert.assertEquals;

import java.util.GregorianCalendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.moomoocow.tammy.analysis.Accountant;
import org.moomoocow.tammy.analysis.Deal.Action;
import org.moomoocow.tammy.analysis.MAHLSignal;
import org.moomoocow.tammy.analysis.ProtectiveSignal;
import org.moomoocow.tammy.analysis.Signal;

public class TestSignals {

  private Accountant a;
  
  @Before
  public void setUp() throws Exception {    
    a = new Accountant(100.0,0.0);
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testAnalyzeProtectiveSignal() {
    Signal s = new ProtectiveSignal(null, 0.2, 0.2, 2);
    a.buyAll(1.0, new GregorianCalendar(2013,0,3).getTime(), Action.BUY);
    assertEquals(Action.TAKEPROFIT,s.analyze(new GregorianCalendar(2013,0,14).getTime(),0.0,0.0,0.0,0.0,1.2,a));
    assertEquals(null,s.analyze(new GregorianCalendar(2013,0,14).getTime(),0.0,0.0,0.0,0.0,1.1,a));
    assertEquals(Action.STOPLOSS,s.analyze(new GregorianCalendar(2013,0,14).getTime(),0.0,0.0,0.0,0.0,0.8,a));
    assertEquals(null,s.analyze(new GregorianCalendar(2013,0,14).getTime(),0.0,0.0,0.0,0.0,0.9,a));
    
    assertEquals(null,s.analyze(new GregorianCalendar(2013,0,5).getTime(),0.0,0.0,0.0,0.0,1.2,a));
    assertEquals(null,s.analyze(new GregorianCalendar(2013,0,5).getTime(),0.0,0.0,0.0,0.0,0.8,a));
  }
  
  @Test
  public void testAnalyzeMASignal() {
    int[] mas = {1,2};
    Signal s = new MAHLSignal(mas,true);
    int dateCount = 1;
    assertEquals(null,s.analyze(new GregorianCalendar(2013,0,dateCount++).getTime(),0.0,0.0,0.0,0.0,1.0,a));
    assertEquals(null,s.analyze(new GregorianCalendar(2013,0,dateCount++).getTime(),0.0,0.0,0.0,0.0,2.0,a));
    assertEquals(Action.SELL,s.analyze(new GregorianCalendar(2013,0,dateCount++).getTime(),0.0,0.0,0.0,0.0,1.0,a));
    assertEquals(Action.BUY,s.analyze(new GregorianCalendar(2013,0,dateCount++).getTime(),0.0,0.0,0.0,0.0,2.0,a));
    
    s = new MAHLSignal(mas,false);
    assertEquals(null,s.analyze(new GregorianCalendar(2013,0,dateCount++).getTime(),0.0,0.0,0.0,0.0,1.0,a));
    assertEquals(null,s.analyze(new GregorianCalendar(2013,0,dateCount++).getTime(),0.0,0.0,0.0,0.0,2.0,a));
    assertEquals(Action.BUY,s.analyze(new GregorianCalendar(2013,0,dateCount++).getTime(),0.0,0.0,0.0,0.0,1.0,a));
    assertEquals(Action.SELL,s.analyze(new GregorianCalendar(2013,0,dateCount++).getTime(),0.0,0.0,0.0,0.0,2.0,a));

  }
  

}
