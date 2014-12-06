package test.util;

import java.util.BitSet;

import org.junit.Before;
import org.junit.Test;

public class BitSetTestCase {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testShort() {
		
		BitSet bs = BitSet.valueOf(new long[]{8});
		System.out.println("Length: " + bs.length());
		
		System.out.println("content: " + bs.toString());
		
		bs = BitSet.valueOf(new long[] {612});
		System.out.println("content: " + bs.toString());
	}

}
