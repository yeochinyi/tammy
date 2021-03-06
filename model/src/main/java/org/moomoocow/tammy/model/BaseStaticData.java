package org.moomoocow.tammy.model;

import java.util.Date;

import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;

@PersistenceCapable
@Inheritance(strategy = InheritanceStrategy.SUBCLASS_TABLE)
public abstract class BaseStaticData extends BasePersistData {
    
  protected String code;
  
  protected String description;

  protected Date modifiedDate;

  protected Boolean active;
  
  //protected Integer totalChildren;
  

  public BaseStaticData(String code, String description) {
    super();
    this.createdDate = new Date();
    this.code = code;
    this.description = description;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }


  
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Date getModifiedDate() {
    return modifiedDate;
  }

  public void setModifiedDate(Date modifiedDate) {
    this.modifiedDate = modifiedDate;
  }

  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  /*
  public Integer getTotalChildren() {
    return totalChildren;
  }

  public void setTotalChildren(Integer totalChildren) {
    this.totalChildren = totalChildren;
  }
  
  public void addTotalChildren(Integer addChildren) {
    if(this.totalChildren == null) this.totalChildren = 0;
    this.totalChildren += addChildren;
  }*/


  
}
