package com.antu.nmea.codec;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.antu.nmea.annotation.MessageField;
import com.antu.nmea.annotation.SentenceField;
import com.antu.nmea.annotation.SentenceMessage;
import com.antu.nmea.codec.exception.MessageFieldCodecNotFoundException;
import com.antu.nmea.codec.exception.SentenceFieldCodecNotFoundException;
import com.antu.nmea.message.field.codec.IMessageFieldCodec;
import com.antu.nmea.sentence.EncapsulationSentence;
import com.antu.nmea.sentence.IEncapsulatedSentence;
import com.antu.nmea.sentence.INmeaSentence;
import com.antu.nmea.sentence.field.codec.ISentenceFieldCodec;
import com.antu.nmea.util.Pair;
import com.antu.nmea.util.SentenceStore;
import com.antu.nmea.util.StringHelper;
import com.antu.util.GenericFactory;
import com.antu.util.PrintableList;

abstract public class EncapsulationSentenceCodec extends AbstractNmeaSentenceCodec {
	
	static public final long CHECK_INTERVAL = 500;
	static public final long INIT_DELAY = 200;
		
	private Timer checkTimer;
	
	private int sequenceId;

	static private Log logger = LogFactory.getLog(EncapsulationSentenceCodec.class);

	private SentenceStore<Pair<Integer, Integer>, EncapsulationSentence> storedSentences =
			new SentenceStore<Pair<Integer, Integer>, EncapsulationSentence>();
	
	private GenericFactory<IMessageFieldCodec> messageFieldCodecFactory;

	public EncapsulationSentenceCodec() {
		this.checkTimer = new Timer(true);
		this.checkTimer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				try {
					check();
				} catch (MessageFieldCodecNotFoundException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
				}
			}
			
		}, INIT_DELAY, CHECK_INTERVAL);
		messageFieldCodecFactory = new GenericFactory<IMessageFieldCodec>("com.antu.nmea.message.field.codec", "?MessageFieldCodec");
		new GenericFactory<ISentenceFieldCodec>("com.antu.nmea.sentence.field.codec", "?SentenceFieldCodec");
	}

	/**
	 * Periodically checks to see if there are incomplete sentences and if
	 * so and if they stayed in the repository for too long, try to decode
	 * them even if they are not complete.
	 * 
	 * @throws MessageFieldCodecNotFoundException
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	protected void check() throws MessageFieldCodecNotFoundException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		
		Date now = Calendar.getInstance().getTime();
		
		List<EncapsulationSentence> expiredSentences = this.storedSentences.getExpiredItems(now, 
				(int) EncapsulationSentenceCodec.CHECK_INTERVAL);
		
		for (EncapsulationSentence sentence : expiredSentences) {
			sentence.decodeStringData(sentence.getEncapsulatedData());
			this.decodeMessageFields(sentence);
		}
	}
	
	/**
	 * Each encapsulation sentence will have bits parsed at this stage, and hence knows how to create
	 * encapsulated sentences from those bits.
	 * @param sentence
	 * @return
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	abstract protected IEncapsulatedSentence createEncapsulatedSentence(EncapsulationSentence sentence) 
			throws ClassNotFoundException, InstantiationException, IllegalAccessException;
	
	/**
	 * group item should be unique, although not necessarily the last field in a message
	 * @param embedded encapsulated sentence that contains the group item
	 * @return number of bits after the group item.
	 * @throws InstantiationException thrown if there are multiple group items in a message.
	 */
	protected int isGroupItemCorrect(IEncapsulatedSentence embedded)
					throws InstantiationException {
		
		List<Field> fields = AbstractNmeaSentenceCodec.getMessageFields(embedded);
		
		int groupIndex = fields.size();
		int bits = 0;
		for (int i = 0; i < fields.size(); i++) {
			
			MessageField annotation = fields.get(i).getAnnotation(MessageField.class);
			if (annotation.isGroup()) {
				
				if (groupIndex == fields.size()) {
					groupIndex = i;
				} else {
					throw new InstantiationException("multiple group items: " + embedded.getClass().getName());
				}
			} else if (groupIndex < fields.size()) {
				bits += annotation.requiredBits();
			}
		}
		return bits;
	}
	
	/**
	 * Decodes encapsulated sentence objects from parsed bits.
	 * 
	 * @param sentence
	 * @throws MessageFieldCodecNotFoundException
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	protected void decodeMessageFields(EncapsulationSentence sentence) 
			throws MessageFieldCodecNotFoundException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		
		IEncapsulatedSentence encapsulatedSentence = this.createEncapsulatedSentence(sentence);
		
		try {
			this.isGroupItemCorrect(encapsulatedSentence);
		} catch (InstantiationException e) {
			EncapsulationSentenceCodec.logger.error("multiple group items for: " + encapsulatedSentence.getClass().getName());
			throw e;
		}
		
		sentence.setEncapsulatedSentence(encapsulatedSentence);
		
		EncapsulationSentenceCodec.logger.info("encapsulated sentence set: " + encapsulatedSentence.getClass().getName());
		
		List<Field> fields = AbstractNmeaSentenceCodec.getMessageFields(encapsulatedSentence);
		List<Byte> bits = sentence.getRawData();
		
		int bitIndex = 0;
		for (Field field : fields) {
			MessageField annotation = field.getAnnotation(MessageField.class);
			Integer size = null;
			
			EncapsulationSentenceCodec.logger.info("trying to find message field codec from symbol: " + annotation.fieldType());
			try {
				IMessageFieldCodec codec = //MessageFieldCodecManager.instance().getCodec(annotation.fieldType());
						this.messageFieldCodecFactory.getBySymbol(StringHelper.capitalizeFirstChar(annotation.fieldType()));
	
				EncapsulationSentenceCodec.logger.info("trying to decode field: " + field.getName());
				if (codec == null) {
					EncapsulationSentenceCodec.logger.error("message field codec not found: " + annotation.fieldType());
					return;
				} else {
					size = codec.decode(bits, bitIndex, encapsulatedSentence, field);
					
					if (size == null) {
						EncapsulationSentenceCodec.logger.error("unable to decode field: " + field.getName());
						return;
					} else {
						EncapsulationSentenceCodec.logger.info("field: " + field.getName() + "; value: " + field.get(encapsulatedSentence).toString());
					}
				}
			} catch (Exception e) {
				EncapsulationSentenceCodec.logger.error("error finding codec", e);
				return;
			}
			bitIndex += size;
			
			if (bitIndex >= bits.size())
				break;
		}
		
		this.setChanged();
		this.notifyObservers(sentence);
		EncapsulationSentenceCodec.logger.info("message fields decoded, sentence: " + sentence.toString());
	}

	@Override
	protected boolean postDecodeProcess(INmeaSentence sentence) {

		if (sentence instanceof EncapsulationSentence)  {
			try {
				this.storeSentence((EncapsulationSentence)sentence);
				EncapsulationSentenceCodec.logger.info("post-decode complete");
				return true;
			} catch (MessageFieldCodecNotFoundException |
					 ClassNotFoundException | 
					 InstantiationException | 
					 IllegalAccessException e) {
				EncapsulationSentenceCodec.logger.error("exception during post-decode process", e);
				return false;
			}
		}
		EncapsulationSentenceCodec.logger.error("sentence type mismatch, EncapsulationSentence expected, actual type: " + sentence.getClass().getName());
		return false;
	}
	
	abstract protected int getMessageId(EncapsulationSentence sentence);
	
	protected void storeSentence(EncapsulationSentence sentence) 
			throws MessageFieldCodecNotFoundException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		
		if (sentence.getTotalNumberOfSentences() == 1) {
			sentence.decodeStringData(sentence.getEncapsulatedData());
			this.decodeMessageFields(sentence);
		} else {
			Pair<Integer, Integer> key = new Pair<Integer, Integer>(sentence.getSequentialMessageId(),
					this.getMessageId(sentence));
			EncapsulationSentence sent = this.storedSentences.addItem(key, sentence);
			
			EncapsulationSentenceCodec.logger.info("sentence obtained: " + sent.getClass().getName());
			if (sent != null && sent.decodeStringData(sent.getEncapsulatedData())) {
				this.decodeMessageFields(sent);
			}
		}
	}

	@Override
	public List<String> doEncode(String talker, INmeaSentence sentence) 
			throws SentenceFieldCodecNotFoundException {

		List<String> result = new ArrayList<String>();
		List<Field> fields = AbstractNmeaSentenceCodec.getSentenceFields(sentence);
		
		if (talker.length() != 2)
			return result;
		
		int length = 7;
		for (Field field : fields) {
			
			SentenceField annotation = field.getAnnotation(SentenceField.class);
			
			if (annotation.isIgnoredInReconstruction())
				continue;
			
			length += annotation.fieldWidth();
		}
		
		// calculate how many sentences needed
		int maxChars = 80 - 8 - length;
		
		int bodyLength = ((EncapsulationSentence) sentence).getEncapsulatedData().length();
		int sentences = bodyLength / maxChars;
		int remainder = bodyLength % maxChars;
		
		if (remainder > 0)
			sentences++;
		int totalSentences = sentences;

		List<StringBuilder> builders = new ArrayList<StringBuilder>();
		
		// tries to construct sentence, anything that is ignored, everything
		// after that has to be ignored as well.
		for (int i = 1; i <= totalSentences; i++) {
			
			StringBuilder sb = new StringBuilder("!");
			sb.append(talker).append(sentence.sentenceType().toUpperCase());
			
			((EncapsulationSentence) sentence).setTotalSentenceCount(totalSentences);
			((EncapsulationSentence) sentence).setSentenceNumber(i);
			if (totalSentences > 1)
				((EncapsulationSentence) sentence).setSequenceId(this.sequenceId);
			else
				((EncapsulationSentence) sentence).setSequenceId(null);
			for (Field field : fields) {
					
				SentenceField annotation = field.getAnnotation(SentenceField.class);
				
				if (annotation.isIgnoredInReconstruction())
					continue;
				
				length += annotation.fieldWidth();
				ISentenceFieldCodec fieldCodec = this.getCodec(annotation, field.getName());
				
				if (fieldCodec == null || !fieldCodec.encode(sb, sentence, field)) {
					return new PrintableList<String>();
				}
			}
			
			builders.add(sb);
		}
		
		if (totalSentences > 1) {
			this.sequenceId++;
			if (this.sequenceId > 9)
				this.sequenceId -= 9;
		}
			
		List<StringBuilder> sentenceBuilders = this.breakSentences(builders, 
				(EncapsulationSentence) sentence, ((EncapsulationSentence) sentence).getTotalNumberOfSentences(),
				length);
			
		for (int i = 0; i < sentenceBuilders.size(); i++) {
			
			StringBuilder sb = sentenceBuilders.get(i);
			
			sb.append(',');
			if (i != sentenceBuilders.size() - 1) {
				sb.append('0');
			} else {
				sb.append(((EncapsulationSentence) sentence).getFillBits());
			}
			
			this.appendCheckSum(sb);
			sb.append("\r\n");
			result.add(sb.toString());
		}
		return result;
	}
	

	/**
	 * get encapsulated sentence and convert the message fields to bits
	 */
	@Override
	protected boolean preEncodeProcess(INmeaSentence sentence) {
		
		assert(sentence != null);
		assert(sentence instanceof EncapsulationSentence);
		
		IEncapsulatedSentence encap = ((EncapsulationSentence)sentence).getEncapsulatedSentence();
		
		assert(encap != null);
		
		List<Field> messageFields = AbstractNmeaSentenceCodec.getMessageFields(encap);
		List<Byte> bits = new ArrayList<Byte>();
		
		boolean success = true;
		for (Field field : messageFields) {
			MessageField annotation = field.getAnnotation(MessageField.class);
			
			IMessageFieldCodec codec = null;
			try {
				codec = this.messageFieldCodecFactory.getBySymbol(StringHelper.capitalizeFirstChar(annotation.fieldType()));
			} catch (InstantiationException | IllegalAccessException e) {
				EncapsulationSentenceCodec.logger.error("cannot create message codec", e);
				return false;
			}
			
			if (codec != null) {
				if (!codec.encode(bits, encap, field)) {
					success = false;
					break;
				}
			}
		}
		
		if (success) {
			
			int remainder = bits.size() % 6;
			int fillBits = (remainder == 0) ? 0 : 6 - remainder;
			
			// if a sentence requires all byte to be aligned, set fill bits to 0
			// and padd the bits.
			if (encap.getClass().isAnnotationPresent(SentenceMessage.class)) {
				SentenceMessage annot = encap.getClass().getAnnotation(SentenceMessage.class);
				
				if (annot != null && annot.alignBytes()) {
					for (int i = 0; i < fillBits; i++) {
						bits.add((byte) 0);
					}
					fillBits = 0;
				}
			}
			
			for (int i = 0; i < fillBits; i++) {
				bits.add((byte) 0);
			}
			
			Byte[] bitArray = new Byte[bits.size()];
			bitArray = bits.toArray(bitArray);
			
			String encoded = com.antu.nmea.sentence.EncapsulationSentence.convertSixBitsToString(bitArray);

			((EncapsulationSentence)sentence).setEncapsulatedData(encoded);
			((EncapsulationSentence)sentence).setFillBits(fillBits);
		}
		return success;
	}


	protected List<StringBuilder> breakSentences(List<StringBuilder> builders, EncapsulationSentence sentence,
			int totalNumberOfSentences, int length) {
		
		String content = sentence.getEncapsulatedData();
		int maxLength = 80 - length - 8;
		for (int i = 0; i < totalNumberOfSentences; i++) {
			StringBuilder sb = builders.get(i);
			sb.append(',');
			
			if (i == totalNumberOfSentences - 1) {
				sb.append(content.substring(i * totalNumberOfSentences));
			} else {
				sb.append(content.substring(i * totalNumberOfSentences, i * totalNumberOfSentences + maxLength));				
			}
		}
		
		return builders;
	}
}
