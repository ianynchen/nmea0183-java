package com.antu.nmea.codec;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

import com.antu.nmea.annotation.SentenceField;
import com.antu.nmea.codec.exception.SentenceFieldCodecNotFoundException;
import com.antu.nmea.sentence.INmeaSentence;
import com.antu.nmea.sentence.ParametricSentenceFactory;
import com.antu.nmea.sentence.field.codec.ISentenceFieldCodec;
import com.antu.util.PrintableList;

public class ParametricSentenceCodec extends AbstractNmeaSentenceCodec {

	private ParametricSentenceFactory sentenceFactory;
	
	public ParametricSentenceCodec() {
		
		this.sentenceFactory = new ParametricSentenceFactory();
	}

	@Override
	public String getCodecType() {
		return "parametric";
	}

	@Override
	protected INmeaSentence createSentence(Date timestamp, String sentenceType) {
		return this.sentenceFactory.createSentence(timestamp, sentenceType);
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

	@Override
	protected List<String> doEncode(String talker, INmeaSentence sentence) 
			throws SentenceFieldCodecNotFoundException {

		PrintableList<String> result = new PrintableList<String>();
		
		List<Field> fields = AbstractNmeaSentenceCodec.getSentenceFields(sentence);
		
		StringBuilder builder = new StringBuilder("$");
		
		if (talker.length() != 2)
			return result;
		
		builder.append(talker).append(sentence.sentenceType().toUpperCase());
		
		for (Field field : fields) {
				
			SentenceField annotation = field.getAnnotation(SentenceField.class);
			ISentenceFieldCodec fieldCodec = null;
			
			if (annotation.isGroup())
				fieldCodec = this.getGroupCodec(annotation);
			else
				fieldCodec = this.getCodec(annotation, field.getName());

			if (fieldCodec == null) {
				throw new SentenceFieldCodecNotFoundException("field codec not found for: " + field.getName());
			}
			
			if (!fieldCodec.encode(builder, sentence, field)) {
				return result;
			}
		}
		
		this.appendCheckSum(builder);
		builder.append("\r\n");
		result.add(builder.toString());
		return result;
	}
}
