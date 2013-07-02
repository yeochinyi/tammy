package org.moomoocow.tammy.gui;

import java.awt.Color;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYPointerAnnotation;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.ohlc.OHLCSeries;
import org.jfree.data.time.ohlc.OHLCSeriesCollection;
import org.jfree.ui.TextAnchor;
import org.moomoocow.tammy.analysis.Action;
import org.moomoocow.tammy.analysis.Simulator;
import org.moomoocow.tammy.analysis.signal.Signal;
import org.moomoocow.tammy.model.StockHistoricalData;
import org.moomoocow.tammy.model.StockHistoricalData.Price;

public class Grapher extends JPanel {

  private static final long serialVersionUID = 4438595343592513192L;
  
  //private final static DateFormat df = new SimpleDateFormat("yyyy-mm-dd");
  
  private String title;

  public Grapher(String title) {
      super();
      this.title = title;    
  }

  public void draw(Simulator sim, Signal strategy) {
      
      this.removeAll();

    OHLCSeriesCollection localOHLCSeriesCollection = new OHLCSeriesCollection();
    TimeSeries volTimeSeries;
    List<TimeSeries> strategiesTimeSeries = new ArrayList<TimeSeries>();

    sim.execute(strategy);

    final OHLCSeries localOHLCSeries = new OHLCSeries("main");
    localOHLCSeriesCollection.addSeries(localOHLCSeries);
    volTimeSeries = new TimeSeries("VOL");

    for (StockHistoricalData h : sim.getSortedDailyData()) {
      Day day = new Day(h.getDate());
      localOHLCSeries.add(day, h.getAccX(Price.OPEN), h.getAccX(Price.HIGH),
         h.getAccX(Price.LOW), h.getAccX(Price.CLOSE));
      //localOHLCSeries.add(day, h.getOpen(), h.getHigh(),
        //      h.getLow(),h.getClose());
      
      volTimeSeries.add(day, h.getVol().doubleValue());
    }

    Map<String, SortedMap<Date, Double>> displayPoints = strategy
        .getGraphPoints();
    for (Entry<String, SortedMap<Date, Double>> e : displayPoints.entrySet()) {
      TimeSeries ts = new TimeSeries(e.getKey());
      strategiesTimeSeries.add(ts);
      for (Entry<Date, Double> e2 : e.getValue().entrySet()) {
        ts.add(new Day(e2.getKey()), e2.getValue());
      }
    }

    int graphIndex = 0;

    //JFreeChart localJFreeChart = ChartFactory.createHighLowChart(super.getTitle(), "Date",
     //"Price", localOHLCSeriesCollection, true);
    JFreeChart localJFreeChart = ChartFactory.createCandlestickChart(
        title, "Date", "Price", localOHLCSeriesCollection, true);
    XYPlot localXYPlot = (XYPlot) localJFreeChart.getPlot();

    // Domain Axis
    DateAxis localDateAxis = (DateAxis) localXYPlot.getDomainAxis();
    localDateAxis.setLowerMargin(0.01D);
    localDateAxis.setUpperMargin(0.01D);

    // RangeAxis
    NumberAxis localNumberAxis1 = (NumberAxis) localXYPlot.getRangeAxis();
    // localNumberAxis1.setLowerMargin(0.6D);
    localNumberAxis1.setAutoRangeIncludesZero(false);

    NumberAxis localNumberAxis2 = new NumberAxis();
    // localNumberAxis2.setUpperMargin(1.0D);
    localXYPlot.setRangeAxis(1, localNumberAxis2);

    XYItemRenderer localXYItemRenderer = localXYPlot.getRenderer();
    localXYItemRenderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator(
        "{0}: ({1}, {2})", new SimpleDateFormat("d-MMM-yyyy"),
        new DecimalFormat("0.00")));

    for (int i = 0; i < strategiesTimeSeries.size(); i++) {
      localXYPlot.setDataset(++graphIndex, new TimeSeriesCollection(
          strategiesTimeSeries.get(i)));
      localXYPlot.mapDatasetToRangeAxis(graphIndex, 0);
      XYLineAndShapeRenderer localXYBarRenderer = new XYLineAndShapeRenderer(
          true, false);
      localXYBarRenderer
          .setBaseToolTipGenerator(new StandardXYToolTipGenerator(
              "{0}: ({1}, {2})", new SimpleDateFormat("d-MMM-yyyy"),
              new DecimalFormat("0.00")));
      localXYPlot.setRenderer(graphIndex, localXYBarRenderer);

    }

    localXYPlot.setDataset(++graphIndex,
        new TimeSeriesCollection(volTimeSeries));
    localXYPlot.mapDatasetToRangeAxis(graphIndex, 1);
    // XYBarRenderer localXYBarRenderer = new XYBarRenderer();
    XYLineAndShapeRenderer localXYBarRenderer = new XYLineAndShapeRenderer(
        true, false);
    // localXYBarRenderer.setDrawBarOutline(false);
    localXYBarRenderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator(
        "{0}: ({1}, {2})", new SimpleDateFormat("d-MMM-yyyy"),
        new DecimalFormat("0,000.00")));
    // localXYBarRenderer.setShadowVisible(false);
    // localXYBarRenderer.setBarPainter(new StandardXYBarPainter());
    localXYPlot.setRenderer(graphIndex, localXYBarRenderer);

    //Date lastBuyDate = null;

    for (Entry<String, Action> e : strategy.getActions().entrySet()) {
    	Action a = e.getValue();
        localXYPlot.addAnnotation(addPointerAnno(e.getKey(),a.getDate(), a.getPrice()));
	}
    
    /*
    for (Deal t : sim.get(strategy).getDeals()) {
      if (t.isBuy()) {
        lastBuyDate = t.getDate();
        localXYPlot.addAnnotation(addPointerAnno(t));
      } else {
        IntervalMarker localIntervalMarker = new IntervalMarker(new Day(
            lastBuyDate).getFirstMillisecond(),
            new Day(t.getDate()).getFirstMillisecond());
        localIntervalMarker.setLabelOffsetType(LengthAdjustmentType.EXPAND);
        localIntervalMarker.setPaint(new Color(150, 150, 255));
        localIntervalMarker.setLabel("Hold");
        localIntervalMarker.setLabelPaint(Color.blue);
        localIntervalMarker.setLabelAnchor(RectangleAnchor.TOP_LEFT);
        localIntervalMarker.setLabelTextAnchor(TextAnchor.TOP_RIGHT);
        localXYPlot.addDomainMarker(localIntervalMarker, Layer.BACKGROUND);
        localXYPlot.addAnnotation(addPointerAnno(t));
      }
    }*/

    ChartUtilities.applyCurrentTheme(localJFreeChart);
    ChartPanel localChartPanel = new ChartPanel(localJFreeChart);
    localChartPanel.setMouseWheelEnabled(true);

    //localChartPanel.setPreferredSize(new Dimension(500, 270));
    //setContentPane(localChartPanel);
    this.add(localChartPanel);
  }

  private XYPointerAnnotation addPointerAnno(String comments,Date date,double price) {
    XYPointerAnnotation localXYPointerAnnotation = new XYPointerAnnotation(
    		comments, new Day(date).getFirstMillisecond(), price,
        2.356194490192345D);
    localXYPointerAnnotation.setBaseRadius(10.0D);
    localXYPointerAnnotation.setTipRadius(0.0D);
    localXYPointerAnnotation.setPaint(Color.blue);
    localXYPointerAnnotation.setTextAnchor(TextAnchor.HALF_ASCENT_RIGHT);
    return localXYPointerAnnotation;
  }
}
