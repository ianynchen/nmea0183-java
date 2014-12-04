package com.antu.nmea.sentence;

import java.util.Date;

public abstract class ParametricSentence extends NmeaSentence {

	public ParametricSentence() {
		super();
	}
	
	public ParametricSentence(Date date) {
		super(date);
	}
	
	public ParametricSentence(long currentTimeSinceEpochInSeconds) {
		super(currentTimeSinceEpochInSeconds);
	}

}
