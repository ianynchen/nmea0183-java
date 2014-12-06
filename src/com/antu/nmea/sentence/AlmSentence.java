package com.antu.nmea.sentence;

import java.util.Date;

import com.antu.nmea.annotation.SentenceField;

public class AlmSentence extends ParametricSentence {

	public AlmSentence() {
		super();
	}

	public AlmSentence(Date date) {
		super(date);
	}

	public AlmSentence(long currentTimeSinceEpochInSeconds) {
		super(currentTimeSinceEpochInSeconds);
	}

	@Override
	public String sentenceType() {
		return "alm";
	}

	@SentenceField(order = 1, fieldType="integer")
	public int totalNumberOfSentences;
	
	@SentenceField(order = 2, fieldType="integer")
	public int sentenceNumber;
	
	@SentenceField(order = 3, fieldType="integer", fieldWidth = 2)
	public int satellitePRNNumber;
	
	@SentenceField(order = 4, fieldType="integer")
	public int gpsWeekNumber;
	
	@SentenceField(order = 5, fieldType="hex", fieldWidth = 2)
	public int svHealth;
	
	@SentenceField(order = 6, fieldType="hex", fieldWidth = 4)
	public int eccentricity;
	
	@SentenceField(order = 7, fieldType="hex", fieldWidth = 2)
	public int almanacReferenceTime;
	
	@SentenceField(order = 8, fieldType="hex", fieldWidth = 4)
	public int inclinationAngle;
	
	@SentenceField(order = 9, fieldType="hex", fieldWidth = 4)
	public int rateOfRightAscension;
	
	@SentenceField(order = 10, fieldType="hex", fieldWidth = 6)
	public int rootOfSemiMajorAxis;
	
	@SentenceField(order = 11, fieldType="hex", fieldWidth = 6)
	public int argumentOfPerigee;
	
	@SentenceField(order = 12, fieldType="hex", fieldWidth = 6)
	public int longtitudeOfAscensionNode;
	
	@SentenceField(order = 13, fieldType="hex", fieldWidth = 6)
	public int meanAnomaly;
	
	@SentenceField(order = 14, fieldType="hex", fieldWidth = 3)
	public int clockParameter1;
	
	@SentenceField(order = 15, fieldType="hex", fieldWidth = 3)
	public int clockParameter2;
}
