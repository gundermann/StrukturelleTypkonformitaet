package de.fernuni.hagen.ma.gundermann.typkonverter;

import de.fernuni.hagen.ma.gundermann.typkonverter.structureByNames.StructureByNamesTypeConverter;
import de.fernuni.hagen.ma.gundermann.typkonverter.structureBySignatures.StructureBySignatureTypeConverter;

public class TypeConverterBuilder {
	
	private final ConformityCheckingBase checkingBase;
	private StructureDefinition structureDefinition = StructureDefinition.ALL_METHODS_NECESSARY;
	private BehaviorDefinition behaviorDefinition;


	public static TypeConverterBuilder create(ConformityCheckingBase checkingBase) {
		return new TypeConverterBuilder(checkingBase);
	}
	
	
	private TypeConverterBuilder(ConformityCheckingBase checkingBase) {
		this.checkingBase = checkingBase;
	}
	
	public TypeConverterBuilder withStructureDefinition(StructureDefinition structureDefinition) {
		this.structureDefinition = structureDefinition;
		return this;
	}
	
	public <T> TypeConverter<T> build(Class<T> targetStructure){
		if(checkingBase == ConformityCheckingBase.NAMES) {
			return new StructureByNamesTypeConverter<T>(targetStructure, structureDefinition, behaviorDefinition);
		}else if(checkingBase == ConformityCheckingBase.SIGNATURES){
			return new StructureBySignatureTypeConverter<T>(targetStructure, structureDefinition, behaviorDefinition);
		}
		return null;
	}
	
	
	
	
	

}
