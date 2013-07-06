package org.moomoocow.tammy.gui;

import org.moomoocow.tammy.analysis.Simulator;
import org.moomoocow.tammy.analysis.signal.*;
import org.moomoocow.tammy.model.Stock;
import org.moomoocow.tammy.model.util.Helper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 7/5/13
 * Time: 8:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class MainIntellij {
    private JPanel panel1;

    private JTextField symbolTf;
    private JTextField dateTf;
    private JPanel graphPanel;
    private JTree tree1;
    private JTextField numTf;
    private JScrollPane mainSP;

    private final static DateFormat df = new SimpleDateFormat("yyyy-mm-dd");
    private PersistenceManager pm = Helper.SINGLETON.getPersistenceManager();

    private Grapher g;
    public MainIntellij() {
        g = new Grapher("TEST");
        symbolTf.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                plot();
            }
        });
    }

    private void plot(){


        Date dateFrom = null;
        try{
            dateFrom = df.parse(dateTf.getText());
        }
        catch(Exception e){
            System.out.println("date parse error : " + dateTf.getText() );
        }

        String symbol = symbolTf.getText();

        Query q = pm.newQuery(Stock.class, "this.code == '" + symbol+ "'");
        List<Stock> s = (List<Stock>) q.execute();

        Simulator sim = new Simulator(s.get(0), null);
        int[] mas = { 9, 54 };
        Signal sig = new BuyAtFirst(new MinPeriod(3, new EnhancedProtective(0.18,0.06, new Protective(0.07, false,
                new MACrosser(mas, true)))));
        JPanel panel = g.draw(sim,sig);
        panel.setPreferredSize(new Dimension(3000,300));
        JScrollPane sp = new JScrollPane(panel);
        this.graphPanel.add(sp, BorderLayout.CENTER);
        ((JFrame) this.panel1.getTopLevelAncestor()).pack();
        //this.mainSP.invalidate();
        //pack();


    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("MainIntellij");
        frame.setContentPane(new MainIntellij().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(800,600));
        frame.pack();
        frame.setVisible(true);
    }

}
