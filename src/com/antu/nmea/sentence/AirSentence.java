package com.antu.nmea.sentence;

import java.util.Date;

import com.antu.nmea.annotation.SentenceField;

public class AirSentence extends ParametricSentence {

	public AirSentence() {
		super();
	}

	public AirSentence(Date date) {
		super(date);
	}

	public AirSentence(long currentTimeSinceEpochInSeconds) {
		super(currentTimeSinceEpochInSeconds);
	}

	@Override
	public String sentenceType() {
		return "air";
	}

	@SentenceField(order = 1, fieldType="integer", fieldWidth = 9)
	public int mmsiOfInterrogatedStation1;
	
	@SentenceField(order = 2, fieldType="integer")
	public int messageRequestedFromStation1;
	
	@SentenceField(order = 3, fieldType="integer", isRequired = false)
	public Integer messageSubSection1ForStation1;
	
	@SentenceField(order = 4, fieldType="integer")
	public int numberOfSecondMessageRequestedFromStation1 = -1;
	
	@SentenceField(order = 5, fieldType="integer", isRequired = false)
	public Integer messageSubSection2ForStation1;
	
	@SentenceField(order = 6, fieldType="integer", fieldWidth = 9)
	public int mmsiOfInterrogatedStation2 = -1;
	
	@SentenceField(order = 7, fieldType="integer")
	public int numberOfMessageRequestedFromStation2 = -1;
	
	@SentenceField(order = 8, fieldType="integer")
	public int messageSubSection1ForStation2 = -1;
}
