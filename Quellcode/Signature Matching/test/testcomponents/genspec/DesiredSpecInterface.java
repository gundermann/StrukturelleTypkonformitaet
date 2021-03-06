package testcomponents.genspec;

public interface DesiredSpecInterface {

  public String concat( Specific spec1, Specific spec2 );

  public Specific add( Specific spec, Integer i );

  public Specific add( Specific spec1, Specific spec2 );

  public Long getBoxedLongAttr( Specific spec );

  public Boolean and( Specific spec, Boolean b );

  public Specific getNull();
}
