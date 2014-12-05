package com.antu.nmea.message.field.codec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;

import com.antu.nmea.util.StringHelper;

public class MessageFieldCodecHelper {
	
	static final Character MAPPING[] =
		{
			'@', 'A', 'B', 'C', 'D', 'E', 'F', 'G',
			'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
			'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
			'X', 'Y', 'Z', '[', '\\', ']', '^', '_',
			' ', '!', 0x22, '#', '$', '%', '&', '`',
			'(', ')', '*', '+',',', '-', '.', '/',
			'0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', ':', ';', '<', '=', '>', '?'
		};
	
	static final List<Character> MappingList = Arrays.asList(MAPPING);

	private MessageFieldCodecHelper() {
	}

	static public Byte parseByte(List<Byte> bits, int startIndex, int length, boolean keepSign) {
		
		if (length > 8 || bits.size() < startIndex + length)
			return null;
		
		byte result = 0;
		
		boolean isNegative = false;
		if (keepSign && bits.get(startIndex) == 1)
			isNegative = true;
		List<Byte> newBits = new ArrayList<Byte>();
		
		if (isNegative) {
			for (int i = 0; i < 8 - length; i++) {
				newBits.add((byte) 1);
			}
		} else {
			for (int i = 0; i < 8 - length; i++) {
				newBits.add((byte) 0);
			}
		}
		
		for (int i = 8 - length; i < 8; i++) {
			newBits.add(bits.get(startIndex + i - (8 - length)));
		}

		for (int i = 0; i < newBits.size(); i++) {
			result = (byte) (result << 1);
			result |= newBits.get(i);
		}

		return result;
	}
	
	static public Short parseShort(List<Byte> bits, int startIndex, int length, boolean keepSign) {
		
		if (length > 16 || bits.size() < startIndex + length)
			return null;
		
		short result = 0;
		
		boolean isNegative = false;
		if (keepSign && bits.get(startIndex) == 1)
			isNegative = true;
		List<Byte> newBits = new ArrayList<Byte>();
		
		if (isNegative) {
			for (int i = 0; i < 16 - length; i++) {
				newBits.add((byte) 1);
			}
		} else {
			for (int i = 0; i < 16 - length; i++) {
				newBits.add((byte) 0);
			}
		}
		
		for (int i = 16 - length; i < 16; i++) {
			newBits.add(bits.get(startIndex + i - (16 - length)));
		}

		for (int i = 0; i < newBits.size(); i++) {
			result = (short) (result << 1);
			result |= newBits.get(i);
		}

		return result;
	}
	
	static public Integer parseInteger(List<Byte> bits, int startIndex, int length, boolean keepSign) {
		
		if (length > 32 || bits.size() < startIndex + length)
			return null;
		
		int result = 0;
		
		boolean isNegative = false;
		if (keepSign && bits.get(startIndex) == 1)
			isNegative = true;
		List<Byte> newBits = new ArrayList<Byte>();
		
		if (isNegative) {
			for (int i = 0; i < 32 - length; i++) {
				newBits.add((byte) 1);
			}
		} else {
			for (int i = 0; i < 32 - length; i++) {
				newBits.add((byte) 0);
			}
		}
		
		for (int i = 32 - length; i < 32; i++) {
			newBits.add(bits.get(startIndex + i - (32 - length)));
		}

		for (int i = 0; i < newBits.size(); i++) {
			result = (int) (result << 1);
			result |= newBits.get(i);
		}

		return result;
	}
	
	static public Long parseLong(List<Byte> bits, int startIndex, int length, boolean keepSign) {
		
		if (length > 64 || bits.size() < startIndex + length)
			return null;
		
		long result = 0;
		
		boolean isNegative = false;
		if (keepSign && bits.get(startIndex) == 1)
			isNegative = true;
		List<Byte> newBits = new ArrayList<Byte>();
		
		if (isNegative) {
			for (int i = 0; i < 64 - length; i++) {
				newBits.add((byte) 1);
			}
		} else {
			for (int i = 0; i < 64 - length; i++) {
				newBits.add((byte) 0);
			}
		}
		
		for (int i = 64 - length; i < 64; i++) {
			newBits.add(bits.get(startIndex + i - (64 - length)));
		}

		for (int i = 0; i < newBits.size(); i++) {
			result = (long) (result << 1);
			result |= newBits.get(i);
		}

		return result;
	}
	
	static public Character parseChar(List<Byte> bits, int startIndex) {
		
		if (bits.size() < startIndex + 6)
			return null;
		
		char result = 0;
		for (int i = startIndex; i < startIndex + 6; i++){
			result = (char) (result << 1);
			result |= bits.get(i);
		}


		if (result >= 0 && result < 64){
			return MAPPING[result];
		}
		else {
			return null;
		}
	}
	
	static public String parseString(List<Byte> bits, int startIndex, int length, boolean removeAtSigns) {
		
		if (length <= 0 || bits.size() < startIndex + 6 * length) {
			return null;
		}
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i += 6) {
			Character c = MessageFieldCodecHelper.parseChar(bits, startIndex + i);
			
			if (c == null)
				return null;
			else
				sb.append(c);
		}
		
		if (removeAtSigns) {
			return sb.toString().replace('@', ' ').trim();
		}
		return sb.toString();
	}
	
	static public String removeAtSigns(String str) {
		return str.replace('@', ' ').trim();
	}
	
	static public String convertReservedChars(String str) {
		
		char[] content = str.toCharArray();
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < content.length;) {
			if (content[i] == '^') {
				 char a = content[i + 1];
				 char b = content[i + 2];
				 
				 char c = (char) StringHelper.toByte(a, b);
				 sb.append(c);
				 i += 3;
			} else {
				sb.append(content[i]);
				i++;
			}
		}
		return sb.toString();
	}
	
	static public boolean byteToBits(List<Byte> bits, byte value, int numberOfBits, boolean keepSign) {
		
		if (numberOfBits > 8)
			return false;
		
		BitSet bs = BitSet.valueOf(new long[] { value });
		for (int i = numberOfBits - 1; i >= 0; i--) {
			if (bs.get(i))
				bits.add((byte) 0x01);
			else
				bits.add((byte) 0x00);
		}
		return true;
	}
	
	static public boolean shortToBits(List<Byte> bits, short value, int numberOfBits, boolean keepSign) {
		
		if (numberOfBits > 16)
			return false;
		
		BitSet bs = BitSet.valueOf(new long[] { value });
		for (int i = numberOfBits - 1; i >= 0; i--) {
			if (bs.get(i))
				bits.add((byte) 0x01);
			else
				bits.add((byte) 0x00);
		}
		return true;
	}
	
	static public boolean integerToBits(List<Byte> bits, int value, int numberOfBits, boolean keepSign) {
		
		if (numberOfBits > 32)
			return false;
		
		BitSet bs = BitSet.valueOf(new long[] { value });
		for (int i = numberOfBits - 1; i >= 0; i--) {
			if (bs.get(i))
				bits.add((byte) 0x01);
			else
				bits.add((byte) 0x00);
		}
		return true;
	}
	
	static public boolean longToBits(List<Byte> bits, long value, int numberOfBits, boolean keepSign) {
		
		if (numberOfBits > 64)
			return false;
		
		BitSet bs = BitSet.valueOf(new long[] { value });
		for (int i = numberOfBits - 1; i >= 0; i--) {
			if (bs.get(i))
				bits.add((byte) 0x01);
			else
				bits.add((byte) 0x00);
		}
		return true;
	}
	
	static public boolean asciiCharToBits(List<Byte> bits, char c, int numberOfBits) {
		
		int index = MessageFieldCodecHelper.MappingList.indexOf(c);
		
		if ( index >= 0 ) {

			if (!MessageFieldCodecHelper.integerToBits(bits, index, 6, false))
				return false;
		}
		return true;
	}
	
	static public boolean stringToBits(List<Byte> bits, String value, int length) {
		
		if (value == null)
			value = "";
		
		List<Byte> tempBits = new ArrayList<Byte>();
		
		boolean success = true;
		for (int i = 0; i < ((length != 0) ? length : value.length()); i++) {
			char c = (i >= value.length()) ? '@' : value.charAt(i);
			
			int index = MessageFieldCodecHelper.MappingList.indexOf(c);
			if (index >= 0) {
				MessageFieldCodecHelper.asciiCharToBits(tempBits, c, 6);
			} else {
				MessageFieldCodecHelper.asciiCharToBits(tempBits, '^', 6);
				char a = (char) ((c & 0xF0) >> 4);
				char b = (char) (c & 0x0F);
				
				char ac = StringHelper.fromHex(a);
				char bc = StringHelper.fromHex(b);
				MessageFieldCodecHelper.asciiCharToBits(tempBits, ac, 6);
				MessageFieldCodecHelper.asciiCharToBits(tempBits, bc, 6);
			}
		}
		
		bits.addAll(tempBits);
		return success;
	}
}
