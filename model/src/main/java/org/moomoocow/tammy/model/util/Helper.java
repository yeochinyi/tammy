package org.moomoocow.tammy.model.util;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

public enum Helper {
  SINGLETON;

  private PersistenceManager pm;

  Helper() {
    PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory(
        "datanucleus.properties", Helper.class.getClassLoader());
    this.pm = pmf.getPersistenceManager();
  }

  public PersistenceManager getPersistenceManager() {
    return this.pm;
  }

}
