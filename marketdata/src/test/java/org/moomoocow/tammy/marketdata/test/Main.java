package org.moomoocow.tammy.marketdata.test;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;

import org.moomoocow.tammy.marketdata.DailyData;
import org.moomoocow.tammy.marketdata.Exchange;
import org.moomoocow.tammy.marketdata.Stock;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
				
		PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory("datanucleus.properties");
		PersistenceManager pm = pmf.getPersistenceManager();

		Transaction tx=pm.currentTransaction();
		
	    tx.begin();
	    
	    //Exchange e = new Exchange("SI","SGX");
	    //Stock s = new Stock("TEST","Test",true,e);
	    //pm.makePersistent(s);
	    //e.getStocks().add(s);
	    //pm.makePersistent(e);
	    
		    
		    //Inventory inv = new Inventory("My Inventory123");
		    //Product product = new Product("Sony Discman123", "A standard discman from Sony", 49.99);
		    //pm.makePersistent(product);
		    //inv.getProducts().add(product);
		    	
		    //pm.makePersistent(inv);
	    //pm.
	    //Query q = pm.newQuery("SELECT FROM " + Stock.class.getName() + " WHERE price < 150.00 ORDER BY price ASC");
	    //List<Product> products = (List<Product>)q.execute();

	    
	    //DailyData dd = new DailyData(new Date())
		    
	    tx.commit();		    				
		

		    //Query q = pm.newQuery("SELECT FROM " + Product.class.getName() + " WHERE price < 150.00 ORDER BY price ASC");
		    //List<Product> products = (List<Product>)q.execute();
		    //Iterator<Product> iter = products.iterator();
		    
		    //Query q = pm.newQuery("SELECT FROM " + Inventory.class.getName());
		    //List<Inventory> products = (List<Inventory>)q.execute();
		    //Iterator<Inventory> iter = products.iterator();
		    
		    //while (iter.hasNext())
		    //{
		    	//Inventory p = iter.next();
		        //System.out.println(p);

		        //... (use the retrieved objects)
		    //}

	    tx.commit();

	    pm.close();
	}

}
