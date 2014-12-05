package com.antu.nmea.sentence.field.codec;

import java.lang.reflect.Field;

import com.antu.nmea.annotation.FieldSetting;

public class BooleanSentenceFieldCodec extends AbstractSentenceFieldCodec {

	@Override
	public String fieldCodecType() {
		return "boolean";
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
			} else {
				try {
					field.set(obj, Boolean.getBoolean(setting.getDefaultValue()));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					return false;
				}
				return true;
			}
		};
		
		if (segments[startIndex].equals("A") || segments[startIndex].equals("a")) {
			
			try {
				field.set(obj, true);
				return true;
			} catch (IllegalArgumentException | IllegalAccessException e) {
				return false;
			}
			
		} else if (segments[startIndex].equals("V") || segments[startIndex].equals("v")) {
			
			try {
				field.set(obj, false);
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
			 
			if (value == null) {
				if (setting.isRequired()) {
					return false;
				} else {
					builder.append(",");
					return true;
				}
			}
			if (value instanceof Boolean) {
				if ((Boolean)value) {
					builder.append(",A");
				} else {
					builder.append(",V");
				}
				return true;
			}
			return false;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			return false;
		}
	}
}
