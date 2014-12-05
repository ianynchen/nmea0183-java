package com.antu.nmea.sentence.field.codec;

import java.lang.reflect.Field;

import com.antu.nmea.annotation.FieldSetting;

public class IntegerSentenceFieldCodec extends AbstractSentenceFieldCodec {

	@Override
	public String fieldCodecType() {
		return "integer";
	}

	@Override
	public int requiredSegments() {
		return 1;
	}

	@Override
	protected boolean doDecode(String[] segments, Object obj, Field field,
			FieldSetting setting, int startIndex) {

		try {
			if (segments[startIndex].isEmpty()) {
				if (setting.isRequired()) {
					return false;
				} else {
					field.set(obj, Integer.parseInt(setting.getDefaultValue()));
					return true;
				}
			}
			
			field.set(obj, Integer.parseInt(segments[startIndex]));
			return true;
		} catch (IllegalArgumentException
				| IllegalAccessException e) {
			
			return false;
		}
	}

	@Override
	protected boolean doEncode(StringBuilder builder, Object obj, Field field,
			FieldSetting setting) {

		Object value;
		try {
			value = field.get(obj);

			if (value == null) {
				if (setting.isRequired()) {
					return false;
				} else {
					builder.append(",");
					return true;
				}
			} else if (value instanceof Integer) {
				if (setting.getFieldWidth() == 0)
					builder.append(',').append(((Integer)value).toString());
				else {
					String format = String.format("%%0%dd", setting.getFieldWidth());
					builder.append(',').append(String.format(format, (Integer)value));
				}
				return true;
			} else {
				return false;
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			
			return false;
		}
	}

}
