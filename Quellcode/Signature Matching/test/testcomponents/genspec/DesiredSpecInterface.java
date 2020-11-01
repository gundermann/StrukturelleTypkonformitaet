package testcomponents.genspec;

public interface DesiredSpecInterface {

	public String concat(Specific spec1, Specific spec2);

	public Specific add (Specific spec, int i);
	
	public Specific add (Specific spec1, Specific spec2);
	
	public Long getLong(Specific spec);

	public boolean and(Specific spec, boolean b);
}
