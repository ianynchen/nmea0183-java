package com.antu.nmea.message.field.codec;

import java.lang.reflect.Field;
import java.util.List;

import com.antu.nmea.annotation.FieldSetting;
import com.antu.nmea.sentence.ais.AbstractAisSentence;

public class HeadingMessageFieldCodec extends AbstractMessageFieldCodec {

	public HeadingMessageFieldCodec() {
	}

	@Override
	public String fieldCodecType() {
		return "heading";
	}

	@Override
	protected Integer doDecode(List<Byte> bits, int startIndex, Object obj,
			Field field, FieldSetting setting) {
		
		assert(setting.getFieldWidth() == 9);
		
		if (bits.size() - startIndex < setting.getFieldWidth())
			return null;

		Short b = MessageFieldCodecHelper.parseShort(bits, startIndex, setting.getFieldWidth(), false);
		
		if (b == null)
			return null;

		try {
			if (b < 0 || b > 359)
				field.set(obj, AbstractAisSentence.HEADING_NOT_AVAILABLE);
			else
				field.set(obj, b);
			return setting.getFieldWidth();
		} catch (IllegalArgumentException | IllegalAccessException e) {
			return null;
		}
	}

	@Override
	protected boolean doEncode(List<Byte> bits, Object obj, Field field,
			FieldSetting setting) {
		
		try {
			Short val = field.getShort(obj);
			
			short b = 511;
			if (val != null && val >= 0 && val < 360) {
				b = val;
			}
			if (!MessageFieldCodecHelper.shortToBits(bits, b, setting.getFieldWidth(), false)) {
				return false;
			}
			return true;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			return false;
		}
	}

}
