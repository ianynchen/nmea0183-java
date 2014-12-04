package com.antu.nmea.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UtcDate {
	
	public int year;
	public int month;
	public int day;

	public boolean isValid() {
		
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, day, 0, 0, 0);
		
		Date date = cal.getTime();
		
		SimpleDateFormat sdf = new SimpleDateFormat();
		
		try {
			sdf.parse(date.toString());
			return true;
		} catch (ParseException e) {
			return false;
		}
	}
}
