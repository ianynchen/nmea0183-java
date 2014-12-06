package test.util;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.junit.Before;
import org.junit.Test;

import com.antu.nmea.codec.exception.SentenceCodecNotFoundException;
import com.antu.nmea.codec.exception.SentenceFieldCodecNotFoundException;
import com.antu.nmea.sentence.NmeaSentence;

public class TutTestCase {

	@Before
	public void setUp() throws Exception {
		System.setProperty("org.apache.commons.logging.Log","org.apache.commons.logging.impl.SimpleLog");
		System.setProperty("org.apache.commons.logging.simplelog.defaultlog", "trace");
	}

	@Test
	public void testDecode() {
		final com.antu.nmea.codec.CodecManager manager = new com.antu.nmea.codec.CodecManager();
		manager.addObserver(new Observer() {

			@Override
			public void update(Observable o, Object arg) {
				
				if (arg instanceof NmeaSentence) {
					System.out.println(arg.toString());
					
					List<String> result = manager.encode("IN", (NmeaSentence)arg);
					System.out.println("Encoded message: " + result.get(0));
					assertEquals(1, result.size());
					assertEquals("$INTUT,SD,01,01,0,A,5368616C6C6F7720576174657221*4A\r\n", result.get(0));
				}
			}
		
		});
		manager.acceptanceList().addAllMessages("tut");
		try {
			manager.decode("$INTUT,SD,01,01,0,A,5368616C6C6F7720576174657221*4A\r\n");
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | SentenceFieldCodecNotFoundException
				| SentenceCodecNotFoundException e) {
			e.printStackTrace();
		}
	}

}
