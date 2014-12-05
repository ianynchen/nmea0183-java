package com.antu.nmea.sentence.field.codec;

import java.lang.reflect.Field;

import com.antu.nmea.annotation.FieldSetting;

public class LongitudeSentenceFieldCodec extends AbstractSentenceFieldCodec {

	@Override
	public String fieldCodecType() {
		return "longitude";
	}

	@Override
	public int requiredSegments() {
		return 2;
	}

	@Override
	protected boolean doDecode(String[] segments, Object obj, Field field,
			FieldSetting setting, int startIndex) {
		
		if (this.isNeededSegmentsEmpty(segments, startIndex)) {
			if (setting.isRequired()) {
				return false;
			} else {
				try {
					field.set(obj, Double.parseDouble(setting.getDefaultValue()));
					return true;
				} catch (IllegalArgumentException
						| IllegalAccessException e) {
					return false;
				}
			}
		}
		
		Integer degree = FieldCodecHelper.parseInt(segments[startIndex], 0, 3);
		
		if (degree != null) {
			
			Double minute = FieldCodecHelper.parseDouble(segments[startIndex], 2, 0);
			double total = degree + minute;
			
			if (segments[startIndex + 1].equals("W") || segments[startIndex + 1].equals("w")) {
				total = -total;
			} else if (!segments[startIndex + 1].equals("E") && !segments[startIndex + 1].equals("e")) {
				return false;
			}
			
			try {
				field.set(obj, new Double(total));
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
					builder.append(",,");
				}
			}
			
			if (value instanceof Double) {
				double total = (Double)value;
				
				boolean isNegative = total < 0;
				
				if (isNegative) total = -total;
				int degree = (int)(total / 100);
				
				double minute = total - degree * 100;
				
				String result = String.format("%03d%5.2f", degree, minute);
				if (isNegative) result += ",W";
				else result += ",E";
				
				builder.append(result);
				return true;
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			return false;
		}
		return false;
	}

}
