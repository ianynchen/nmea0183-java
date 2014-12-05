package com.antu.nmea.codec;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Observable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.antu.nmea.annotation.GroupItemAnnotationSorter;
import com.antu.nmea.annotation.MessageFieldAnnotationSorter;
import com.antu.nmea.annotation.SentenceField;
import com.antu.nmea.annotation.SentenceFieldAnnotationSorter;
import com.antu.nmea.annotation.exception.MultipleGroupItemException;
import com.antu.nmea.codec.exception.MessageFieldCodecNotFoundException;
import com.antu.nmea.codec.exception.SentenceFieldCodecNotFoundException;
import com.antu.nmea.sentence.INmeaSentence;
import com.antu.nmea.sentence.field.codec.AbstractSentenceFieldCodec;
import com.antu.nmea.sentence.field.codec.IGroupSentenceFieldCodec;
import com.antu.nmea.sentence.field.codec.ISentenceFieldCodec;
import com.antu.nmea.util.StringHelper;
import com.antu.util.GenericFactory;
import com.antu.util.PrintableList;

/**
 * 
 * @author yining
 *
 * This is a base class for all sentence codecs. Unlike CodecManager, which is just a wrapper
 * that delegates to underlying codecs, this is the part where the actual encoding/decoding
 * takes place.
 * 
 * In
 */
public abstract class AbstractNmeaSentenceCodec extends Observable {
	
	protected GenericFactory<AbstractSentenceFieldCodec> sentenceFieldCodecFactory = 
			new GenericFactory<AbstractSentenceFieldCodec>("com.antu.nmea.sentence.field.codec", "?SentenceFieldCodec");

	static private Log logger = LogFactory.getLog(AbstractNmeaSentenceCodec.class);
	
	/**
	 * Codec type, this is no longer necessary
	 * @return
	 */
	abstract public String getCodecType();
	
	/**
	 * gets sentence field information for decoding segments
	 * @param sentence
	 * @return
	 */
	static public List<Field> getSentenceFields(INmeaSentence sentence) {
		
		List<Field> annotatedFields = new ArrayList<Field>();
		for (Field field : sentence.getClass().getFields()) {
			if (field.isAnnotationPresent(com.antu.nmea.annotation.SentenceField.class)) {
				annotatedFields.add(field);
			}
		}
		
		Collections.sort(annotatedFields, new SentenceFieldAnnotationSorter());
		return annotatedFields;
	}
	
	/**
	 * calculates checksum of a sentence
	 * @param sentence
	 * @return
	 */
	static public short checksum(String sentence) {
		short checksum = 0;
		
		int checksumIndex = sentence.lastIndexOf('*');
		
		if (checksumIndex > 0 && sentence.length() < checksumIndex + 3)
			return 0;
		
		if (checksumIndex < 0)
			checksumIndex = sentence.length();
		
		for (int i = 1; i < checksumIndex; i++) {
			checksum ^= (short)sentence.charAt(i);
		}

		return checksum;
	}
	
	/**
	 * calculates checksum of a sentence, and append it.
	 * @param builder
	 */
	protected void appendCheckSum(StringBuilder builder) {
		
		short checksum = AbstractNmeaSentenceCodec.checksum(builder.toString());
		
		char high = (char) ((checksum & 0x00F0) >> 4);
		char low = (char) (checksum & 0x000F);
		
		builder.append('*').append(StringHelper.fromHex(high)).append(StringHelper.fromHex(low));
	}

	static public List<Field> getGroupItems(Object obj) {
		
		List<Field> annotatedFields = new ArrayList<Field>();
		for (Field field : obj.getClass().getFields()) {
			if (field.isAnnotationPresent(com.antu.nmea.annotation.GroupItem.class)) {
				annotatedFields.add(field);
			}
		}
		
		Collections.sort(annotatedFields, new GroupItemAnnotationSorter());
		return annotatedFields;
	}

	/**
	 * gets message field information for encapsulated sentence
	 * @param sentence
	 * @return
	 */
	static public List<Field> getMessageFields(Object sentence) {
		
		List<Field> annotatedFields = new ArrayList<Field>();
		for (Field field : sentence.getClass().getFields()) {
			if (field.isAnnotationPresent(com.antu.nmea.annotation.MessageField.class)) {
				annotatedFields.add(field);
			}
		}
		
		Collections.sort(annotatedFields, new MessageFieldAnnotationSorter());
		return annotatedFields;
	}

	public INmeaSentence decode(long timestampFromEpoch, String sentenceType, String[] segments) 
			throws SentenceFieldCodecNotFoundException, 
			ClassNotFoundException, 
			InstantiationException, IllegalAccessException {
		
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timestampFromEpoch * 1000);

		return this.decode(cal.getTime(), sentenceType, segments);
	}
	
	public INmeaSentence decode(Date timestamp, String sentenceType, String[] segments) 
			throws SentenceFieldCodecNotFoundException, 
			ClassNotFoundException, 
			InstantiationException, IllegalAccessException {
		
		INmeaSentence sentence = this.createSentence(timestamp, sentenceType);
		assert(sentence != null);

		if (this.doDecode(sentenceType, sentence, segments))
			return sentence;
		else
			return null;
	}
	
	public INmeaSentence decode(String sentenceType, String[] segments) 
			throws SentenceFieldCodecNotFoundException, 
			ClassNotFoundException, 
			InstantiationException, IllegalAccessException {
		return this.decode(Calendar.getInstance().getTime(),  sentenceType, segments);
	}
	
	abstract protected INmeaSentence createSentence(Date timestamp, String sentenceType);
	
	protected AbstractSentenceFieldCodec getCodec(SentenceField annotation, String fieldName) {
		
		if (annotation.fullCodecPath() != null && !annotation.fullCodecPath().isEmpty()) {
			
			try {
				AbstractSentenceFieldCodec codec = this.sentenceFieldCodecFactory.getByFullName(annotation.fullCodecPath());
				AbstractNmeaSentenceCodec.logger.info("sentence field codec retrieved for field: " + fieldName);
				return codec;
			} catch (InstantiationException |
					 IllegalAccessException e) {
				AbstractNmeaSentenceCodec.logger.info("unable to retrieve sentence field codec for field: " + fieldName + " with class name: " + annotation.fullCodecPath());
			}
		}
		
		if (annotation.packageName() != null &&
			!annotation.packageName().isEmpty() &&
			this.sentenceFieldCodecFactory.isPackageRegistered(annotation.packageName())) {

			AbstractNmeaSentenceCodec.logger.info("registering package name: " + annotation.packageName());
			this.sentenceFieldCodecFactory.registerPackage(annotation.packageName());
		}
		
		if (annotation.fieldType() != null &&
				!annotation.fieldType().isEmpty()) {
			
			AbstractNmeaSentenceCodec.logger.info("retrieving sentence field codec from fieldType: " + annotation.fieldType());
			
			try {
				AbstractSentenceFieldCodec codec = this.sentenceFieldCodecFactory.getBySymbol(StringHelper.capitalizeFirstChar(annotation.fieldType()));
				AbstractNmeaSentenceCodec.logger.info("sentence field codec with fieldType: " + annotation.fieldType() + " retrieved.");
				return codec;
			} catch (InstantiationException |
					 IllegalAccessException e) {
				AbstractNmeaSentenceCodec.logger.error("failed retrieving sentence field codec with fieldType: " + annotation.fieldType());
				return null;
			}
		}
		
		return null;
	}
	
	protected IGroupSentenceFieldCodec getGroupCodec(SentenceField annotation) {
		
		assert(annotation.isGroup() == true);
		assert(annotation.groupType() != null && !annotation.groupType().isEmpty());
		assert(annotation.groupItemClass() != null && !annotation.groupItemClass().isEmpty());
		
		try {
			AbstractSentenceFieldCodec codec = this.sentenceFieldCodecFactory.getBySymbol(StringHelper.capitalizeFirstChar(annotation.groupType()));
			AbstractNmeaSentenceCodec.logger.info("retrieved group sentence field codec with groupType: " + annotation.groupType());
			
			if (codec instanceof IGroupSentenceFieldCodec) {
				return (IGroupSentenceFieldCodec)codec;
			} else {
				AbstractNmeaSentenceCodec.logger.error("group sentence field codec does not implement IGroupSentenceFieldCodec: " + codec.getClass().getName());
				return null;
			}
		} catch (InstantiationException |
				 IllegalAccessException e) {
			AbstractNmeaSentenceCodec.logger.error("failed retrieving group sentence field codec with groupType: " + annotation.groupType());
		}
		
		return null;
	}
	
	/**
	 * Checks if the fields have more than 1 SentenceField isGroup == true marked, and
	 * 
	 * 
	 * @param annotatedSentenceFields fields that are annotated with SentenceField annotation
	 * @throws MultipleGroupItemException throws exception if there are multiple fields annotated with isGroup == true, which
	 *         is not allowed.
	 * @return the number of sentence segments required after a isGroup == true item.
	 */
	protected int checkGroupItems(List<Field> annotatedSentenceFields) 
			throws SentenceFieldCodecNotFoundException {
		
		boolean foundGroupItem = false;
		int segmentsRequired = 0;
		
		for (Field field : annotatedSentenceFields) {
			
			SentenceField annotation = field.getAnnotation(SentenceField.class);
			
			if (annotation.isGroup()) {
				if (!foundGroupItem) {
					foundGroupItem = true;
					continue;
				} else {
					throw new MultipleGroupItemException(field.getName() + 
							": " +annotation.fieldType());
				}
			}
			
			if (foundGroupItem) {
				ISentenceFieldCodec codec = this.getCodec(annotation, field.getName());
				
				if (codec == null) {
					throw new SentenceFieldCodecNotFoundException("Sentence field codec not found for field: " + field.getName());
				} else {
					segmentsRequired += codec.requiredSegments();
				}
			}
		}
		
		return segmentsRequired;
	}

	protected boolean doDecode(String sentenceType, INmeaSentence sentence, String[] segments) 
			throws SentenceFieldCodecNotFoundException {
		
		List<Field> annotatedFields = AbstractNmeaSentenceCodec.getSentenceFields(sentence);
		
		this.checkGroupItems(annotatedFields);
		
		AbstractNmeaSentenceCodec.logger.info("decoding...");
		int index = 1;
		for (Field field : annotatedFields) {
			SentenceField annotation = field.getAnnotation(SentenceField.class);
			
			if (!annotation.isGroup()) {
				ISentenceFieldCodec codec = this.getCodec(annotation, field.getName());
				
				if (codec != null) {
					if (codec.decode(segments, sentence, field, index)) {
						index += codec.requiredSegments();
						AbstractNmeaSentenceCodec.logger.info("decoded: " + field.getName());
					} else {
						if (annotation.isRequired()) {
							AbstractNmeaSentenceCodec.logger.error("unable to decode a required field: " + field.getName());
							return false;
						} else {
							index += codec.requiredSegments();
							AbstractNmeaSentenceCodec.logger.info("unable to decode a non-required field: " + field.getName());
							continue;
						}
					}
				} else {
					AbstractNmeaSentenceCodec.logger.info("sentence field codec not found: " + field.getName());
					throw new SentenceFieldCodecNotFoundException("Sentence field codec not found for field: " + field.getName());
				}
			} else {
				assert(annotation.groupItemClass() != null &&
						!annotation.groupItemClass().isEmpty());
				
				IGroupSentenceFieldCodec codec = this.getGroupCodec(annotation);

				if (codec != null) {
					Object groupItem = codec.createGroupItem(annotation.groupItemClass());
					
					int segmentsPerItem = codec.segmentsPerItem(groupItem);
					int totalItems = codec.totalItems(annotation, index, segments.length, segmentsPerItem);

					if (totalItems % segmentsPerItem != 0) {
						AbstractNmeaSentenceCodec.logger.error("segments for group item does not match: " + field.getName());
						return false;
					}
							
					if (codec.decode(segments, sentence, field, index)) {
						index += totalItems * segmentsPerItem;
					} else {
						if (annotation.isRequired()) {
							return false;
						}
						else {
							index += totalItems * segmentsPerItem;
							continue;
						}
					}
				} else {
					throw new SentenceFieldCodecNotFoundException("Sentence field codec not found for field: " + field.getName());
				}
			}
		}
		
		AbstractNmeaSentenceCodec.logger.info("entering post-decode");
		if (this.postDecodeProcess(sentence))
			return true;
		return false;
	}
	
	public List<String> encode(String talker, INmeaSentence sentence) 
			throws SentenceFieldCodecNotFoundException, MessageFieldCodecNotFoundException {
		
		if (this.preEncodeProcess(sentence))
			return this.doEncode(talker, sentence);
		else
			return new PrintableList<String>();
	}
	
	abstract protected List<String> doEncode(String talker, INmeaSentence sentence) 
			throws SentenceFieldCodecNotFoundException;

	abstract protected boolean postDecodeProcess(INmeaSentence sentence);
	
	abstract protected boolean preEncodeProcess(INmeaSentence sentence);
}
