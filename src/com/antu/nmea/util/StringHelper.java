package com.antu.nmea.util;

public class StringHelper {

	static public String capitalizeFirstChar(String str) {
		
		char[] chars = str.toCharArray();
		chars[0] = Character.toUpperCase(chars[0]);
		
		return new String(chars);
	}
	
	static public byte toByte(char c) {
		switch (c) {
		case '1':
			return (byte)0x01;
		case '2':
			return (byte)0x02;
		case '3':
			return (byte)0x03;
		case '4':
			return (byte)0x04;
		case '5':
			return (byte)0x05;
		case '6':
			return (byte)0x06;
		case '7':
			return (byte)0x07;
		case '8':
			return (byte)0x08;
		case '9':
			return (byte)0x09;
		case 'A':
			return (byte)0x0A;
		case 'B':
			return (byte)0x0B;
		case 'C':
			return (byte)0x0C;
		case 'D':
			return (byte)0x0D;
		case 'E':
			return (byte)0x0E;
		case 'F':
			return (byte)0x0F;
		default: 
			return (byte)0;
		}
	}
	
	static public byte toByte(char a, char b) {
		
		return (byte) ((StringHelper.toByte(a) << 4) | (StringHelper.toByte(b)));
	}

	static public char fromHex(char c) {
		switch (c) {
		case 1:
			return '1';
		case 2:
			return '2';
		case 3:
			return '3';
		case 4:
			return '4';
		case 5:
			return '5';
		case 6:
			return '6';
		case 7:
			return '7';
		case 8:
			return '8';
		case 9:
			return '9';
		case 0x0A:
			return 'A';
		case 0x0B:
			return 'B';
		case 0x0C:
			return 'C';
		case 0x0D:
			return 'D';
		case 0x0E:
			return 'E';
		case 0x0F:
			return 'F';
		default:
			return '0';
		}
	}
}
