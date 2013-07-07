package org.moomoocow.tammy.gui;

import org.moomoocow.tammy.analysis.MtmManager;
import org.moomoocow.tammy.analysis.Simulator;
import org.moomoocow.tammy.analysis.signal.*;
import org.moomoocow.tammy.model.Stock;
import org.moomoocow.tammy.model.util.Helper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 7/5/13
 * Time: 8:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class MainIntellij {
    private JPanel mainPanel;

    private JTextField symbolTf;
    private JTextField dateTf;
    private JPanel graphPanel;
    private JTree strategyTree;
    private JTextField numTf;
    private JTextPane consoleTP;
    private JScrollPane mainSP;
    private Simulator sim;

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
        strategyTree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                //Object src = e.getSource();
                Object src = ((DefaultMutableTreeNode) e.getNewLeadSelectionPath().getLastPathComponent()).getUserObject();
                if(src instanceof Signal){
                    Signal sig = (Signal) src;
                    JPanel panel = g.draw(sim,sig);
                    panel.setPreferredSize(new Dimension(3000,300));
                    JScrollPane sp = new JScrollPane(panel);
                    graphPanel.add(sp, BorderLayout.CENTER);
                    ((JFrame) mainPanel.getTopLevelAncestor()).pack();

                }
            }
        });
    }

    private void appendToConsole(String text){
        consoleTP.setText(consoleTP.getText() + "\n"+ text);
    }

    private void plot(){
        Date dateFrom = null;
        try{
            dateFrom = df.parse(dateTf.getText());
        }
        catch(Exception e){
            //System.out.println( );
            String text = consoleTP.getText();
            appendToConsole("date parse error : " + dateTf.getText());
        }

        String symbol = symbolTf.getText();

        Query q = pm.newQuery(Stock.class, "this.code == '" + symbol+ "'");
        List<Stock> s = (List<Stock>) q.execute();

        sim = new Simulator(s.get(0), null);
        //int[] mas = { 9, 54 };
        //Signal sig = new BuyAtFirst(new MinPeriod(3, new EnhancedProtective(0.18,0.06, new Protective(0.07, false,
          //      new MACrosser(mas, true)))));

        int runs = Integer.parseInt(numTf.getText());

        for(int i=0; i<runs; i++){
            sim.execute(new BuyAtFirst(MinPeriod.getRandom(EnhancedProtective.getRandom(Protective.getRandomStopLoss(MACrosser.getRandomEMA(null))))));
            //sim.execute(new BuyAtFirst(EnhancedProtective.getRandom(Protective.getRandomStopLoss(MACrosser.getRandomEMA(null)))));
            if(i % 1000 == 0) appendToConsole("Counting " + i);
        }

        DefaultMutableTreeNode root = new DefaultMutableTreeNode(new Object());
        TreeModel tm = new DefaultTreeModel(root);

        for (Map.Entry<Double, List<Signal>> e : sim.getPnlMap().entrySet()) {
            //System.out.println(e.getKey() + "-->");
            DefaultMutableTreeNode profitBranch = new DefaultMutableTreeNode(e.getKey());
            root.add(profitBranch);
            for (Signal st : e.getValue()) {
                MtmManager mtmManager = sim.getMtmMap().get(st);
                DefaultMutableTreeNode signalB =  new DefaultMutableTreeNode(st);
                profitBranch.add(signalB);
                signalB.add(new DefaultMutableTreeNode(mtmManager));
                //System.out.println("  " + st.toString() + "->"
                  //      + mtmManager);
            }
        }

        this.strategyTree.setModel(tm);
        this.strategyTree.validate();

        /*
        */
        //this.mainSP.invalidate();
        //pack();


    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("MainIntellij");
        frame.setContentPane(new MainIntellij().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(800,600));
        frame.pack();
        frame.setVisible(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
