package com.antu.nmea.sentence.field.codec;

import java.lang.reflect.Field;

import com.antu.nmea.annotation.FieldSetting;

public class DoubleSentenceFieldCodec extends AbstractSentenceFieldCodec {

	@Override
	public String fieldCodecType() {
		return "double";
	}

	@Override
	public int requiredSegments() {
		return 1;
	}

	@Override
	protected boolean doDecode(String[] segments, Object obj, Field field,
			FieldSetting setting, int startIndex) {
		
		if (segments[startIndex].isEmpty()) {
			if (setting.isRequired()) {
				return false;
			}
			try {
				field.set(obj, Double.parseDouble(setting.getDefaultValue()));
			} catch (IllegalArgumentException
					| IllegalAccessException e) {
				return false;
			}
			return true;
		}
		
		Double value = FieldCodecHelper.parseDouble(segments[startIndex], 0, 0);
		if (value != null) {
			
			try {
				field.set(obj, value);
				return true;
			} catch (IllegalArgumentException | IllegalAccessException e) {
				return false;
			}
			
		}
		return false;
	}

	@Override
	protected boolean doEncode(StringBuilder builder, Object obj, Field field,
			FieldSetting setting) {
		try {
			Object value = field.get(obj);
			 
			if (value == null && setting.isRequired()) {
				return false;
			} else if (value == null) {
				builder.append(",");
				return true;
			} if (value instanceof Double) {
				int width = (setting.getFieldWidth() != 0) ? setting.getFieldWidth() : setting.getPrecision() + 2;
				String format = String.format("%%1$%d.%df", width, setting.getPrecision());
				builder.append(",").append(String.format(format, (Double)value));
				return true;
			}
			return false;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			return false;
		}
	}

}
