package test.util;

import org.junit.Test;

import com.antu.nmea.util.UtcDate;

public class DateTimeTestCase {

	@Test
	public void testDate() {
		
		UtcDate d1 = new UtcDate();
		d1.year = 1999;
		d1.month = 13;
		d1.day = 1;
		
		assert(d1.isValid() == false);
		
		UtcDate d2 = new UtcDate();
		d2.year = 1999;
		d2.month = 1;
		d2.day = 32;
		
		assert(d2.isValid() == false);
		
		UtcDate d3 = new UtcDate();
		d3.year = 1999;
		d3.month = 2;
		d3.day = 28;
		
		assert(d3.isValid() == false);
		
		UtcDate d4 = new UtcDate();
		d4.year = 2000;
		d4.month = 2;
		d4.day = 28;
		
		assert(d4.isValid() == true);
		
		UtcDate d5 = new UtcDate();
		d5.year = 2000;
		d5.month = 1;
		d5.day = 31;
		
		assert(d5.isValid() == true);
	}

}
