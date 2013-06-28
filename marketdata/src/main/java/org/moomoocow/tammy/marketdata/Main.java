package org.moomoocow.tammy.marketdata;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Main {

  public static final String actionArg = "action";

  public static void main(String[] args) {
    
    System.out.println("Starting MD Importer");
    
    CommandLineParser parser = new GnuParser();
    Options options = new Options();

    options.addOption("e", false, "make (e)xchanges");
    options.addOption("s", false, "make (s)tocks");
    options.addOption("d", true, "make (d)ata for [exchange_name]");
    options.addOption("g", true, "make (g)roup for [group_code]");

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

    if(line.hasOption("e")){
      new EODDataImport().makeExchanges();
    }
    
    if(line.hasOption("s")){
      new EODDataImport().importEODStocksFromFiles();
    }
    
    if(line.hasOption("d")){
      String exchange = line.getOptionValue("d");
      new YahooImport().importData(true, exchange);
    }

    if(line.hasOption("g")){
      String group = line.getOptionValue("g");
      new YahooImport().importGroupData(true, group);
    }
    

  }

}
