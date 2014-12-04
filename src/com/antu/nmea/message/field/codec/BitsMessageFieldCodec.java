package com.antu.nmea.message.field.codec;

import java.lang.reflect.Field;
import java.util.List;

import com.antu.nmea.annotation.MessageField;
import com.antu.util.PrintableList;

public class BitsMessageFieldCodec implements IMessageFieldCodec {

	public BitsMessageFieldCodec() {
	}

	@Override
	public String fieldCodecType() {
		return "bits";
	}

	@Override
	public boolean decode(List<Byte> bits, int startIndex, Object obj,
			Field field) {
		
		MessageField annotation = field.getAnnotation(MessageField.class);

		PrintableList<Byte> result = new PrintableList<Byte>();
		for (int i = 0; i < annotation.requiredBits(); i++) {
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
	public boolean encode(List<Byte> bits, Object obj, Field field,
			MessageField annotation) {
		
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
