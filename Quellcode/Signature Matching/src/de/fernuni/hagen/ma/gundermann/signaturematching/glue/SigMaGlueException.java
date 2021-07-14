package de.fernuni.hagen.ma.gundermann.signaturematching.glue;

import java.lang.reflect.Method;

public class SigMaGlueException extends Throwable {

  /**
   *
   */
  private static final long serialVersionUID = -8895710521534706387L;

  private final Throwable originalThrowable;

  private final Method calledSourceMethod;

  public SigMaGlueException( Throwable originalThrowable, Method calledSourceMethod ) {
    this.originalThrowable = originalThrowable;
    this.calledSourceMethod = calledSourceMethod;
  }

  public Throwable getOriginalThrowable() {
    return originalThrowable;
  }

  public Method getCalledSourceMethod() {
    return calledSourceMethod;
  }

  @Override
  public String getMessage() {
    return "Unable to use method matching: " + calledSourceMethod.getName();
  }

}
