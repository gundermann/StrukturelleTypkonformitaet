package testcomponents.genspec;

public class General {

	private 
	final 
	String stringAttr;

	private int intAttr;

	private Long boxedLongAttr;

	public General(String stringAttr) {
		this.stringAttr = stringAttr;
	}

	public General(String stringAttr, int intAttr, Long boxedLongAttr) {
		this.stringAttr = stringAttr;
		this.intAttr = intAttr;
		this.boxedLongAttr = boxedLongAttr;
	}

	public String getStringAttr() {
		return stringAttr;
	}

	public int getIntAttr() {
		return intAttr;
	}

	public Long getBoxedLongAttr() {
		return boxedLongAttr;
	}

}
