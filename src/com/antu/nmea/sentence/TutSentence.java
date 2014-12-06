package com.antu.nmea.sentence;

import java.util.Date;

import com.antu.nmea.annotation.SentenceField;

public class TutSentence extends NmeaSentence {

	public TutSentence() {
		super();
	}

	public TutSentence(Date date) {
		super(date);
	}

	public TutSentence(long currentTimeSinceEpochInSeconds) {
		super(currentTimeSinceEpochInSeconds);
	}

	@Override
	public String sentenceType() {
		return "tut";
	}

	@SentenceField(order = 1, fieldType = "string")
	public String sourceIdentifier;
	
	@SentenceField(order = 2, fieldType = "string")
	public String translationCode;

	@SentenceField(order = 3, fieldType = "string")
	public String textBody;
	
	public boolean isComplete = true;
	
	public boolean isTranslated = true;
}
