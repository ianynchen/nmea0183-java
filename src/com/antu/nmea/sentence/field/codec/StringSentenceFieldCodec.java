package com.antu.nmea.sentence.field.codec;

import java.lang.reflect.Field;

import com.antu.nmea.annotation.FieldSetting;

public class StringSentenceFieldCodec extends AbstractSentenceFieldCodec {

	@Override
	public String fieldCodecType() {
		return "string";
	}

	@Override
	public int requiredSegments() {
		return 1;
	}

	@Override
	protected boolean doDecode(String[] segments, Object obj, Field field,
			FieldSetting setting, int startIndex) {

		try {
			if (this.isNeededSegmentsEmpty(segments, startIndex)) {
				if (setting.isRequired())
					return false;
				else {
					field.set(obj, setting.getDefaultValue());
					return true;
				}
			}
			field.set(obj, segments[startIndex]);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			return false;
		}
		return true;
	}

	@Override
	protected boolean doEncode(StringBuilder builder, Object obj, Field field,
			FieldSetting setting) {

		try {
			Object value = field.get(obj);
			
			if (value == null) {
				if (setting.isRequired())
					return false;
				else {
					builder.append(",");
					return true;
				}
			}
			builder.append(',').append(value.toString());
		} catch (IllegalArgumentException | IllegalAccessException e) {
			
			return false;
		}

		return true;
	}

}
