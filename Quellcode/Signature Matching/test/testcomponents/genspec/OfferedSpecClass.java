package testcomponents.genspec;

public class OfferedSpecClass {

	public String concat(Specific spec, Specific s) {
		return spec.getStringAttr().concat(s.getStringAttr());
	}

	public Specific add(Specific spec, int i) {
		int sum = spec.getIntAttr() + i;
		return new Specific(spec.getStringAttr(), sum);
	}

	public Specific add(Specific spec, Specific spec2) {
		int sum = spec.getIntAttr() + spec2.getIntAttr();
		return new Specific(spec.getStringAttr(), sum);
	}

	public Long getLong(Specific spec) {
		return spec.getBoxedLongAttr();
	}

	public boolean and(Specific spec, boolean b) {
		return b && spec.isSpecificAttr();
	}

}
