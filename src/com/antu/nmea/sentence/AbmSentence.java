package com.antu.nmea.sentence;

//import java.util.ArrayList;
import java.util.Date;

import com.antu.nmea.annotation.SentenceField;
import com.antu.nmea.sentence.ais.AbstractAisSentence;

public class AbmSentence extends AbstractAisSentence {
	
	//private ArrayList<Byte> content = new ArrayList<Byte>();

	public AbmSentence() {
		super();
	}

	public AbmSentence(Date date) {
		super(date);
	}

	public AbmSentence(long currentTimeSinceEpochInSeconds) {
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
		return "abm";
	}

	@SentenceField(order = 1, fieldType = "integer")
	public int totalNumberOfSentences;

	@SentenceField(order = 2, fieldType = "integer")
	public int sentenceNumber;

	@SentenceField(order = 3, fieldType = "integer")
	public int sequentialMessageId;
	
	@SentenceField(order = 4, fieldType = "integer", fieldWidth = 9, isRequired = false)
	public Integer destinationMMSI;
	
	@SentenceField(order = 5, fieldType = "integer", isRequired = false)
	public Integer aisChannelForBroadcast;
	
	@SentenceField(order = 5, fieldType = "integer", isRequired = false, fieldWidth = 9)
	public Integer messageId;
	
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
