package com.antu.nmea.sentence.ais;

import com.antu.nmea.annotation.MessageField;
import com.antu.util.PrintableList;

public class AisMessage4 extends AbstractAisMessage {

	public AisMessage4() {
		this.messageId = 4;
	}
	
	@MessageField(order = 4, requiredBits = 14, fieldType = "short")
	public short utcYear = 0;
	
	@MessageField(order = 5, requiredBits = 4, fieldType = "short")
	public short utcMonth = 0;
	
	@MessageField(order = 6, requiredBits = 5, fieldType = "short")
	public short utcDay = 0;
	
	@MessageField(order = 7, requiredBits = 5, fieldType = "short")
	public short utcHour = 24;
	
	@MessageField(order = 8, requiredBits = 6, fieldType = "short")
	public short utcMinute = 60;
	
	@MessageField(order = 9, requiredBits = 6, fieldType = "short")
	public short utcSecond = 60;
	
	@MessageField(order = 10, requiredBits = 1, fieldType = "short")
	public short positionAccuracy = 0;
	
	@MessageField(order = 11, requiredBits = 28, fieldType = "longitude")
	public double longitude = AbstractAisSentence.LONGITUDE_NOT_AVAILABLE;
	
	@MessageField(order = 12, requiredBits = 27, fieldType = "latitude")
	public double latitude = AbstractAisSentence.LATITUDE_NOT_AVAILABLE;
	
	@MessageField(order = 13, requiredBits = 4, fieldType = "short")
	public short typeOfElectronicPositionFixingDevice = 0;
	
	@MessageField(order = 14, requiredBits = 10, fieldType = "short")
	public short spare = 0;
	
	@MessageField(order = 15, requiredBits = 1, fieldType = "short")
	public short raimFlag = 0;
	
	@MessageField(order = 16, requiredBits = 19, fieldType = "bits")
	public PrintableList<Byte> communicationState;
}
