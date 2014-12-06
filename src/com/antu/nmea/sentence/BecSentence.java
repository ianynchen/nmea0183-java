package com.antu.nmea.sentence;

import java.util.Date;

import com.antu.nmea.annotation.SentenceField;
import com.antu.nmea.util.UtcTime;

public class BecSentence extends ParametricSentence {

	public BecSentence() {
		super();
	}

	public BecSentence(Date date) {
		super(date);
	}

	public BecSentence(long currentTimeSinceEpochInSeconds) {
		super(currentTimeSinceEpochInSeconds);
	}

	@Override
	public String sentenceType() {
		return "bec";
	}

	@SentenceField(order = 1, fieldType = "time")
	public UtcTime utcOfObservation;
	
	@SentenceField(order = 2, fieldType = "latitude")
	public double waypointLatitude;
	
	@SentenceField(order = 3, fieldType = "longitude")
	public double waypointLongitude;
	
	@SentenceField(order = 4, fieldType = "double")
	public double trueBearing;
	
	@SentenceField(order = 5, fieldType = "char")
	public char bearingTypeTrue;
	
	@SentenceField(order = 6, fieldType = "double")
	public double magneticBearing;
	
	@SentenceField(order = 7, fieldType = "char")
	public char bearingTypeMagnetic;
	
	@SentenceField(order = 8, fieldType = "double")
	public double distance;
	
	@SentenceField(order = 9, fieldType = "char")
	public char distanceUnitNauticleMiles;
	
	@SentenceField(order = 10, fieldType = "string")
	public String waypointId;
}
