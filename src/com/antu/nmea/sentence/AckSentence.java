package com.antu.nmea.sentence;

import java.util.Date;

import com.antu.nmea.annotation.SentenceField;

public class AckSentence extends ParametricSentence {

	public AckSentence() {
		super();
	}

	public AckSentence(Date date) {
		super(date);
	}

	public AckSentence(long currentTimeSinceEpochInSeconds) {
		super(currentTimeSinceEpochInSeconds);
	}

	@Override
	public String sentenceType() {
		return "ack";
	}

	@SentenceField(order = 1, fieldType = "integer")
	public int alarmNumberAtSource;
}
