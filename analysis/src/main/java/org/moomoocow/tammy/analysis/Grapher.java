package org.moomoocow.tammy.analysis;

import static org.moomoocow.tammy.model.StockHistoricalData.Price.CLOSE;
import static org.moomoocow.tammy.model.StockHistoricalData.Price.HIGH;
import static org.moomoocow.tammy.model.StockHistoricalData.Price.LOW;
import static org.moomoocow.tammy.model.StockHistoricalData.Price.MID;
import static org.moomoocow.tammy.model.StockHistoricalData.Price.OPEN;

import java.awt.Dimension;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.ohlc.OHLCSeries;
import org.jfree.data.time.ohlc.OHLCSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.moomoocow.tammy.model.Exchange;
import org.moomoocow.tammy.model.Stock;
import org.moomoocow.tammy.model.StockHistoricalData;
import org.moomoocow.tammy.model.StockHistoricalData.Price;
import org.moomoocow.tammy.model.util.Helper;

public class Grapher extends ApplicationFrame {
  
  //private static final Calendar calendar = Calendar.getInstance();

  public Grapher(String paramString) {
    super(paramString);    
    JPanel localJPanel = createDemoPanel();
    localJPanel.setPreferredSize(new Dimension(500, 270));
    setContentPane(localJPanel);
  }

  OHLCSeries localOHLCSeries = new OHLCSeries("SUNW");
  
  TimeSeries localTimeSeries = new TimeSeries("Volume");
  
  TimeSeries nvTimeSeries = new TimeSeries("MA");


  private JFreeChart createChart()
  {
    createDataset();
    
    OHLCSeriesCollection localOHLCSeriesCollection = new OHLCSeriesCollection();
    localOHLCSeriesCollection.addSeries(localOHLCSeries);
    
    String str = "Sun Microsystems (SUNW)";
    JFreeChart localJFreeChart = ChartFactory.createHighLowChart(str, "Date", "Price", localOHLCSeriesCollection, true);
    XYPlot localXYPlot = (XYPlot)localJFreeChart.getPlot();    
    DateAxis localDateAxis = (DateAxis)localXYPlot.getDomainAxis();
    localDateAxis.setLowerMargin(0.01D);
    localDateAxis.setUpperMargin(0.01D);
    NumberAxis localNumberAxis1 = (NumberAxis)localXYPlot.getRangeAxis();
    localNumberAxis1.setLowerMargin(0.6D);
    localNumberAxis1.setAutoRangeIncludesZero(false);
    XYItemRenderer localXYItemRenderer = localXYPlot.getRenderer();
    localXYItemRenderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator("{0}: ({1}, {2})", new SimpleDateFormat("d-MMM-yyyy"), new DecimalFormat("0.00")));
    
    
    TimeSeriesCollection tsc = new TimeSeriesCollection(localTimeSeries);
    //tsc.addSeries(nvTimeSeries);
    
    NumberAxis localNumberAxis2 = new NumberAxis("Volume");
    localNumberAxis2.setUpperMargin(1.0D);
    localXYPlot.setRangeAxis(1, localNumberAxis2);
    localXYPlot.setDataset(1, tsc );
    localXYPlot.setRangeAxis(1, localNumberAxis2);
    localXYPlot.mapDatasetToRangeAxis(1, 1);
    XYBarRenderer localXYBarRenderer = new XYBarRenderer();
    localXYBarRenderer.setDrawBarOutline(false);
    localXYBarRenderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator("{0}: ({1}, {2})", new SimpleDateFormat("d-MMM-yyyy"), new DecimalFormat("0,000.00")));
    localXYPlot.setRenderer(1, localXYBarRenderer);
    ChartUtilities.applyCurrentTheme(localJFreeChart);
    localXYBarRenderer.setShadowVisible(false);
    localXYBarRenderer.setBarPainter(new StandardXYBarPainter());
    

    
    return localJFreeChart;
  }

  
  
  public void createDataset() {

    PersistenceManager pm = Helper.SINGLETON.getPersistenceManager();
    Query q = pm.newQuery(Exchange.class,"this.code == 'TEST'");
    List<Exchange> e = (List<Exchange> ) q.execute();
       
    Stock stock = null;
    
    for(Stock s  : e.get(0).getActiveStocks()){
      stock = s;
    }

    Double accumulatedMultipler = null;
    MA ma = new MA();
    
    for(StockHistoricalData h : stock.getSortedDailyData()){
      
      accumulatedMultipler = h.accumlateMultiplers(accumulatedMultipler);
      
      double mid = 0.0;
      double open = 0.0;
      double close = 0.0;
      double high = 0.0;
      double low = 0.0;
      
      mid = h.getAccX(MID);
      open = h.getAccX(OPEN);
      close = h.getAccX(CLOSE);
      high = h.getAccX(HIGH);
      low = h.getAccX(LOW);
      
      ma.add(mid);
          
      Day day = new Day(h.getDate());
      localOHLCSeries.add(day,open , high, low, close);
      localTimeSeries.add(day, h.getVol().doubleValue());
      nvTimeSeries.add(day,ma.getMA(14));
    }    
  }


  public JPanel createDemoPanel() {
    JFreeChart localJFreeChart = createChart();
    ChartPanel localChartPanel = new ChartPanel(localJFreeChart);
    localChartPanel.setMouseWheelEnabled(true);
    return localChartPanel;
  }

  public static void main(String[] paramArrayOfString) {
    Grapher localCandlestickChartDemo1 = new Grapher(
        "JFreeChart : CandlestickChartDemo1.java");
    localCandlestickChartDemo1.pack();
    RefineryUtilities.centerFrameOnScreen(localCandlestickChartDemo1);
    localCandlestickChartDemo1.setVisible(true);
  }
}
