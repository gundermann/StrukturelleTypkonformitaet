package matching.methods;

import java.lang.reflect.Method;

class MethodStructure {
  private final Class<?> returnType;

  private final Class<?>[] sortedArgumentTypes;

  static MethodStructure createFromDeclaredMethod( Method method ) {
    return new MethodStructure( method.getReturnType(), method.getParameterTypes() );
  }

  private MethodStructure( Class<?> returnType, Class<?>[] sortedArgumentTypes ) {
    this.returnType = returnType;
    this.sortedArgumentTypes = sortedArgumentTypes != null ? sortedArgumentTypes : new Class<?>[] {};
  }

  @Override
  public int hashCode() {
    int prime = 7;
    int hash = prime * returnType.hashCode();
    for ( int i = 0; i < sortedArgumentTypes.length; i++ ) {
      hash += prime * sortedArgumentTypes[i].hashCode();
    }
    return hash;
  }

  @Override
  public boolean equals( Object obj ) {
    if ( obj instanceof MethodStructure ) {
      MethodStructure other = (MethodStructure) obj;
      if ( returnType != other.returnType ) {
        return false;
      }
      if ( sortedArgumentTypes.length != other.sortedArgumentTypes.length ) {
        return false;
      }
      for ( int i = 0; i < sortedArgumentTypes.length; i++ ) {
        if ( sortedArgumentTypes[i] != other.sortedArgumentTypes[i] ) {
          return false;
        }
      }
      return true;
    }
    return false;
  }

  public Class<?> getReturnType() {
    return returnType;
  }

  public Class<?>[] getSortedArgumentTypes() {
    return sortedArgumentTypes;
  }

}