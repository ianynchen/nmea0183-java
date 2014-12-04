package com.antu.nmea.sentence;

/**
 * 
 * @author yining
 *
 * A grouped sentence provides the interface for multi-sentence sentences
 * as well as encapsulation sentences
 */
public interface IGroupedSentence extends INmeaSentence {

	int getSequentialMessageId();
	
	int getTotalNumberOfSentences();
	
	int getSentenceNumber();
	
	boolean isComplete();
	
	void concatenate(IGroupedSentence sentence);
}
