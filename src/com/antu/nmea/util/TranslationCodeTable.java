package com.antu.nmea.util;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public final class TranslationCodeTable {

	static private TranslationCodeTable Instance = new TranslationCodeTable();
	
	private HashMap<String, String> proprietaryCode = new HashMap<String, String>();
	
	private TranslationCodeTable() {
	}

	static public TranslationCodeTable instance() {
		return TranslationCodeTable.Instance;
	}
	
	public void addCode(String code, String encoding) {
		synchronized(this) {
			this.proprietaryCode.put(code, encoding);
		}
	}
	
	public boolean isTranslationCodeProprietary(String translationCode) {
		return translationCode.startsWith("P") && translationCode.length() == 4;
	}
	
	public String getEncoding(String translationCode) {
		
		try {
			int encoding = Integer.parseInt(translationCode);
			
			return "ISO8859_" + new Integer(encoding).toString();
		} catch (NumberFormatException e) {
			
			if (translationCode.equals("U")) {
				return "UTF-16";
			} else if (translationCode .equals("A")) {
				return "ASCII";
			} else if (translationCode.startsWith("P")) {
				String code = translationCode.substring(1);
				
				if (this.proprietaryCode.containsKey(code)) {
					return this.proprietaryCode.get(code);
				} else {
					return null;
				}
			}
		}
		return null;
	}
	
	public String decode(String translationCode, String textCode) {
		
		if (translationCode == null || translationCode.isEmpty() ||
				textCode == null || textCode.isEmpty())
			return null;
		
		String encoding = this.getEncoding(translationCode);
		
		if (encoding != null) {
			
			byte[] bytes = new byte[textCode.length() / 2];
			for (int i = 0; i < bytes.length; i++) {
				char a = textCode.charAt(i * 2);
				char b = textCode.charAt(i * 2 + 1);
				
				bytes[i] = StringHelper.toByte(a, b);
			}
			
			try {
				return new String(bytes, encoding);
			} catch (UnsupportedEncodingException e) {
				return null;
			}
		}
		
		return null;
	}
	
	public String encode(String translationCode, String text) {
				
		if (translationCode == null || translationCode.isEmpty() ||
				text == null || text.isEmpty() || text.length() % 2 != 0)
			return null;
		
		String encoding = this.getEncoding(translationCode);
		
		if (encoding != null) {
			
			try {
				byte[] bytes = text.getBytes(encoding);
				
				StringBuilder builder = new StringBuilder();
				for (byte b : bytes) {
					char c = (char) ((b & 0xF0) >> 4);
					char d = (char) (b & 0x0F);
					
					builder.append(StringHelper.fromHex(c)).append(StringHelper.fromHex(d));
				}
				return builder.toString();
				
			} catch (UnsupportedEncodingException e) {
				return null;
			}
		}
		return null;
	}
}
