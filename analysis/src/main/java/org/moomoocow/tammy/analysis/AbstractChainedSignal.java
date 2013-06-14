package org.moomoocow.tammy.analysis;


public abstract class AbstractChainedSignal implements Signal {
  
  protected Signal chainSignal;
  
  
  public AbstractChainedSignal(Signal chainSignal){
    this.chainSignal = chainSignal;
  }


}
