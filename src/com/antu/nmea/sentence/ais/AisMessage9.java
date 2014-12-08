package com.antu.nmea.sentence.ais;

import java.util.List;

import com.antu.nmea.annotation.MessageField;

public class AisMessage9 extends AbstractAisMessage {

	public AisMessage9() {
		this.messageId = 9;
	}

	@MessageField(order = 4, requiredBits = 12, fieldType = "short")
	public short altitude;
	
	@MessageField(order = 5, requiredBits = 10, fieldType = "speed")
	public double speedOverGroud = AbstractAisSentence.SPEED_NOT_AVAILABLE;
	
	@MessageField(order = 6, requiredBits = 1, fieldType = "short")
	public short positionAccuracy;
	
	@MessageField(order = 7, requiredBits = 28, fieldType = "longitude")
	public double longitude = AbstractAisSentence.LONGITUDE_NOT_AVAILABLE;
	
	@MessageField(order = 8, requiredBits = 27, fieldType = "latitude")
	public double latitude = AbstractAisSentence.LATITUDE_NOT_AVAILABLE;
	
	@MessageField(order = 9, requiredBits = 12, fieldType = "course")
	public double courseOverGround = AbstractAisSentence.COURSE_NOT_AVAILABLE;
	
	@MessageField(order = 10, requiredBits = 6, fieldType = "short")
	public short timestamp;
	
	@MessageField(order = 11, requiredBits = 1, fieldType = "short")
	public short altitudeSensor;
	
	@MessageField(order = 12, requiredBits = 7, fieldType = "short")
	public short spare;
	
	@MessageField(order = 13, requiredBits = 1, fieldType = "short")
	public short dte;
	
	@MessageField(order = 14, requiredBits = 3, fieldType = "short")
	public short spare2;
	
	@MessageField(order = 15, requiredBits = 1, fieldType = "short")
	public short assignedModeFlag;
	
	@MessageField(order = 16, requiredBits = 1, fieldType = "short")
	public short raimFlag;
	
	@MessageField(order = 17, requiredBits = 1, fieldType = "short")
	public short communicationStateSelectorFlag;
	
	@MessageField(order = 18, requiredBits = 19, fieldType = "bits")
	public List<Byte> communicationState;
}
