package testcomponents.genspec;

public class OfferedSpecClass {

	public String concat(Specific spec, String s) {
		return spec.getStringAttr().concat(s);
	}

	public Specific add(Specific spec, int i) {
		int sum = spec.getIntAttr() + i;
		return new Specific(spec.getStringAttr(), sum);
	}

	public Long getLong(Specific spec) {
		return spec.getBoxedLongAttr();
	}

	public boolean and(Specific spec, boolean b) {
		return b && spec.isSpecificAttr();
	}

}
