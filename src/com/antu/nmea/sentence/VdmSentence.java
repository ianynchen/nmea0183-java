package com.antu.nmea.sentence;

import java.util.Date;

public class VdmSentence extends AbstractAisVesselSentence {

	public VdmSentence() {
		super();
	}

	public VdmSentence(Date date) {
		super(date);
	}

	public VdmSentence(long currentTimeSinceEpochInSeconds) {
		super(currentTimeSinceEpochInSeconds);
	}

	@Override
	public String sentenceType() {
		return "vdm";
	}

}
