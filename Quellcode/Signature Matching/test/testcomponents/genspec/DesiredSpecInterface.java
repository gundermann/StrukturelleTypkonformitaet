package testcomponents.genspec;

public interface DesiredSpecInterface {

	public String concat(Specific spec, String s);
	
	public Specific add (Specific spec, int i);
	
	public Long getLong(Specific spec);

	public boolean and(Specific spec, boolean b);
}
