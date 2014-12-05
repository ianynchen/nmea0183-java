package com.antu.nmea.sentence;

import java.util.Date;

public class QuerySentence extends NmeaSentence {

	public QuerySentence() {
		super();
	}
	
	public QuerySentence(Date date) {
		super(date);
	}
	
	public QuerySentence(long currentTimeSinceEpochInSeconds) {
		super(currentTimeSinceEpochInSeconds);
	}
	
	@Override
	public String sentenceType() {
		return "query";
	}

	public String requestTalker = "";
	
	public String requestedTalker = "";
	
	public String query = "";
}
