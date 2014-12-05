package test.util;

import static org.junit.Assert.*;

import org.junit.Test;

import com.antu.nmea.message.field.codec.MessageFieldCodecHelper;
import com.antu.util.PrintableList;

public class MessageFieldCodecHelperTestCase {

	@Test
	public void testParseByte() {
		
		PrintableList<Byte> bits = new PrintableList<Byte>();
		bits.add((byte) 1);
		bits.add((byte) 1);
		bits.add((byte) 0);
		bits.add((byte) 0);
		bits.add((byte) 0);
		bits.add((byte) 0);
		
		Short value = MessageFieldCodecHelper.parseShort(bits, 0, 6, false);
		
		assertNotNull(value);
		assertEquals(new Short((short) 48), value);
		
		value = MessageFieldCodecHelper.parseShort(bits, 0, 6, true);
		
		assertNotNull(value);
		assertEquals(new Short((short) -16), value);
		
		PrintableList<Byte> newBits = new PrintableList<Byte>();
		assertEquals(true, MessageFieldCodecHelper.shortToBits(newBits, value, 6, true));
		assertEquals(bits, newBits);
	}
}
