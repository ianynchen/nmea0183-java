package com.antu.nmea.sentence.field.codec;

import java.lang.reflect.Field;

import com.antu.nmea.annotation.FieldSetting;

public class CharSentenceFieldCodec extends AbstractSentenceFieldCodec {

	@Override
	public String fieldCodecType() {
		return "char";
	}

	@Override
	public int requiredSegments() {
		return 1;
	}

	@Override
	protected boolean doDecode(String[] segments, Object obj, Field field,
			FieldSetting setting, int startIndex) {

		if (segments[startIndex].length() > 1)
			return false;
		
		try {
			if (segments[startIndex].isEmpty()) {
				if (!setting.isRequired()) {
					field.set(obj, setting.getDefaultValue().charAt(0));
					return true;
				} else {
					return false;
				}
			} else {
				field.set(obj, segments[startIndex].charAt(0));
			}
			return true;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			return false;
		}
	}

	@Override
	protected boolean doEncode(StringBuilder builder, Object obj, Field field,
			FieldSetting setting) {

		try {
			Object value = field.get(obj);
			if (value != null && value instanceof Character) {
				builder.append(',').append(value);
				return true;
			} else if (value == null) {
				
				if (setting.isRequired()) {
					return false;
				} else {
					builder.append(",");
					return true;
				}
			}
			return false;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			
			return false;
		}
	}

}
