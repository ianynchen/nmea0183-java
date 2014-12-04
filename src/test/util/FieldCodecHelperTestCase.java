package test.util;
import org.junit.Test;

import com.antu.nmea.sentence.field.codec.FieldCodecHelper;


public class FieldCodecHelperTestCase {

	@Test
	public void testParseShort() {
		assert(FieldCodecHelper.parseShort("", 0, 0) == null);
		assert(FieldCodecHelper.parseShort("20", 10, 0) == null);
		assert(FieldCodecHelper.parseShort("20", 1, 3) == null);
		
		assert(FieldCodecHelper.parseShort("20", 1, 1) == 0);
		assert(FieldCodecHelper.parseShort("201", 1, 2) == 1);
		assert(FieldCodecHelper.parseShort("201", 0, 0) == 201);
	}

	@Test
	public void testParseInteger() {
		assert(FieldCodecHelper.parseInt("", 0, 0) == null);
		assert(FieldCodecHelper.parseInt("20", 10, 0) == null);
		assert(FieldCodecHelper.parseInt("20", 1, 3) == null);
		
		assert(FieldCodecHelper.parseInt("20", 1, 1) == 0);
		assert(FieldCodecHelper.parseInt("201", 1, 2) == 1);
		assert(FieldCodecHelper.parseInt("201.4", 0, 0) == 201.4);
	}

	@Test
	public void testParseDouble() {
		assert(FieldCodecHelper.parseDouble("", 0, 0) == null);
		assert(FieldCodecHelper.parseDouble("20", 10, 0) == null);
		assert(FieldCodecHelper.parseDouble("20", 1, 3) == null);
		
		assert(FieldCodecHelper.parseDouble("20", 1, 1) == 0);
		assert(FieldCodecHelper.parseDouble("20.1", 1, 3) == 0.1);
		assert(FieldCodecHelper.parseDouble("201", 0, 3) == 201);
	}
}
