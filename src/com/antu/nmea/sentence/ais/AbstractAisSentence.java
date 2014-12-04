package com.antu.nmea.sentence.ais;

import java.util.Date;

import com.antu.nmea.sentence.EncapsulationSentence;
import com.antu.nmea.sentence.IEncapsulatedSentence;

public abstract class AbstractAisSentence extends EncapsulationSentence {
	
	public static final double RATE_OF_TURN_NOT_AVAILABLE = 10000;
	public static final double RATE_OF_TURN_EXCEED_LIMIT = 800;
	
	public static final double SPEED_EXCEED_LIMIT = 102.2;
	public static final double SPEED_NOT_AVAILABLE = 200;
	
	public static final double COURSE_NOT_AVAILABLE = 360;
	public static final double LATITUDE_NOT_AVAILABLE = 91;
	public static final double LONGITUDE_NOT_AVAILABLE = 181;
	public static final short HEADING_NOT_AVAILABLE = 511;
	
	public IEncapsulatedAisMessage aisMessage;

	public AbstractAisSentence() {
		super();
	}

	public AbstractAisSentence(Date date) {
		super(date);
	}

	public AbstractAisSentence(long currentTimeSinceEpochInSeconds) {
		super(currentTimeSinceEpochInSeconds);
	}
	
	@Override
	public IEncapsulatedSentence getEncapsulatedSentence() {
		return aisMessage;
	}
	
	@Override
	public void setEncapsulatedSentence(IEncapsulatedSentence sentence) {
		this.aisMessage = (IEncapsulatedAisMessage) sentence;
	}
}
