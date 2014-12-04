package com.antu.nmea.sentence;

import java.util.Date;

public class VdoSentence extends AbstractAisVesselSentence {

	public VdoSentence() {
		super();
	}

	public VdoSentence(Date date) {
		super(date);
	}

	public VdoSentence(long currentTimeSinceEpochInSeconds) {
		super(currentTimeSinceEpochInSeconds);
	}

	@Override
	public String sentenceType() {
		return "vdo";
	}

}
