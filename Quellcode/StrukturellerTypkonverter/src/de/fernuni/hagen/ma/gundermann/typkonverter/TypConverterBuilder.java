package de.fernuni.hagen.ma.gundermann.typkonverter;


public class TypConverterBuilder {
	
	private final ConformityCheckingBase checkingBase;
	private StructureDefinition structureDefinition;
	private BehaviorDefinition behaviorDefinition;


	public static TypConverterBuilder create(ConformityCheckingBase checkingBase) {
		return new TypConverterBuilder(checkingBase);
	}
	
	
	private TypConverterBuilder(ConformityCheckingBase checkingBase) {
		this.checkingBase = checkingBase;
	}
	
	public TypConverterBuilder withStructureDefinition(StructureDefinition structureDefinition) {
		this.structureDefinition = structureDefinition;
		return this;
	}
	
	public <T> TypConverter<T> build(Class<T> targetStructure){
		if(checkingBase == ConformityCheckingBase.NAMES) {
			return new de.fernuni.hagen.ma.gundermann.typkonverter.structureByNames.TypeConverter<T>(targetStructure, structureDefinition, behaviorDefinition);
		}else if(checkingBase == ConformityCheckingBase.SIGNATURES){
			return new de.fernuni.hagen.ma.gundermann.typkonverter.structureBySignatures.TypeConverter<T>(targetStructure, structureDefinition, behaviorDefinition);
		}
		return null;
	}
	
	
	
	
	

}
