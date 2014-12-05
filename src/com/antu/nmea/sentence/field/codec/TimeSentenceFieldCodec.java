package com.antu.nmea.sentence.field.codec;

import java.lang.reflect.Field;

import com.antu.nmea.annotation.FieldSetting;
import com.antu.nmea.util.UtcTime;

public class TimeSentenceFieldCodec extends AbstractSentenceFieldCodec {

	public TimeSentenceFieldCodec() {
		super();
	}

	@Override
	public String fieldCodecType() {
		return "time";
	}

	@Override
	public int requiredSegments() {
		return 1;
	}

	@Override
	protected boolean doDecode(String[] segments, Object obj, Field field,
			FieldSetting setting, int startIndex) {
		
		if (this.isNeededSegmentsEmpty(segments, startIndex)) {
			if (setting.isRequired()) {
				return false;
			} else {
				segments[startIndex] = setting.getDefaultValue();
			}
		}
		
		Integer hour = FieldCodecHelper.parseInt(segments[startIndex], 0, 2);
		Integer minute = FieldCodecHelper.parseInt(segments[startIndex], 2, 2);
		Double second = FieldCodecHelper.parseDouble(segments[startIndex], 4, 0);
		
		if (hour != null && minute != null && second != null) {
			
			UtcTime value = new UtcTime();
			value.hour = hour;
			value.minute = minute;
			value.second = second;
			
			if (value.isValid()) {

				try {
					field.set(obj, value);
					return true;
				} catch (IllegalArgumentException | IllegalAccessException e) {
					return false;
				}
			} else
				return false;
			
		}
		return false;
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
			
			if (value instanceof UtcTime) {
				UtcTime time = (UtcTime)value;
				builder.append(',').append(String.format("%02d%02d%5.2f", time.hour, time.minute, time.second));
			}
			return false;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			return false;
		}
	}

}
