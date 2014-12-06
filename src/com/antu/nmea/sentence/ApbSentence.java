package com.antu.nmea.sentence;

import java.util.Date;

import com.antu.nmea.annotation.SentenceField;

public class ApbSentence extends ParametricSentence {

	public ApbSentence() {
		super();
	}

	public ApbSentence(Date date) {
		super(date);
	}

	public ApbSentence(long currentTimeSinceEpochInSeconds) {
		super(currentTimeSinceEpochInSeconds);
	}

	@Override
	public String sentenceType() {
		return "apb";
	}

	@SentenceField(order = 1, fieldType = "boolean")
	public boolean isDataValid;
	
	@SentenceField(order = 2, fieldType = "boolean")
	public boolean isDataValid2;
	
	@SentenceField(order = 3, fieldType = "double")
	public double magnitudeOfXTE;
	
	@SentenceField(order = 4, fieldType = "char")
	public char directionToSteer;
	
	@SentenceField(order = 5, fieldType = "char")
	public char XTEUnits;
	
	@SentenceField(order = 6, fieldType = "boolean")
	public boolean isArrivalCircleEntered;
	
	@SentenceField(order = 7, fieldType = "boolean")
	public boolean isPerpendicularPassedAtWaypoint;
	
	@SentenceField(order = 8, fieldType = "double")
	public double bearingOriginToDestination;
	
	@SentenceField(order = 9, fieldType = "char")
	public char bearingTypeMagneticOrTrue;
	
	@SentenceField(order = 10, fieldType = "string")
	public String destinationWaypointId;
	
	@SentenceField(order = 11, fieldType = "double")
	public double bearingPresentToDestination;
	
	@SentenceField(order = 12, fieldType = "char")
	public char presentBearingTypeMagneticOrTrue;
	
	@SentenceField(order = 13, fieldType = "double")
	public double headingToSteerToDestination;
	
	@SentenceField(order = 14, fieldType = "char")
	public char headingToSteerToDestinationTypeMagneticOrTrue;
	
	@SentenceField(order = 15, fieldType = "char")
	public char modeIndicator;
}
