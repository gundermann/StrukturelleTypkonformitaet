package matching.types;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import matching.types.TypeMatchingInfo;
import matching.types.TypeMatchingInfoFactory;

public class TypeMatchingInfoFactoryTest {

  @Test
  public void create() {
    Class<Integer> intClass = Integer.class;
    TypeMatchingInfoFactory factory = new TypeMatchingInfoFactory( intClass, intClass );
    TypeMatchingInfo info = factory.create();
    assertThat( info.getSource(), equalTo( intClass ) );
    assertThat( info.getTarget(), equalTo( intClass ) );
  }

}
