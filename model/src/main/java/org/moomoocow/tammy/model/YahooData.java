package org.moomoocow.tammy.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD}) 
@Retention(RetentionPolicy.RUNTIME)
public @interface YahooData{ 
  String key();
  boolean mayContainCommas() default false;
  boolean isAlwaysImported() default false;
}
