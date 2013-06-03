package org.moomoocow.tammy.analysis;

import static org.moomoocow.tammy.model.StockHistoricalData.Price.CLOSE;
import static org.moomoocow.tammy.model.StockHistoricalData.Price.HIGH;
import static org.moomoocow.tammy.model.StockHistoricalData.Price.LOW;
import static org.moomoocow.tammy.model.StockHistoricalData.Price.MID;
import static org.moomoocow.tammy.model.StockHistoricalData.Price.OPEN;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYDrawableAnnotation;
import org.jfree.chart.annotations.XYPointerAnnotation;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.ohlc.OHLCSeries;
import org.jfree.data.time.ohlc.OHLCSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.Layer;
import org.jfree.ui.LengthAdjustmentType;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.RefineryUtilities;
import org.jfree.ui.TextAnchor;
import org.moomoocow.tammy.model.Stock;
import org.moomoocow.tammy.model.StockHistoricalData;
import org.moomoocow.tammy.model.util.Helper;

public class Grapher extends ApplicationFrame {
  
  //private static final Calendar calendar = Calendar.getInstance();
  
  /**
   * 
   */
  private static final long serialVersionUID = 4438595343592513192L;
  private JFreeChart localJFreeChart; 
  private XYPlot localXYPlot;
  private OHLCSeriesCollection localOHLCSeriesCollection;
  //private TimeSeries maTimeSeries;
  private TimeSeries volTimeSeries;  
  
  private List<Integer> mAPeriods;
  private List<TimeSeries> mATimeSeries;
  
  private String symbol;
  
  private int days;
  
  private Simulator s;

  public Grapher(String symbol, int days, List<Integer> mAPeriods) {
    super(symbol);
    
    this.s = new Simulator(symbol, days, mAPeriods.get(0), mAPeriods.get(1), new SimulatorListener() {
      
      Date lastBuyDate = null;
      
      @Override
      public void sell(double price, double qty, Date d, int holdingDays) {
        IntervalMarker localIntervalMarker = new IntervalMarker(new Day(lastBuyDate).getFirstMillisecond(), new Day(d).getFirstMillisecond());
        localIntervalMarker.setLabelOffsetType(LengthAdjustmentType.EXPAND);
        localIntervalMarker.setPaint(new Color(150, 150, 255));
        localIntervalMarker.setLabel("Holding Period");
        localIntervalMarker.setLabelPaint(Color.blue);
        localIntervalMarker.setLabelAnchor(RectangleAnchor.TOP_LEFT);
        localIntervalMarker.setLabelTextAnchor(TextAnchor.TOP_RIGHT);
        localXYPlot.addDomainMarker(localIntervalMarker, Layer.BACKGROUND);
        
        XYPointerAnnotation localXYPointerAnnotation = new XYPointerAnnotation("Sell@" + String.format("%.2g", price), new Day(d).getFirstMillisecond(), price, 2.356194490192345D);
        localXYPointerAnnotation.setBaseRadius(10.0D);
        localXYPointerAnnotation.setTipRadius(0.0D);
        localXYPointerAnnotation.setPaint(Color.blue);
        localXYPointerAnnotation.setTextAnchor(TextAnchor.HALF_ASCENT_RIGHT);
        localXYPlot.addAnnotation(localXYPointerAnnotation);

        
      }
      
      @Override
      public void buy(double price, double qty, Date d, int holdingDays) {
        lastBuyDate = d;
        
        XYPointerAnnotation localXYPointerAnnotation = new XYPointerAnnotation("Buy@" + String.format("%.2g", price), new Day(d).getFirstMillisecond(), price, 2.356194490192345D);
        localXYPointerAnnotation.setBaseRadius(10.0D);
        localXYPointerAnnotation.setTipRadius(0.0D);
        localXYPointerAnnotation.setPaint(Color.blue);
        localXYPointerAnnotation.setTextAnchor(TextAnchor.HALF_ASCENT_RIGHT);
        localXYPlot.addAnnotation(localXYPointerAnnotation);
      }
    });
    
    this.days = days;
    this.symbol = symbol;
    this.mAPeriods = mAPeriods;
    
    int graphIndex = 0;
    
    localOHLCSeriesCollection = new OHLCSeriesCollection();
    mATimeSeries = new ArrayList<TimeSeries>();
            
    createDataset();
    
    //localJFreeChart = ChartFactory.createHighLowChart(symbol, "Date", "Price", localOHLCSeriesCollection, true);
    localJFreeChart = ChartFactory.createCandlestickChart(symbol, "Date", "Price", localOHLCSeriesCollection, true);
    localXYPlot = (XYPlot)localJFreeChart.getPlot();

    //Domain Axis
    DateAxis localDateAxis = (DateAxis)localXYPlot.getDomainAxis();
    localDateAxis.setLowerMargin(0.01D);
    localDateAxis.setUpperMargin(0.01D);
    
    //RangeAxis
    NumberAxis localNumberAxis1 = (NumberAxis)localXYPlot.getRangeAxis();
    //localNumberAxis1.setLowerMargin(0.6D);
    localNumberAxis1.setAutoRangeIncludesZero(false);
    
    NumberAxis localNumberAxis2 = new NumberAxis();
    //localNumberAxis2.setUpperMargin(1.0D);
    localXYPlot.setRangeAxis(1, localNumberAxis2);
    
    XYItemRenderer localXYItemRenderer = localXYPlot.getRenderer();
    localXYItemRenderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator("{0}: ({1}, {2})", new SimpleDateFormat("d-MMM-yyyy"), new DecimalFormat("0.00")));

    
    
    for (int i=0; i < mAPeriods.size(); i++) {
      localXYPlot.setDataset(++graphIndex, new TimeSeriesCollection(mATimeSeries.get(i)));
      localXYPlot.mapDatasetToRangeAxis(graphIndex, 0);
      XYLineAndShapeRenderer localXYBarRenderer = new XYLineAndShapeRenderer(true,false);
      localXYBarRenderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator("{0}: ({1}, {2})", new SimpleDateFormat("d-MMM-yyyy"), new DecimalFormat("0.00")));
      localXYPlot.setRenderer(graphIndex, localXYBarRenderer);
      
    }
    
    
    localXYPlot.setDataset(++graphIndex, new TimeSeriesCollection(volTimeSeries));   
    localXYPlot.mapDatasetToRangeAxis(graphIndex, 1);        
    //XYBarRenderer localXYBarRenderer = new XYBarRenderer();
    XYLineAndShapeRenderer localXYBarRenderer = new XYLineAndShapeRenderer(true,false);
    //localXYBarRenderer.setDrawBarOutline(false);
    localXYBarRenderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator("{0}: ({1}, {2})", new SimpleDateFormat("d-MMM-yyyy"), new DecimalFormat("0,000.00")));
    //localXYBarRenderer.setShadowVisible(false);
    //localXYBarRenderer.setBarPainter(new StandardXYBarPainter());    
    localXYPlot.setRenderer(graphIndex, localXYBarRenderer);
    
    this.s.testStocks();
    
    ChartUtilities.applyCurrentTheme(localJFreeChart);

    
    //for (int index=0; index < tsc.size(); index++) {
    //}
    
    
    ChartPanel localChartPanel = new ChartPanel(localJFreeChart);
    localChartPanel.setMouseWheelEnabled(true);

    localChartPanel.setPreferredSize(new Dimension(500, 270));
    setContentPane(localChartPanel);
    
    
  }
  
  @SuppressWarnings("unchecked")
  private void createDataset() {

    PersistenceManager pm = Helper.SINGLETON.getPersistenceManager();
    Query q = pm.newQuery(Stock.class,"this.code == '" + symbol + "'");
    List<Stock> s = (List<Stock> ) q.execute();
       
    Stock stock = s.get(0);

    Double accumulatedMultipler = null;
    MA ma = new MA();

    OHLCSeries localOHLCSeries = new OHLCSeries(symbol);
    localOHLCSeriesCollection.addSeries(localOHLCSeries);

    
    for (int i=0; i < mAPeriods.size(); i++) {
      mATimeSeries.add(new TimeSeries("MA(" + i + ")"));
    }

    volTimeSeries = new TimeSeries("VOL");
    
    List<StockHistoricalData> sortedDailyData = stock.getSortedDailyData();
    
    int sortedDailyDataSize = sortedDailyData.size();
    
    int x = (sortedDailyDataSize < days ? 0 : sortedDailyDataSize - days);
                
    while(x < sortedDailyDataSize){
      
      StockHistoricalData h = sortedDailyData.get(x++);
      
      accumulatedMultipler = h.accumlateMultiplers(accumulatedMultipler);
      
      double mid = 0.0;
      double open = 0.0;
      double close = 0.0;
      double high = 0.0;
      double low = 0.0;
      double vol = 0.0;
      
      mid = h.getAccX(MID);
      open = h.getAccX(OPEN);
      close = h.getAccX(CLOSE);
      high = h.getAccX(HIGH);
      low = h.getAccX(LOW);
      vol = h.getVol().doubleValue();
      
      ma.add(mid);
          
      Day day = new Day(h.getDate());
      localOHLCSeries.add(day,open , high, low, close);
      
      volTimeSeries.add(day, vol);
      
      for (int i=0; i < mAPeriods.size(); i++) { 
        mATimeSeries.get(i).add(day,ma.getMA(mAPeriods.get(i)));
      }
      
    } 
    
  }


  //public JPanel createDemoPanel() {
    //JFreeChart localJFreeChart = createMainAxis();
    //ChartPanel localChartPanel = new ChartPanel(localJFreeChart);
    //localChartPanel.setMouseWheelEnabled(true);
    //return localChartPanel;
  //}

  public static void main(String[] args) {
    
    int days = Integer.parseInt(args[1]);
    
    List<Integer> MAList = new ArrayList<Integer>();
            
    for (int i = 2; i < args.length; i++) {
      MAList.add(new Integer(args[i]));
    }
    
    Grapher localCandlestickChartDemo1 = new Grapher(args[0],days,MAList);
    localCandlestickChartDemo1.pack();
    RefineryUtilities.centerFrameOnScreen(localCandlestickChartDemo1);
    localCandlestickChartDemo1.setVisible(true);
  }
}
