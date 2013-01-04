package org.moomoocow.tammy.marketdata

def cons(def val, Closure c) { return new LazyList(val, c); }

class LazyList
{
  def car
  Closure cdr
  LazyList(def car, Closure cdr) { this.car=car; this.cdr=cdr }
  //def getCar() { car }
  //def LazyList getCdr() { cdr ?  cdr() : null }
  
  def List take(n){
	 def r = []; def l = this
	 //n.times { r.add(l.car); l = l.getCdr() }
	 n.times { r.add(l.car); l = l.cdr() }
	 r
  }
  
  
  def LazyList filter(Closure pred){
  //if(getCar() != null)
	  if(pred(car))
		  return cons(car, { cdr().filter(pred) })
	  else
		  return cdr().filter(pred)
   //return l
  }
  
  
}



def integers(n) { cons(n, { integers(n+1) }) }
//println integers(10).
def naturalnumbers = integers(1)

//naturalnumbers.take(10).each { print "$it\n"; }

def evennumbers = naturalnumbers.filter { it % 2 == 0 }
evennumbers.take(10).each { print "$it\n"; }