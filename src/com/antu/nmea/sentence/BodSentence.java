package com.antu.nmea.sentence;

import java.util.Date;

import com.antu.nmea.annotation.SentenceField;

public class BodSentence extends ParametricSentence {

	public BodSentence() {
		super();
	}

	public BodSentence(Date date) {
		super(date);
	}

	public BodSentence(long currentTimeSinceEpochInSeconds) {
		super(currentTimeSinceEpochInSeconds);
	}

	@Override
	public String sentenceType() {
		return "bod";
	}

	@SentenceField(order = 1, fieldType = "double")
	public double bearingDegreesTrue;
	
	@SentenceField(order = 2, fieldType = "char")
	public char bearingTypeTrue;
	
	@SentenceField(order = 3, fieldType = "double")
	public double bearingDegreesMagnetic;
	
	@SentenceField(order = 4, fieldType = "char")
	public char bearingTypeMagnetic;
	
	@SentenceField(order = 5, fieldType = "string")
	public String destinationWaypointId;
	
	@SentenceField(order = 6, fieldType = "string")
	public String originWaypointId;
}
