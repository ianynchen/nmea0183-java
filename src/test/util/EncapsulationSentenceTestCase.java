package test.util;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.junit.Test;

import com.antu.nmea.codec.exception.SentenceCodecNotFoundException;
import com.antu.nmea.codec.exception.SentenceFieldCodecNotFoundException;
import com.antu.nmea.sentence.EncapsulationSentence;
import com.antu.nmea.sentence.NmeaSentence;

public class EncapsulationSentenceTestCase {
	
	public void setUp() {
		System.setProperty("org.apache.commons.logging.Log","org.apache.commons.logging.impl.SimpleLog");
		System.setProperty("org.apache.commons.logging.simplelog.defaultlog", "trace");
	}

	@Test
	public void testBitConversion() {
		Byte[] bits = EncapsulationSentence.convertStringToBits("1P000Oh1IT1svTP2r:43grwb05q4");
		
		assertEquals(168, bits.length);
		
		String content = EncapsulationSentence.convertSixBitsToString(bits);
		
		assertEquals("1P000Oh1IT1svTP2r:43grwb05q4", content);
	}

	@Test
	public void testMessage1() {
		final com.antu.nmea.codec.CodecManager manager = new com.antu.nmea.codec.CodecManager();
		manager.addObserver(new Observer() {

			@Override
			public void update(Observable o, Object arg) {
				
				if (arg instanceof NmeaSentence) {
					System.out.println(arg.toString());
					
					List<String> result = manager.encode("AI", (NmeaSentence)arg);
					System.out.println("Encoded message: " + result.get(0));
					assertEquals(1, result.size());
					assertEquals("!AIVDM,1,1,,1,1P000Oh1IT1svTP2r:43grwb05q4,0*01\r\n", result.get(0));
				}
			}
		
		});
		manager.acceptanceList().addAllMessages("vdm");
		
		try {
			manager.decode("!AIVDM,1,1,,1,1P000Oh1IT1svTP2r:43grwb05q4,0*01\r\n");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SentenceFieldCodecNotFoundException e) {
			e.printStackTrace();
		} catch (SentenceCodecNotFoundException e) {
			e.printStackTrace();
		}
	}
}
