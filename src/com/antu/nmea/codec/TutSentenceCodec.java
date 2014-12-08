package com.antu.nmea.codec;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.antu.nmea.annotation.SentenceField;
import com.antu.nmea.codec.exception.SentenceFieldCodecNotFoundException;
import com.antu.nmea.sentence.IGroupedSentence;
import com.antu.nmea.sentence.INmeaSentence;
import com.antu.nmea.sentence.NmeaSentence;
import com.antu.nmea.sentence.TutSentence;
import com.antu.nmea.sentence.field.codec.ISentenceFieldCodec;
import com.antu.nmea.util.StringHelper;
import com.antu.nmea.util.TranslationCodeTable;
import com.antu.util.PrintableList;

public class TutSentenceCodec extends AbstractNmeaSentenceCodec {
	
	static public final long CHECK_INTERVAL = 500;
	static public final long INIT_DELAY = 200;
	
	public static class TutSegment extends NmeaSentence implements IGroupedSentence {

		public TutSegment() {
			super();
		}
		
		public TutSegment(Date date) {
			super(date);
		}
		
		public TutSegment(long currentTimeSinceEpochInSeconds) {
			super(currentTimeSinceEpochInSeconds);
		}

		@Override
		public String sentenceType() {
			return "tut";
		}
		
		@SentenceField(order = 1, fieldType="string")
		public String sourceIdentifier = "";

		@SentenceField(order = 2, fieldType="integer")
		public int totalNumberOfSentences = 0;

		@SentenceField(order = 3, fieldType="integer")
		public int sentenceNumber = 0;
		
		@SentenceField(order = 4, fieldType="integer")
		public int sequentialMessageIdentifier = 0;
		
		@SentenceField(order = 5, fieldType="string")
		public String translationCode = "";
		
		@SentenceField(order = 6, fieldType="string")
		public String textBody = "";

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

		@Override
		public boolean isComplete() {
			return this.totalNumberOfSentences == this.sentenceNumber;
		}

		@Override
		public void concatenate(IGroupedSentence sentence) {

			if (sentence instanceof TutSegment) {
				TutSegment newSeg = (TutSegment)sentence;
				
				this.textBody += newSeg.textBody;
				this.sentenceNumber = newSeg.sentenceNumber;
			}
		}
	}
	
	private HashMap<String, HashMap<Integer, TutSentenceCodec.TutSegment>> sentences;
	
	private short nextSentenceId = 0;
	
	private Timer checkTimer;

	public TutSentenceCodec() {
		
		this.sentences = new HashMap<String, HashMap<Integer, TutSentenceCodec.TutSegment>>();
		this.checkTimer = new Timer(true);
		this.checkTimer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				check();
			}
			
		}, INIT_DELAY, CHECK_INTERVAL);
	}
	
	synchronized protected void check() {
		
		List<TutSegment> incompleteSegments = new ArrayList<TutSegment>();
		Date now = Calendar.getInstance().getTime();
		
		for (String talker : this.sentences.keySet()) {
			HashMap<Integer, TutSegment> segments = this.sentences.get(talker);
			
			for (int sentenceId : segments.keySet()) {
				TutSegment segment = segments.get(sentenceId);
				
				if (now.getTime() - segment.getReceiveDate().getTime() >= CHECK_INTERVAL) {
					incompleteSegments.add(segment);
				}
			}
		}
		
		for (TutSegment segment : incompleteSegments) {
			this.decodeSegment(segment);
			
			this.sentences.get(segment.sourceIdentifier).remove(segment.sequentialMessageIdentifier);
		}
	}

	@Override
	public String getCodecType() {
		return "tut";
	}

	@Override
	protected INmeaSentence createSentence(Date timestamp, String sentenceType) {
		return new TutSentenceCodec.TutSegment(timestamp);
	}

	@Override
	protected boolean doDecode(String sentenceType, INmeaSentence sentence,
			String[] segments) throws SentenceFieldCodecNotFoundException {
		
		if (!(sentence instanceof TutSentenceCodec.TutSegment)) {
			return false;
		}

		List<Field> annotatedFields = AbstractNmeaSentenceCodec.getSentenceFields(sentence);
		
		int index = 1;
		for (Field field : annotatedFields) {
			SentenceField annotation = field.getAnnotation(SentenceField.class);
			
			ISentenceFieldCodec fieldCodec = this.getCodec(annotation, field.getName());
			
			if (fieldCodec != null) {
				boolean success = fieldCodec.decode(segments, sentence, field, index);
				index += fieldCodec.requiredSegments();
				
				if (!success){
					if (annotation.isRequired())
						return false;
					
					index += fieldCodec.requiredSegments();
					continue;
				}
			} else {
				return false;
			}
		}
		
		this.storeSentenceSegment((TutSentenceCodec.TutSegment)sentence);
		
		return true;
	}
	
	protected void decodeSegment(TutSentenceCodec.TutSegment segment) {
		
		TutSentence sentence = new TutSentence(segment.getReceiveDate());
		sentence.sourceIdentifier = segment.sourceIdentifier;
		sentence.translationCode = segment.translationCode;
		sentence.isComplete = segment.sentenceNumber == segment.totalNumberOfSentences;
		sentence.textBody = TranslationCodeTable.instance().decode(segment.translationCode, segment.textBody);
		sentence.isTranslated = sentence.textBody != null;
		
		this.setChanged();
		this.notifyObservers(sentence);
}
	
	synchronized protected void storeSentenceSegment(TutSentenceCodec.TutSegment segment) {
		
		if (segment.totalNumberOfSentences == 1) {
			this.decodeSegment(segment);
		} else if (this.sentences.containsKey(segment.sourceIdentifier)) {
			
			HashMap<Integer, TutSentenceCodec.TutSegment> segments = this.sentences.get(segment.sourceIdentifier);
			
			if (segments.containsKey(segment.sequentialMessageIdentifier)) {
				
				if (segment.sentenceNumber == segment.totalNumberOfSentences) {
					TutSentenceCodec.TutSegment storedSegment = segments.get(segment.sequentialMessageIdentifier);
					if (segment.sentenceNumber == storedSegment.sentenceNumber + 1) {
						storedSegment.textBody = storedSegment.textBody + segment.textBody;
						storedSegment.sentenceNumber = segment.sentenceNumber;
						segments.remove(segment.sequentialMessageIdentifier);
						
						this.decodeSegment(storedSegment);
					} else {
						segments.remove(segment.sourceIdentifier);
						this.decodeSegment(storedSegment);
					}
					
				} else {
					TutSentenceCodec.TutSegment storedSegment = segments.get(segment.sequentialMessageIdentifier);
					if (segment.sentenceNumber == storedSegment.sentenceNumber + 1) {
						storedSegment.textBody = storedSegment.textBody + segment.textBody;
						storedSegment.sentenceNumber = segment.sentenceNumber;
					} else {
						segments.remove(segment.sourceIdentifier);
						this.decodeSegment(storedSegment);
					}
				}
			} else {
				segments.put(segment.sequentialMessageIdentifier, segment);
			}
		} else {
			
			HashMap<Integer, TutSentenceCodec.TutSegment> segments = new HashMap<Integer, TutSentenceCodec.TutSegment>();
			segments.put(segment.sequentialMessageIdentifier, segment);
			this.sentences.put(segment.sourceIdentifier, segments);
		}
	}

	@Override
	public List<String> encode(String talker, INmeaSentence sentence) {
		
		List<String> result = new PrintableList<String>();
		
		if (talker == null || talker.length() != 2 || sentence == null)
			return result;

		if (sentence instanceof TutSentence) {
			boolean isProprietary = TranslationCodeTable.instance().isTranslationCodeProprietary(((TutSentence) sentence).translationCode);

			String encoded = TranslationCodeTable.instance().encode(((TutSentence) sentence).translationCode, 
					((TutSentence) sentence).textBody);
			int segmentLength = isProprietary ? 53 : 56;

			int number = encoded.length() / segmentLength;
			int remainder = encoded.length() % segmentLength;
			
			if (remainder > 0)
				number++;
			if (number > 255)
				return result;
			
			if (encoded != null) {
				
				for (int i = 0; i < number; i++) {
					
					StringBuilder sb = new StringBuilder("$");
					sb.append(talker).append(sentence.sentenceType().toUpperCase());
					sb.append(",").append(((TutSentence) sentence).sourceIdentifier);
					sb.append(",");
					sb.append(StringHelper.fromHex((char) ((number & 0x00F0) >> 4))).append(StringHelper.fromHex((char) (number & 0x000F)));
					sb.append(",");
					sb.append(StringHelper.fromHex((char) (((i + 1) & 0x00F0) >> 4))).append(StringHelper.fromHex((char) ((i + 1) & 0x000F)));
					sb.append(",");
					sb.append(new Integer(this.nextSentenceId).toString());
					sb.append(",").append(((TutSentence) sentence).translationCode);
					sb.append(",");
					
					if (i == number  - 1) {
						sb.append(encoded.substring(i * segmentLength));
					} else {
						sb.append(encoded.substring(i * segmentLength, (i + 1) * segmentLength));
					}
					this.appendCheckSum(sb);
					sb.append("\r\n");
					result.add(sb.toString());
				}
			}
		}
		this.nextSentenceId++;
		if (this.nextSentenceId > 9)
			this.nextSentenceId = 0;
		return result;
	}

	@Override
	protected boolean postDecodeProcess(INmeaSentence sentence) {

		this.setChanged();
		this.notifyObservers(sentence);
		return true;
	}

	@Override
	protected boolean preEncodeProcess(INmeaSentence sentence) {
		return true;
	}

	/**
	 * method is not used.
	 */
	@Override
	protected List<String> doEncode(String talker, INmeaSentence sentence)
			throws SentenceFieldCodecNotFoundException {
		return null;
	}

}
