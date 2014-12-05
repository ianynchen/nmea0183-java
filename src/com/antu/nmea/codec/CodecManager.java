package com.antu.nmea.codec;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.antu.nmea.codec.exception.MessageFieldCodecNotFoundException;
import com.antu.nmea.codec.exception.SentenceCodecNotFoundException;
import com.antu.nmea.codec.exception.SentenceFieldCodecNotFoundException;
import com.antu.nmea.sentence.EncapsulationSentence;
import com.antu.nmea.sentence.INmeaSentence;
import com.antu.nmea.sentence.MultiSentenceTable;
import com.antu.nmea.sentence.ParametricSentence;
import com.antu.nmea.util.Buffer;
import com.antu.nmea.util.StringHelper;
import com.antu.util.GenericFactory;

/**
 * 
 * @author yining
 *
 * CodecManager is the entry point for all encoding and decoding of NMEA sentences from a single source,
 * ie. each CodecManager should be instantiated and configured for each source generating NMEA messages,
 * may it be a server socket, a client socket, a file or a serial port.
 * 
 * A CodecManager implements Observer and extends Observable, it gets notified from underlying
 * AbstractNmeaSentenceCodecs(Observable) when a sentence is fully decoded. Programs using a CodecManager
 * should implement Oberver interface and add to this Observable to receive decoded sentence objects.
 */
public class CodecManager extends Observable implements Observer {
	
	private Buffer buffer;
	private ParametricSentenceCodec parametricSentenceCodec;
	private QuerySentenceCodec querySentenceCodec;
	
	private GenericFactory<AbstractNmeaSentenceCodec> multiSentenceCodecFactory;
	private GenericFactory<AbstractNmeaSentenceCodec> encapsulationSentenceCodecFactory;
	private GenericFactory<AbstractNmeaSentenceCodec> proprietarySentenceCodecFactory;

	private AcceptanceList acceptanceList;

	static private Log logger = LogFactory.getLog(GenericFactory.class);

	/**
	 * Constructor for CodecManager
	 */
	public CodecManager() {
		
		this.buffer = new Buffer();
		this.parametricSentenceCodec = new ParametricSentenceCodec();
		this.querySentenceCodec = new QuerySentenceCodec();
		
		this.parametricSentenceCodec.addObserver(this);
		this.querySentenceCodec.addObserver(this);
		this.acceptanceList = new AcceptanceList();
		
		this.multiSentenceCodecFactory = new GenericFactory<AbstractNmeaSentenceCodec>("com.antu.nmea.codec", "?SentenceCodec");
		this.encapsulationSentenceCodecFactory = new GenericFactory<AbstractNmeaSentenceCodec>("com.antu.nmea.codec", "?SentenceCodec");
		this.proprietarySentenceCodecFactory = new GenericFactory<AbstractNmeaSentenceCodec>("com.antu.nmea.codec", "?SentenceCodec");
	}
	
	/**
	 * AcceptanceList is used to filter out certain sentence types. CodecManagers can
	 * receive all kinds of NMEA sentences and some may not need to be encoded/decoded
	 * based on different usage purposes. Use of this list can filter-out unwanted sentences
	 * and hence saving encoding/decoding time and computer resources.
	 * 
	 * <strong>By default, it accepts no sentences.</strong>
	 * 
	 * @return AcceptanceList for the codec manager
	 */
	public AcceptanceList acceptanceList() {
		return this.acceptanceList;
	}
	
	/**
	 * Decodes a sentence with timestamp.
	 * 
	 * @param timestamp the time the sentence is received.
	 * @param content actual content of the sentence.
	 * @throws SentenceFieldCodecNotFoundException
	 * @throws SentenceCodecNotFoundException
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 */
	public void decode(Date timestamp, String content) 
			throws SentenceFieldCodecNotFoundException, SentenceCodecNotFoundException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		this.decode(timestamp, content.toCharArray());
	}
	
	/**
	 * Decodes a sentence with current time as the timestamp
	 * @param content
	 * @throws SentenceFieldCodecNotFoundException
	 * @throws SentenceCodecNotFoundException
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public void decode(String content) 
			throws SentenceFieldCodecNotFoundException, SentenceCodecNotFoundException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		this.decode(Calendar.getInstance().getTime(), content);
	}
	
	/**
	 * Decodes a sentence with time in milliseconds from epoch as the time
	 * @param timeInMillis
	 * @param content
	 * @throws SentenceFieldCodecNotFoundException
	 * @throws SentenceCodecNotFoundException
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public void decode(long timeInMillis, String content) 
			throws SentenceFieldCodecNotFoundException, SentenceCodecNotFoundException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timeInMillis);
		this.decode(cal.getTime(), content);
	}
	
	protected short hexValue(char c) {
		switch (c) {
		case 'A':
		case 'a':
			return 10;
		case 'B':
		case 'b':
			return 11;
		case 'C':
		case 'c':
			return 12;
		case 'D':
		case 'd':
			return 13;
		case 'E':
		case 'e':
			return 14;
		case 'F':
		case 'f':
			return 15;
			
		case '1':
			return 1;
		case '2':
			return 2;
		case '3':
			return 3;
		case '4':
			return 4;
		case '5':
			return 5;
		case '6':
			return 6;
		case '7':
			return 7;
		case '8':
			return 8;
		case '9':
			return 9;
			
		default:
			return 0;
		}
	}
	
	/**
	 * Finds out if the checksum is valid. Only does the checking
	 * if the sentence comes with a checksum.
	 * @param sentence
	 * @return true if checksum is valid, false otherwise.
	 */
	protected boolean isChecksumValid(String sentence) {
		short checksum = 0;
		
		int checksumIndex = sentence.lastIndexOf('*');
		
		if (sentence.length() < checksumIndex + 3)
			return false;
		
		checksum = AbstractNmeaSentenceCodec.checksum(sentence);
		
		short high = (short) ((checksum & 0x00F0) >> 4);
		short low = (short)(checksum & 0x000F);
		
		return (high == this.hexValue(sentence.charAt(checksumIndex + 1))) &&
				(low == this.hexValue(sentence.charAt(checksumIndex + 2))); 
	}
	
	public void decode(char[] content) 
			throws SentenceFieldCodecNotFoundException, SentenceCodecNotFoundException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		this.decode(Calendar.getInstance().getTime(), content);
	}
	
	public void decode(long timeInMillis, char[] content) 
			throws SentenceFieldCodecNotFoundException, SentenceCodecNotFoundException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timeInMillis);
		this.decode(cal.getTime(), content);
	}
	
	public void decode(Date timestamp, char[] content) {
		
		CodecManager.logger.debug("received: " + new String(content));
		
		// break into complete NMEA sentences
		ArrayList<String> rawSentences = this.buffer.append(content);
		
		// for each sentence, do
		for (String rawSentence : rawSentences) {
			
			CodecManager.logger.debug("decoding sentence: " + rawSentence);

			// if there is a checksum, verify it, otherwise, pass the sentence
			int checkSumIndex = rawSentence.lastIndexOf('*');
			if (checkSumIndex >= 0) {
				if (!this.isChecksumValid(rawSentence)) {
					CodecManager.logger.info("sentence checksum validation failed.");
					continue;
				}
				CodecManager.logger.info("sentence checksum validation passed.");
			}
			
			// break sentence into segments separated by ','
			String[] segments = null;
			if (checkSumIndex >= 0) {
				segments = rawSentence.substring(0, checkSumIndex).trim().split(",");
			} else {
				segments = rawSentence.trim().split(",");
			}
			CodecManager.logger.info("sentence segmented.");

			try {
				// proprietary sentence
				if (rawSentence.startsWith("$P") || rawSentence.startsWith("!P")) {
					CodecManager.logger.info("processing as proprietary sentence.");
					
					String sentenceType = segments[0].substring(2);
					
					if (sentenceType == null || sentenceType.length() < 3) {
						CodecManager.logger.error("cannot determine sentence type for: " + rawSentence);
						continue;
					}
					
					sentenceType = sentenceType.substring(0, 3).toLowerCase();
						
					CodecManager.logger.debug("trying to resolve sentence type: " + sentenceType);
					
					if (this.acceptanceList.shouldAcceptProprietary(sentenceType)) {
						CodecManager.logger.info("sentenceType: " + sentenceType + " is accepted.");
						
						AbstractNmeaSentenceCodec codec = this.proprietarySentenceCodecFactory.getBySymbol(
										StringHelper.capitalizeFirstChar(sentenceType));
						
						if (codec != null) {
							codec.addObserver(this);
							codec.decode(timestamp, sentenceType, segments);
						} else {
							CodecManager.logger.error("unable to find proprietary codec for sentence type: " + sentenceType);
						}
					} else {
						CodecManager.logger.info("sentenceType: " + sentenceType + " is not in acceptance list.");
					}
				} else if (segments[0].startsWith("$") &&
						   segments[0].length() == 6 && 
						   segments[0].charAt(5) == 'Q') { // query sentence
					CodecManager.logger.info("processing as query sentence.");
					this.querySentenceCodec.decode(timestamp, null, segments);
				} else { // parametric or encapsulation sentence
					
					// proof check, make sure sentence header is valid
					if (segments[0].length() == 6) {
						String sentenceType = segments[0].substring(3).toLowerCase();
						CodecManager.logger.debug("trying to resolve sentence type: " + sentenceType);
						
						// if this codec manager is allowed to perform
						// codec operation on this sentence
						if (this.acceptanceList.shouldAcceptPublic(sentenceType)) {
							CodecManager.logger.info("sentence type accepted.");
							
							// parametric sentence
							if (segments[0].startsWith("$")) {
								CodecManager.logger.info("processing as parametric/multi-sentence sentence.");
								// first check to see if it's a multisentence
								if (MultiSentenceTable.instance().isMultiSentenceType(sentenceType)) {
									CodecManager.logger.info("processing as multi-sentence sentence.");
									AbstractNmeaSentenceCodec codec = //this.getMultiSentenceCodec(sentenceType);
											this.multiSentenceCodecFactory.getBySymbol(
													StringHelper.capitalizeFirstChar(sentenceType));
									
									if (codec != null) {
										codec.addObserver(this);
										CodecManager.logger.info("codec located.");
										codec.decode(timestamp, sentenceType, segments);
									} else {
										CodecManager.logger.error("unable to find multi-sentence codec for sentence type: " + sentenceType);
									}
								} else { // no, do as a regular parametric sentence.
									CodecManager.logger.info("processing as parametric sentence.");
									this.parametricSentenceCodec.decode(timestamp, sentenceType, segments);
								}
							}
							else if (segments[0].startsWith("!")) { // encapsulation sentence
								CodecManager.logger.info("processing as encapsulation sentence.");
								
								AbstractNmeaSentenceCodec codec = //this.getEncapsulationSentenceCodec(sentenceType);
										this.encapsulationSentenceCodecFactory.getBySymbol(
												StringHelper.capitalizeFirstChar(sentenceType));
	
								if (codec != null) {
									codec.addObserver(this);
									CodecManager.logger.info("codec for encapsulation sentence located.");
									codec.decode(timestamp, sentenceType, segments);
								} else {
									CodecManager.logger.error("unable to find encapsulation sentence codec for sentence type: " + sentenceType);
								}
							}
						} else {
							CodecManager.logger.info("sentence type: " + sentenceType + " not accepted.");
						}
					}
				}
			} catch (ClassNotFoundException
					| InstantiationException
					| IllegalAccessException
					| SentenceFieldCodecNotFoundException e) {
				CodecManager.logger.error("error parsing sentence: " + rawSentence, e);
			}
		}
	}

	@Override
	public void update(Observable o, Object arg) {

		CodecManager.logger.info("parsed object received: " + arg.toString());
		if (arg instanceof INmeaSentence) {
			
			this.setChanged();
			this.notifyObservers(arg);
		}
	}

	public List<String> encode(String talker, INmeaSentence sentence) {
		
		try {
			if (talker == null || talker.length() != 2 || sentence == null)
				return new ArrayList<String>();
			
			// if a multisentence
			if (MultiSentenceTable.instance().isMultiSentenceType(sentence.sentenceType())) {
				
				CodecManager.logger.error("encoding multi-sentence sentence, type: " + sentence.sentenceType());

				AbstractNmeaSentenceCodec codec = 
						this.multiSentenceCodecFactory.getBySymbol(StringHelper.capitalizeFirstChar(sentence.sentenceType()));
				return codec.encode(talker, sentence);
			} else if (sentence instanceof ParametricSentence) {
				
				CodecManager.logger.error("encoding parametric sentence, type: " + sentence.sentenceType());

				// if a parametric sentence
				return this.parametricSentenceCodec.encode(talker, sentence);
			} else if (sentence instanceof EncapsulationSentence) {
				
				CodecManager.logger.error("encoding encapsulation sentence, type: " + sentence.sentenceType());

				// if an encapsulation sentence
				AbstractNmeaSentenceCodec codec = 
						this.encapsulationSentenceCodecFactory.getBySymbol(StringHelper.capitalizeFirstChar(sentence.sentenceType()));
				return codec.encode(talker, sentence);
			} else {
				// must be a proprietary sentence
				CodecManager.logger.error("encoding query sentence, type: " + sentence.sentenceType());
				
				AbstractNmeaSentenceCodec codec = 
						this.proprietarySentenceCodecFactory.getBySymbol(StringHelper.capitalizeFirstChar(sentence.sentenceType()));
				return codec.encode(talker, sentence);
			}
		} catch (SentenceFieldCodecNotFoundException |
				 MessageFieldCodecNotFoundException |
				 InstantiationException |
				 IllegalAccessException e) {
			CodecManager.logger.error("exception occurred during encoding.", e);
			return new ArrayList<String>();
		}
	}
}
