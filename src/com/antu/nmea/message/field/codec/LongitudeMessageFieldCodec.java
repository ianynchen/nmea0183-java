package com.antu.nmea.message.field.codec;

import java.lang.reflect.Field;
import java.util.List;

import com.antu.nmea.annotation.FieldSetting;
import com.antu.nmea.sentence.ais.AbstractAisSentence;

public class LongitudeMessageFieldCodec extends AbstractMessageFieldCodec {

	public LongitudeMessageFieldCodec() {
	}

	@Override
	public String fieldCodecType() {
		return "longitude";
	}

	@Override
	protected Integer doDecode(List<Byte> bits, int startIndex, Object obj,
			Field field, FieldSetting setting) {
		
		assert(setting.getFieldWidth() == 28);
		
		if (bits.size() - startIndex < setting.getFieldWidth())
			return null;

		Integer val = MessageFieldCodecHelper.parseInteger(bits, startIndex, setting.getFieldWidth(), true);
		
		if (val == null)
			return null;

		try {
			if (val == 0x6791AC0)
				field.set(obj, AbstractAisSentence.LONGITUDE_NOT_AVAILABLE);
			else
				field.set(obj, val / 600000.0);
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
			
			int b = 0x6791AC0;
			if (val != null && val >= -90 && val <= 90) {
				b = (int) (val * 60 * 10000);
			}
			if (!MessageFieldCodecHelper.integerToBits(bits, b, setting.getFieldWidth(), true)) {
				return false;
			}
			return true;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			return false;
		}
	}

}
