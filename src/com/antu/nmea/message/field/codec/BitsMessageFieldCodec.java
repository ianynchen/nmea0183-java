package com.antu.nmea.message.field.codec;

import java.lang.reflect.Field;
import java.util.List;

import com.antu.nmea.annotation.FieldSetting;
import com.antu.util.PrintableList;

public class BitsMessageFieldCodec extends AbstractMessageFieldCodec {

	public BitsMessageFieldCodec() {
	}

	@Override
	public String fieldCodecType() {
		return "bits";
	}

	@Override
	protected boolean doDecode(List<Byte> bits, int startIndex, Object obj,
			Field field, FieldSetting setting) {
		
		PrintableList<Byte> result = new PrintableList<Byte>();
		for (int i = 0; i < setting.getFieldWidth(); i++) {
			result.add(bits.get(i + startIndex));
		}
		
		try {
			field.set(obj, result);
			return true;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected boolean doEncode(List<Byte> bits, Object obj, Field field,
			FieldSetting setting) {
		
		try {
			Object val = field.get(obj);
			
			if (val instanceof List) {
				for (Byte b : ((List<Byte>)val)) {
					bits.add(b);
				}
			}
			return true;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			return false;
		}
	}

}
