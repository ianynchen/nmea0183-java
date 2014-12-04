package com.antu.nmea.sentence.field.codec;

public class FieldCodecHelper {
	
	public static Short parseShort(String segment, int startIndex, int length) {
		
		short result = 0;
		int end = length > 0 ? startIndex + length - 1 : segment.length() - 1;

		if (length > segment.length() - startIndex)
			return null;
		
		String str = segment.substring(startIndex, end);
		
		try {
			result = Short.parseShort(str);
			return new Short(result);
		} catch (NumberFormatException e) {
			return null;
		}

		/*int i = startIndex;
		boolean isNegative = false;
		if (segment.charAt(startIndex + 0) == '-') {
			i = 1;
			isNegative = true;
		} else if (segment.charAt(startIndex + 0) == '+') {
			i = 1;
		}

		for (; i < end; i++) {
			short digit = (short)(segment.charAt(i) - '0');

			if (digit >= 0 && digit <= 9) {
				result = (short)(result * 10 + digit);
			} else {
				return null;
			}
		}

		if (!isNegative) {
			return new Short(result);
		} else {
			return new Short((short) -result);
		}*/
	}
	
	public static Integer parseInt(String segment, int startIndex, int length) {
		
		int result = 0;
		int end = length > 0 ? startIndex + length - 1 : segment.length() - 1;

		if (length > segment.length() - startIndex)
			return null;

		String str = segment.substring(startIndex, end);
		
		try {
			result = Integer.parseInt(str);
			return new Integer(result);
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	public static Double parseDouble(String segment, int startIndex, int length) {
		
		double result = 0;

		int end = length > 0? startIndex + length - 1 : segment.length() - 1;

		if (length > segment.length() - startIndex)
			return null;
		
		String str = segment.substring(startIndex, end);
		try {
			result = Double.parseDouble(str);
			return new Double(result);
		} catch (NumberFormatException e) {
			return null;
		}
	}
}
