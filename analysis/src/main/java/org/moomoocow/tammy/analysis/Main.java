package org.moomoocow.tammy.analysis;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.jfree.ui.RefineryUtilities;

public class Main {

  public static final String actionArg = "action";

  public static void main(String[] args) {
    
    System.out.println("Starting Analyser");
    
    CommandLineParser parser = new GnuParser();
    Options options = new Options();

    options.addOption("s",true,"(s)ymbol");
    options.addOption("g", true, "(g)rapher for comma-delimited MA values[default 50,100]");
    //options.addOption("s", true, "(s)imulator for comma-delimited MA values[default 30]");

    try {
      CommandLine line = parser.parse(options, args, false);
      
      int size = line.getOptions().length;
      if(size == 0){
        throw new ParseException("No options selected!");
      }
      
      System.out.println("Selected " + size + " options.");
      
      new Main(line);
    } catch (ParseException e) {
      new HelpFormatter().printHelp("java -jar <jarfile>", options);
    }

  }

  public Main(CommandLine line) {
    
    String symbol = "TEST";
    
    if(line.hasOption("s")){
      line.getOptionValue("s", "TEST");
    }

    if(line.hasOption("g")){
      String mAStringValues = line.getOptionValue("g");
      
      List<Integer> MAList = new ArrayList<Integer>();
      
      String[] split = mAStringValues.split(",");      
      for (int i = 0; i < split.length; i++) {
        MAList.add(new Integer(split[i]));
      }        
      
      //Grapher localCandlestickChartDemo1 = new Grapher("TEST",MAList);
      //localCandlestickChartDemo1.pack();
      //RefineryUtilities.centerFrameOnScreen(localCandlestickChartDemo1);
      //localCandlestickChartDemo1.setVisible(true);      
    }
        
    

  }

}
