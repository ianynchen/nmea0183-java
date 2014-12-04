package test.util;

import static org.junit.Assert.*;

import org.junit.Test;

import com.antu.nmea.util.StringHelper;
import com.antu.util.GenericFactory;

public class FactoryTestCase {

	@Test
	public void testBySymbol() {
		
		GenericFactory<Object> factory = new GenericFactory<Object>("com.antu.nmea.sentence.field.codec", "?SentenceFieldCodec");
		
		try {
			Object obj = factory.createBySymbol(StringHelper.capitalizeFirstChar("integer"));
			
			assertNotNull(obj);
			assertEquals(obj.getClass(), com.antu.nmea.sentence.field.codec.IntegerSentenceFieldCodec.class);
		} catch (InstantiationException e) {
			fail(e.toString());
		} catch (IllegalAccessException e) {
			fail(e.toString());
		}
	}

}
