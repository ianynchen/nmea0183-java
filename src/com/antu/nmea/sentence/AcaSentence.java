package com.antu.nmea.sentence;

import java.util.Date;

import com.antu.nmea.annotation.SentenceField;
import com.antu.nmea.util.UtcTime;

public class AcaSentence extends ParametricSentence {

	public AcaSentence() {
		super();
	}

	public AcaSentence(Date date) {
		super(date);
	}

	public AcaSentence(long currentTimeSinceEpochInSeconds) {
		super(currentTimeSinceEpochInSeconds);
	}

	@Override
	public String sentenceType() {
		return "aca";
	}

	@SentenceField(order = 1, fieldType = "integer")
	public int sequenceNumber;
	
	@SentenceField(order = 2, fieldType = "latitude")
	public double northEastCornerLatitude;
	
	@SentenceField(order = 3, fieldType = "longitude")
	public double northEastCornerLongitude;
	
	@SentenceField(order = 4, fieldType = "latitude")
	public double southWestCornerLatitude;
	
	@SentenceField(order = 5, fieldType = "longitude")
	public double southWestCornerLongitude;
	
	@SentenceField(order = 6, fieldType = "integer")
	public int transitionZoneSize;
	
	@SentenceField(order = 7, fieldType = "integer")
	public int channelANumber;
	
	@SentenceField(order = 8, fieldType = "integer")
	public int channelABandWidth;
	
	@SentenceField(order = 9, fieldType = "integer")
	public int channelBNumber;
	
	@SentenceField(order = 10, fieldType = "integer")
	public int channelBBandWidth;
	
	@SentenceField(order = 11, fieldType = "integer")
	public int tranceivingModeControl;
	
	@SentenceField(order = 12, fieldType = "integer")
	public int powerLevelControl;
	
	@SentenceField(order = 13, fieldType = "char")
	public char informationSource;
	
	@SentenceField(order = 14, fieldType = "integer", isRequired = false)
	public Integer inUseFlag;
	
	@SentenceField(order = 15, fieldType = "time", isRequired = false)
	public UtcTime timeOfInUseChange;
}
