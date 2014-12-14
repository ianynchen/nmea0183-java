package com.antu.nmea.message.field.codec;

import java.lang.reflect.Field;
import java.util.List;

import com.antu.nmea.annotation.FieldSetting;

public class ShortMessageFieldCodec extends AbstractMessageFieldCodec {

	public ShortMessageFieldCodec() {
	}

	@Override
	public String fieldCodecType() {
		return "short";
	}

	@Override
	protected Integer doDecode(List<Byte> bits, int startIndex, Object obj,
			Field field, FieldSetting setting) {

		if (bits.size() < startIndex + setting.getFieldWidth())
			return null;
		
		Short value = MessageFieldCodecHelper.parseShort(bits, startIndex, setting.getFieldWidth(), false);
		
		if (value != null) {
			try {
				field.setShort(obj, value);
				return setting.getFieldWidth();
			} catch (IllegalArgumentException | IllegalAccessException e) {
				return null;
			}
		}
		
		return null;
	}

	@Override
	protected boolean doEncode(List<Byte> bits, Object obj, Field field,
			FieldSetting setting) {
		
		try {
			Short value = field.getShort(obj);
			
			if (value != null) {
				if (!MessageFieldCodecHelper.shortToBits(bits, value, setting.getFieldWidth(), false)) {
					return false;
				}
			}
			return true;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			return false;
		}
	}
}
