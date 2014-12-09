package com.antu.nmea.message.field.codec;

import java.lang.reflect.Field;
import java.util.List;

import com.antu.nmea.annotation.FieldSetting;
import com.antu.nmea.sentence.ais.AbstractAisSentence;

public class SpeedMessageFieldCodec extends AbstractMessageFieldCodec {

	public SpeedMessageFieldCodec() {
	}

	@Override
	public String fieldCodecType() {
		return "speed";
	}

	@Override
	protected Integer doDecode(List<Byte> bits, int startIndex, Object obj,
			Field field, FieldSetting setting) {
		
		assert(setting.getFieldWidth() == 10);
		if (bits.size() - startIndex < setting.getFieldWidth())
			return null;

		Short b = MessageFieldCodecHelper.parseShort(bits, startIndex, setting.getFieldWidth(), false);
		
		if (b == null || b < 0)
			return null;

		try {
			if (b == 1023)
				field.set(obj, AbstractAisSentence.SPEED_NOT_AVAILABLE);
			else if (b == 1022)
				field.set(obj, AbstractAisSentence.SPEED_EXCEED_LIMIT);
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
			
			short b = 1023;
			if (val != null && val >= 0) {
				if (val == AbstractAisSentence.SPEED_EXCEED_LIMIT)
					b = 1022;
				else {
					b = (short) (val * 10);
				}
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
