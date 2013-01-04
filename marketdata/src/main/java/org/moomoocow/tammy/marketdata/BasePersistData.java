package org.moomoocow.tammy.marketdata;

import java.util.Date;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;

@PersistenceCapable(identityType=IdentityType.DATASTORE)
@Inheritance(strategy = InheritanceStrategy.SUBCLASS_TABLE)
public abstract class BasePersistData {
    
  protected Date createdDate;
  
  public BasePersistData(){
    this.createdDate = new Date();
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  public Date getCreatedDate() {
    return createdDate;
  }
  
}
