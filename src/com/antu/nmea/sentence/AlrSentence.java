package com.antu.nmea.sentence;

import java.util.Date;

import com.antu.nmea.annotation.SentenceField;
import com.antu.nmea.util.UtcTime;

public class AlrSentence extends ParametricSentence {

	public AlrSentence() {
		super();
	}

	public AlrSentence(Date date) {
		super(date);
	}

	public AlrSentence(long currentTimeSinceEpochInSeconds) {
		super(currentTimeSinceEpochInSeconds);
	}

	@Override
	public String sentenceType() {
		return "alr";
	}

	@SentenceField(order = 1, fieldType = "time")
	public UtcTime timeOfAlarmConditionChange;
	
	@SentenceField(order = 2, fieldType = "integer")
	public int uniqueAlarmNumberAtAlarmSource;
	
	@SentenceField(order = 3, fieldType = "boolean")
	public boolean thresholdExceeded;
	
	@SentenceField(order = 4, fieldType = "boolean")
	public boolean alarmStateAcknowledged;
	
	@SentenceField(order = 5, fieldType = "string")
	public String alarmDescription;
}
