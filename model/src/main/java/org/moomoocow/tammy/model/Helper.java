package org.moomoocow.tammy.model;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

public enum Helper {
  SINGLETON;

  private PersistenceManager pm;

  Helper() {
    PersistenceManagerFactory pmf = JDOHelper
        .getPersistenceManagerFactory("datanucleus.properties");
    this.pm = pmf.getPersistenceManager();
  }

  public PersistenceManager getPersistenceManager() {
    return this.pm;
  }

}
