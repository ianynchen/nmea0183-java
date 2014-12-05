package test.util;

import static org.junit.Assert.*;

import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;

import com.antu.nmea.util.StringHelper;
import com.antu.util.GenericFactory;

public class GenericFactoryTestCase {

	@Before
	public void setUp() throws Exception {
		System.setProperty("org.apache.commons.logging.Log","org.apache.commons.logging.impl.SimpleLog");
		System.setProperty("org.apache.commons.logging.simplelog.defaultlog", "debug");
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
	}

	@Test
	public void test() {
		
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
