package com.antu.nmea.util;

public class UtcTime {
	
	public int hour;
	public int minute;
	public double second;
	
	public boolean isValid() {
		if (0 <= hour && hour < 24 &&
			0 <= minute && minute < 60 &&
			0 <= second && second < 60)
			return true;
		return false;
	}

}
