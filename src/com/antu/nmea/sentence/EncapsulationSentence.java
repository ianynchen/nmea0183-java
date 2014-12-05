package com.antu.nmea.sentence;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class EncapsulationSentence extends NmeaSentence implements EncapsulationItem {
	
	private ArrayList<Byte> rawData = new ArrayList<Byte>();

	public EncapsulationSentence() {
		super();
	}
	
	public EncapsulationSentence(Date date) {
		super(date);
	}
	
	public EncapsulationSentence(long currentTimeSinceEpochInSeconds) {
		super(currentTimeSinceEpochInSeconds);
	}
	
	@Override
	public void concatenate(IGroupedSentence sentence) {
		
		if (sentence instanceof EncapsulationSentence) {
			
			EncapsulationSentence newSen = (EncapsulationSentence)sentence;
			
			this.increaseSentenceNumber();
			this.rawData.addAll(newSen.rawData);
		}
	}

	@Override
	public boolean addEncapsulatedData(String content, int fillBits) {

		Byte[] sixBytes = EncapsulationSentence.convertStringToBits(content);
		
		if (sixBytes != null &&
				this.rawData.addAll(EncapsulationSentence.getBits(sixBytes, fillBits))) {
			this.increaseSentenceNumber();
			return true;
		} else {
			return false;
		}
	}
	
	public boolean decodeStringData(String content) {
		Byte[] sixBytes = EncapsulationSentence.convertStringToBits(content);
		
		if (sixBytes != null &&
				this.rawData.addAll(EncapsulationSentence.getBits(sixBytes, this.getFillBits()))) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean isComplete() {
		return this.getTotalNumberOfSentences() == this.getSentenceNumber();
	}

	@Override
	public void cleanup() {
		this.rawData.clear();
	}

	@Override
	public List<Byte> getRawData() {
		return this.rawData;
	}

	static public Byte sixBitToByte(byte sixBit) {
		
		if (sixBit < 0 || sixBit - (sixBit & 0x3F) != 0)
			return null;
		
		byte value = sixBit;
		if (sixBit < 0x28) {
			value += 0x30;
		} else {
			value += 0x38;
		}
		return value;
	}
	
	static public Byte byteToSixBit(byte value) {
		
		if ((value >= 0x30 && value <= 0x57) || (value >= 0x60 && value <= 0x77)) {
			
			short result = value;
			result += 0x28;
			
			if (result > 0x80) {
				result += 0x20;
			} else {
				result += 0x28;
			}
			
			return (byte) (result & 0x3F);
		}
		else
			return null;
	}
	
	static public String convertSixBitsToString(Byte[] sixBits) {
		
		int length = sixBits.length;
		
		int remainder = length % 6;
		int paddingLength = remainder == 0 ? 0 : 6 - remainder;
		
		Byte[] paddedBits = new Byte[length + paddingLength];
		
		for (int i = 0; i < length; i++) {
			paddedBits[i] = sixBits[i];
		}
		
		Byte[] content = new Byte[(length + paddingLength) / 6];
		
		for (int i = 0; i < content.length; i++) {
			
			int pos = i * 6;
			content[i] = 0;
			content[i] = (byte) (content[i] | (sixBits[pos] << 5));
			content[i] = (byte) (content[i] | (sixBits[pos + 1] << 4));
			content[i] = (byte) (content[i] | (sixBits[pos + 2] << 3));
			content[i] = (byte) (content[i] | (sixBits[pos + 3] << 2));
			content[i] = (byte) (content[i] | (sixBits[pos + 4] << 1));
			content[i] = (byte) (content[i] | (sixBits[pos + 5]));
		}
		
		byte[] byteContent = new byte[content.length];
		for (int i = 0; i < content.length; i++)
			byteContent[i] = EncapsulationSentence.sixBitToByte(content[i]);
		
		try {
			String result = new String(byteContent, "ASCII");
			return result;
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}
	
	static public Byte[] convertStringToBits(String content) {
		
		try {
			byte[] byteContent = content.getBytes("ASCII");
			
			Byte[] result = new Byte[byteContent.length * 6];
			
			for (int i = 0; i < byteContent.length; i++) {
				Byte temp = EncapsulationSentence.byteToSixBit(byteContent[i]);
				
				if (temp == null)
					return null;
				
				result[i * 6] = (byte) ((temp & 0x20) >> 5);
				result[i * 6 + 1] = (byte) ((temp & 0x10) >> 4);
				result[i * 6 + 2] = (byte) ((temp & 0x08) >> 3);
				result[i * 6 + 3] = (byte) ((temp & 0x04) >> 2);
				result[i * 6 + 4] = (byte) ((temp & 0x02) >> 1);
				result[i * 6 + 5] = (byte) (temp & 0x01);
			}
			return result;
		} catch (UnsupportedEncodingException e) {
			
			return null;
		}
	}
	
	static public Byte[] convertBitsToSixBits(List<Byte> bits) {
		
		int length = (int) Math.ceil(bits.size() / 6);
		
		Byte[] bytes = new Byte[length];
		for (int i = 0; i < length; i++) {
			
			if (i == length - 1) {
				int fillBits = bits.size() % 6;

				byte b = 0;
				for (int j = 0; j < fillBits; j++) {
					b |= (bits.get(i * 6 + fillBits) << (fillBits - j - 1));					
				}
				
				bytes[i] = b;
			} else {
				byte b = 0;
				b |= (bits.get(i * 6) << 5);
				b |= (bits.get(i * 6 + 1) << 4);
				b |= (bits.get(i * 6 + 2) << 3);
				b |= (bits.get(i * 6 + 3) << 2);
				b |= (bits.get(i * 6 + 4) << 1);
				b |= bits.get(i * 6 + 5);
				
				bytes[i] = b;
			}
		}
		
		return bytes;
	}
	
	static public List<Byte> getBits(Byte[] sixBytes, int fillBits) {
		List<Byte> result = new ArrayList<Byte>();
		
		int padding = (fillBits == 0) ? 0 : fillBits;
		
		for (int i = 0; i < sixBytes.length - padding; i++) {
			result.add(sixBytes[i]);
		}
		
		return result;
	}
}
