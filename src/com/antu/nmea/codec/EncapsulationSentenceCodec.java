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
import com.antu.nmea.codec.exception.MessageFieldCodecNotFoundException;
import com.antu.nmea.codec.exception.SentenceFieldCodecNotFoundException;
import com.antu.nmea.message.field.codec.IMessageFieldCodec;
import com.antu.nmea.message.field.codec.MessageFieldCodecManager;
import com.antu.nmea.sentence.EncapsulationSentence;
import com.antu.nmea.sentence.IEncapsulatedSentence;
import com.antu.nmea.sentence.INmeaSentence;
import com.antu.nmea.sentence.field.codec.ISentenceFieldCodec;
import com.antu.nmea.sentence.field.codec.ListSentenceFieldCodec;
import com.antu.nmea.sentence.field.codec.SentenceFieldCodecManager;
import com.antu.nmea.util.Pair;
import com.antu.nmea.util.SentenceStore;
import com.antu.nmea.util.StringHelper;
import com.antu.util.GenericFactory;

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
	
	abstract protected IEncapsulatedSentence createEncapsulatedSentence(EncapsulationSentence sentence) 
			throws ClassNotFoundException, InstantiationException, IllegalAccessException;
	
	protected void decodeMessageFields(EncapsulationSentence sentence) 
			throws MessageFieldCodecNotFoundException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		
		IEncapsulatedSentence encapsulatedSentence = this.createEncapsulatedSentence(sentence);
		sentence.setEncapsulatedSentence(encapsulatedSentence);
		//TODO what if its a group?
		
		EncapsulationSentenceCodec.logger.info("encapsulated sentence set: " + encapsulatedSentence.getClass().getName());
		
		List<Field> fields = AbstractNmeaSentenceCodec.getMessageFields(encapsulatedSentence);
		List<Byte> bits = sentence.getRawData();
		
		int index = 0;
		for (Field field : fields) {
			MessageField annotation = field.getAnnotation(MessageField.class);
			
			EncapsulationSentenceCodec.logger.info("trying to find message field codec from symbol: " + annotation.fieldType());
			try {
				IMessageFieldCodec codec = //MessageFieldCodecManager.instance().getCodec(annotation.fieldType());
						this.messageFieldCodecFactory.getBySymbol(StringHelper.capitalizeFirstChar(annotation.fieldType()));
	
				EncapsulationSentenceCodec.logger.info("trying to decode field: " + field.getName());
				if (codec == null) {
					EncapsulationSentenceCodec.logger.error("message field codec not found: " + annotation.fieldType());
				} else {
					codec.decode(bits, index, encapsulatedSentence, field);
					EncapsulationSentenceCodec.logger.info("field: " + field.getName() + "; value: " + field.get(encapsulatedSentence).toString());
				}
			} catch (Exception e) {
				EncapsulationSentenceCodec.logger.error("error finding codec", e);
			}
			index += annotation.requiredBits();
		}
		
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
		
		Pair<Integer, Integer> key = new Pair<Integer, Integer>(sentence.getSequentialMessageId(),
				this.getMessageId(sentence));
		EncapsulationSentence sent = this.storedSentences.addItem(key, sentence);
		
		EncapsulationSentenceCodec.logger.info("sentence obtained: " + sent.getClass().getName());
		if (sent != null && sent.decodeStringData(sent.getEncapsulatedData())) {
			this.decodeMessageFields(sentence);
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
			
			length += annotation.charLength();
		}
		
		// calculate how many sentences needed
		int maxChars = 80 - 8 - length;
		int totalSentences = (int) Math.ceil(((EncapsulationSentence) sentence).getEncapsulatedData().length() / maxChars);

		List<StringBuilder> builders = new ArrayList<StringBuilder>();
		
		// tries to construct sentence, anything that is ignored, everything
		// after that has to be ignored as well.
		for (int i = 0; i < totalSentences; i++) {
			
			StringBuilder sb = new StringBuilder("!");
			sb.append(talker).append(sentence.sentenceType().toUpperCase());
			
			((EncapsulationSentence) sentence).setTotalSentenceCount(totalSentences);
			((EncapsulationSentence) sentence).setSentenceNumber(i);
			((EncapsulationSentence) sentence).setSequenceId(this.sequenceId);

			for (Field field : fields) {
					
				SentenceField annotation = field.getAnnotation(SentenceField.class);
				
				if (annotation.isIgnoredInReconstruction())
					continue;
				
				length += annotation.charLength();
				ISentenceFieldCodec fieldCodec = SentenceFieldCodecManager.instance().getCodec(annotation.fieldType());
				if (!fieldCodec.encode(sb, sentence, null, field)) {
					return new ArrayList<String>();
				}
			}
			
			builders.add(sb);
		}
		this.sequenceId++;
			
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
	

	@Override
	protected boolean preEncodeProcess(INmeaSentence sentence) {
		
		List<Field> messageFields = AbstractNmeaSentenceCodec.getMessageFields(sentence);
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
				if (!codec.encode(bits, sentence, field, annotation)) {
					success = false;
					break;
				}
			}
		}
		
		if (success) {
			Byte[] sixBits = com.antu.nmea.sentence.EncapsulationSentence.convertBitsToSixBits(bits);
			String encoded = com.antu.nmea.sentence.EncapsulationSentence.convertSixBitsToString(sixBits);
			
			int remainder = sixBits.length % 6;
			int fillBits = (remainder == 0) ? 0 : 6 - remainder;
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
			builders.add(sb);
		}
		
		return builders;
	}
}
