package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer;

public class NoComponentImplementationFoundException extends Throwable {

  /**
   *
   */
  private static final long serialVersionUID = -1380083652238469645L;

  private Class<?> componentInterface;

  public NoComponentImplementationFoundException( Class<?> componentInterface ) {
    this.componentInterface = componentInterface;
  }

  public Class<?> getComponentInterface() {
    return componentInterface;
  }

}
