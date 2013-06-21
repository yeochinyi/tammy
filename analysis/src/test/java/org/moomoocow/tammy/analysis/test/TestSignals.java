package org.moomoocow.tammy.analysis.test;

import static org.junit.Assert.assertEquals;

import java.util.GregorianCalendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.moomoocow.tammy.analysis.Accountant;
import org.moomoocow.tammy.analysis.Deal.Action;
import org.moomoocow.tammy.analysis.signal.EnhancedProtective;
import org.moomoocow.tammy.analysis.signal.MACrosser;
import org.moomoocow.tammy.analysis.signal.MinPeriod;
import org.moomoocow.tammy.analysis.signal.Protective;
import org.moomoocow.tammy.analysis.signal.Signal;

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
    a.buyAll(1.0, new GregorianCalendar(2013,0,3).getTime(), Action.BUY);
    
    Signal s = new Protective(0.2, true,null);
    assertEquals(Action.TAKEPROFIT,s.analyze(new GregorianCalendar(2013,0,14).getTime(),0.0,0.0,0.0,0.0,1.2,0,a));
    assertEquals(null,s.analyze(new GregorianCalendar(2013,0,14).getTime(),0.0,0.0,0.0,0.0,1.1,0,a));
    
    s = new Protective(0.2, false,null);
    assertEquals(Action.STOPLOSS,s.analyze(new GregorianCalendar(2013,0,14).getTime(),0.0,0.0,0.0,0.0,0.8,0,a));
    assertEquals(null,s.analyze(new GregorianCalendar(2013,0,14).getTime(),0.0,0.0,0.0,0.0,0.9,0,a));
    
    s = new MinPeriod(3,null);
    assertEquals(null,s.analyze(new GregorianCalendar(2013,0,5).getTime(),0.0,0.0,0.0,0.0,1.2,0,a));
    assertEquals(null,s.analyze(new GregorianCalendar(2013,0,5).getTime(),0.0,0.0,0.0,0.0,0.8,0,a));
        
    s = new EnhancedProtective(0.3,0.1,null);
    assertEquals(null,s.analyze(new GregorianCalendar(2013,0,14).getTime(),0.0,0.0,0.0,0.0,1.2,0,a));
    assertEquals(null,s.analyze(new GregorianCalendar(2013,0,15).getTime(),0.0,0.0,0.0,0.0,2.0,0,a));
    assertEquals(Action.TAKEPROFIT,s.analyze(new GregorianCalendar(2013,0,16).getTime(),0.0,0.0,0.0,0.0,1.8,0,a));

  }
  
  @Test
  public void testAnalyzeMASignal() {
    int[] mas = {1,2};
    Signal s = new MACrosser(mas,true,true);
    int dateCount = 1;
    assertEquals(null,s.analyze(new GregorianCalendar(2013,0,dateCount++).getTime(),0.0,0.0,0.0,0.0,1.0,0,a));
    assertEquals(null,s.analyze(new GregorianCalendar(2013,0,dateCount++).getTime(),0.0,0.0,0.0,0.0,2.0,0,a));
    assertEquals(Action.SELL,s.analyze(new GregorianCalendar(2013,0,dateCount++).getTime(),0.0,0.0,0.0,0.0,1.0,0,a));
    assertEquals(Action.BUY,s.analyze(new GregorianCalendar(2013,0,dateCount++).getTime(),0.0,0.0,0.0,0.0,2.0,0,a));
    
    s = new MACrosser(mas,false,true);
    assertEquals(null,s.analyze(new GregorianCalendar(2013,0,dateCount++).getTime(),0.0,0.0,0.0,0.0,1.0,0,a));
    assertEquals(null,s.analyze(new GregorianCalendar(2013,0,dateCount++).getTime(),0.0,0.0,0.0,0.0,2.0,0,a));
    assertEquals(Action.BUY,s.analyze(new GregorianCalendar(2013,0,dateCount++).getTime(),0.0,0.0,0.0,0.0,1.0,0,a));
    assertEquals(Action.SELL,s.analyze(new GregorianCalendar(2013,0,dateCount++).getTime(),0.0,0.0,0.0,0.0,2.0,0,a));

  }
  

}
