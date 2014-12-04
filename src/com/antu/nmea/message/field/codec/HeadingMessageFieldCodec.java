package com.antu.nmea.message.field.codec;

import java.lang.reflect.Field;
import java.util.List;

import com.antu.nmea.annotation.MessageField;
import com.antu.nmea.sentence.ais.AbstractAisSentence;

public class HeadingMessageFieldCodec implements IMessageFieldCodec {

	public HeadingMessageFieldCodec() {
	}

	@Override
	public String fieldCodecType() {
		return "heading";
	}

	@Override
	public boolean decode(List<Byte> bits, int startIndex, Object obj,
			Field field) {
		
		MessageField annotation = field.getAnnotation(MessageField.class);
		Short b = MessageFieldCodecHelper.parseShort(bits, startIndex, annotation.requiredBits(), false);
		
		if (b == null)
			return false;

		try {
			if (b < 0 || b > 359)
				field.set(obj, AbstractAisSentence.HEADING_NOT_AVAILABLE);
			else
				field.set(obj, b);
			return true;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			return false;
		}
	}

	@Override
	public boolean encode(List<Byte> bits, Object obj, Field field,
			MessageField annotation) {
		
		try {
			Short val = field.getShort(obj);
			
			short b = 511;
			if (val != null && val >= 0 && val < 360) {
				b = val;
			}
			if (!MessageFieldCodecHelper.shortToBits(bits, b, annotation.requiredBits(), false)) {
				return false;
			}
			return true;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			return false;
		}
	}

}
