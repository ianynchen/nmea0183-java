package com.antu.nmea.sentence;

import java.util.Date;

import com.antu.nmea.annotation.SentenceField;
import com.antu.nmea.sentence.ais.AbstractAisSentence;

public class BbmSentence extends AbstractAisSentence {

	public BbmSentence() {
		super();
	}

	public BbmSentence(Date date) {
		super(date);
	}

	public BbmSentence(long currentTimeSinceEpochInSeconds) {
		super(currentTimeSinceEpochInSeconds);
	}

	@Override
	public int getSequentialMessageId() {
		return sequentialMessageId;
	}

	@Override
	public int getTotalNumberOfSentences() {
		return totalNumberOfSentences;
	}

	@Override
	public int getSentenceNumber() {
		return sentenceNumber;
	}

	@Override
	public String sentenceType() {
		return "bbm";
	}

	@SentenceField(order = 1, fieldType = "integer")
	public int totalNumberOfSentences;

	@SentenceField(order = 2, fieldType = "integer")
	public int sentenceNumber;

	@SentenceField(order = 3, fieldType = "integer")
	public int sequentialMessageId;
	
	@SentenceField(order = 4, fieldType = "integer", fieldWidth = 9)
	public int destinationMMSI;
	
	@SentenceField(order = 5, fieldType = "integer")
	public int aisChannelForBroadcast;
	
	@SentenceField(order = 5, fieldType = "integer", fieldWidth = 2)
	public int messageId;
	
	@SentenceField(order = 6, fieldType = "string", isIgnoredInReconstruction = true)
	public String encapsulatedData;
	
	@SentenceField(order = 7, fieldType = "integer", isIgnoredInReconstruction = true)
	public int fillBits;

	@Override
	public String getEncapsulatedData() {
		return this.encapsulatedData;
	}

	@Override
	public int getFillBits() {
		return this.fillBits;
	}

	@Override
	public void increaseSentenceNumber() {

		this.sentenceNumber++;
		
	}

	@Override
	public void setEncapsulatedData(String content) {

		this.encapsulatedData = content;
	}

	@Override
	public void setFillBits(int fillBits) {
		
		this.fillBits = fillBits;
	}

	@Override
	public void setTotalSentenceCount(int total) {
		
		this.totalNumberOfSentences = total;
	}

	@Override
	public void setSentenceNumber(int number) {
		
		this.sentenceNumber = number;
	}

	@Override
	public void setSequenceId(Integer sequence) {
		
		this.setSequenceId(sequence);
	}
}
