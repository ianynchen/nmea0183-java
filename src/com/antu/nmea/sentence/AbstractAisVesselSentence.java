package com.antu.nmea.sentence;

import java.util.Date;

import com.antu.nmea.annotation.SentenceField;
import com.antu.nmea.sentence.ais.AbstractAisSentence;

public abstract class AbstractAisVesselSentence extends AbstractAisSentence {

	public AbstractAisVesselSentence() {
		super();
	}

	public AbstractAisVesselSentence(Date date) {
		super(date);
	}

	public AbstractAisVesselSentence(long currentTimeSinceEpochInSeconds) {
		super(currentTimeSinceEpochInSeconds);
	}
	
	@SentenceField(order = 1, fieldType = "integer")
	public int totalNumberOfSentences = 1;
	
	@SentenceField(order = 2, fieldType = "integer")
	public int sentenceNumber = 1;
	
	@SentenceField(order = 3, fieldType = "integer", isRequired = false)
	public Integer sequentialMessageIdentifier = null;
	
	@SentenceField(order = 4, fieldType = "char")
	public char aisChannel;
	
	@SentenceField(order = 5, fieldType = "string", isIgnoredInReconstruction = true)
	public String encapsulatedContent;
	
	@SentenceField(order = 6, fieldType = "integer", isIgnoredInReconstruction = true)
	public int fillBits;
	
	@Override
	public String getEncapsulatedData() {
		return this.encapsulatedContent;
	}

	@Override
	public void setEncapsulatedData(String content) {
		this.encapsulatedContent = content;
	}
	
	@Override
	public int getFillBits() {
		return this.fillBits;
	}
	
	@Override
	public void setFillBits(int fillBits) {
		this.fillBits = fillBits;
	}
	
	@Override
	public void increaseSentenceNumber() {
		this.sentenceNumber++;
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
		this.sequentialMessageIdentifier = sequence;
	}

	@Override
	public int getSequentialMessageId() {
		return this.sequentialMessageIdentifier;
	}

	@Override
	public int getTotalNumberOfSentences() {
		return this.totalNumberOfSentences;
	}

	@Override
	public int getSentenceNumber() {
		return this.sentenceNumber;
	}
}
