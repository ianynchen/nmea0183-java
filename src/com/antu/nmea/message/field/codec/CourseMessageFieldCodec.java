package com.antu.nmea.message.field.codec;

import java.lang.reflect.Field;
import java.util.List;

import com.antu.nmea.annotation.FieldSetting;
import com.antu.nmea.sentence.ais.AbstractAisSentence;

public class CourseMessageFieldCodec extends AbstractMessageFieldCodec {

	public CourseMessageFieldCodec() {
	}

	@Override
	public String fieldCodecType() {
		return "course";
	}

	@Override
	protected Integer doDecode(List<Byte> bits, int startIndex, Object obj,
			Field field, FieldSetting setting) {
		
		assert(setting.getFieldWidth() == 12);
		
		if (bits.size() - startIndex < setting.getFieldWidth())
			return null;
		
		Short b = MessageFieldCodecHelper.parseShort(bits, startIndex, setting.getFieldWidth(), false);
		
		if (b == null || b < 0 || b > 3600)
			return null;

		try {
			if (b == 3600)
				field.set(obj, AbstractAisSentence.COURSE_NOT_AVAILABLE);
			else
				field.set(obj, b / 10.0);
			return setting.getFieldWidth();
		} catch (IllegalArgumentException | IllegalAccessException e) {
			return null;
		}
	}

	@Override
	protected boolean doEncode(List<Byte> bits, Object obj, Field field,
			FieldSetting setting) {
		
		try {
			Double val = field.getDouble(obj);
			
			short b = 3600;
			if (val != null && val >= 0 && val < 360) {
				b = (short) (val * 10);
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
