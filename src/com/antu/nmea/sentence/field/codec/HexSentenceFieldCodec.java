package com.antu.nmea.sentence.field.codec;

import java.lang.reflect.Field;

import com.antu.nmea.annotation.FieldSetting;

public class HexSentenceFieldCodec extends AbstractSentenceFieldCodec {

	public HexSentenceFieldCodec() {
	}

	@Override
	public String fieldCodecType() {
		return "hex";
	}

	@Override
	public int requiredSegments() {
		return 1;
	}

	public static Integer toInteger(String hexString) {
		if (hexString == null || hexString.isEmpty())
			return null;
 
		int result = 0;
		hexString = hexString.toLowerCase();
		for (int i = 0; i < hexString.length(); i += 2) {
			byte high = (byte) (Character.digit(hexString.charAt(i), 16) & 0xff);
			byte low = (byte) (Character.digit(hexString.charAt(i + 1), 16) & 0xff);
			result = result << 8 | (byte) (high << 4 | low);
		}
		return new Integer(result);
	}

	/**
	 * 
	 * @param integer
	 * @param width number of half bytes
	 * @return
	 */
	public static String toHexString(int integer, int width) {
 
		final StringBuilder hexString = new StringBuilder();
		
		byte b0 = (byte) ((integer & 0xFF000000) >> 24);
		byte b1 = (byte) ((integer & 0x00FF0000) >> 16);
		byte b2 = (byte) ((integer & 0x0000FF00) >> 8);
		byte b3 = (byte) (integer & 0x000000FF);

		byte[] byteArray = new byte[4];
		byteArray[0] = b0;
		byteArray[1] = b1;
		byteArray[2] = b2;
		byteArray[3] = b3;
		
		for (int i = 0; i < byteArray.length; i++) {
			if ((byteArray[i] & 0xff) < 0x10)//0~FÇ°Ãæ²»Áã
				hexString.append("0");
			hexString.append(Integer.toHexString(0xFF & byteArray[i]));
		}
		String hex = hexString.toString().toUpperCase();
		
		if (width == 0) {
			return hex;
		} else {
			return hex.substring(8 - width);
		}
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
			
			Integer value = HexSentenceFieldCodec.toInteger(segments[startIndex]);
			if (value != null)
				field.set(obj, value);
			else
				return false;
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
			
			if (value instanceof Integer) {
				
				builder.append(',').append(HexSentenceFieldCodec.toHexString((int) value, 
						setting.getFieldWidth()));
			} else {
				return false;
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			return false;
		}

		return true;
	}
}
