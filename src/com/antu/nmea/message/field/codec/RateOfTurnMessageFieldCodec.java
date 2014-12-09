package com.antu.nmea.message.field.codec;

import java.lang.reflect.Field;
import java.util.List;

import com.antu.nmea.annotation.FieldSetting;
import com.antu.nmea.sentence.ais.AbstractAisSentence;

public class RateOfTurnMessageFieldCodec extends AbstractMessageFieldCodec {

	public RateOfTurnMessageFieldCodec() {
	}

	@Override
	public String fieldCodecType() {
		return "rateOfTurn";
	}

	@Override
	protected Integer doDecode(List<Byte> bits, int startIndex, Object obj,
			Field field, FieldSetting setting) {
		
		assert(setting.getFieldWidth() == 8);
		
		if (bits.size() - startIndex < setting.getFieldWidth())
			return null;

		Byte b = MessageFieldCodecHelper.parseByte(bits, startIndex, setting.getFieldWidth(), true);
		
		if (b == null)
			return null;

		try {
			if (b == -128)
				field.set(obj, AbstractAisSentence.RATE_OF_TURN_NOT_AVAILABLE);
			else if (b == -127 || b == 127)
				field.set(obj, AbstractAisSentence.RATE_OF_TURN_EXCEED_LIMIT);
			else
				field.set(obj, (b / 4.733) * (b / 4.733));
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
			
			byte b = -128;
			if (val != null) {
				if (val == AbstractAisSentence.RATE_OF_TURN_EXCEED_LIMIT)
					b = (byte) ((val > 0) ? 127 : -127);
				else {
					boolean negative = val < 0;
					if (val < 0)
						val = -val;
					b = (byte) (Math.sqrt(val) * 4.733);
					
					if (negative) b = (byte) -b;
				}
			}
			if (!MessageFieldCodecHelper.byteToBits(bits, b, setting.getFieldWidth(), true)) {
				return false;
			}
			return true;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			return false;
		}
	}

}
