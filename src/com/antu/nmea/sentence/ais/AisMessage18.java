package com.antu.nmea.sentence.ais;

import java.util.List;

import com.antu.nmea.annotation.MessageField;

public class AisMessage18 extends AbstractAisMessage {

	public AisMessage18() {
		this.messageId = 18;
	}

	@MessageField(order = 4, requiredBits = 8, fieldType = "short")
	public short spare;
	
	@MessageField(order = 5, requiredBits = 10, fieldType = "speed")
	public double speedOverGround = AbstractAisSentence.SPEED_NOT_AVAILABLE;
	
	@MessageField(order = 6, requiredBits = 1, fieldType = "short")
	public short positionAccuracy;
	
	@MessageField(order = 7, requiredBits = 28, fieldType = "longitude")
	public double longitude = AbstractAisSentence.LONGITUDE_NOT_AVAILABLE;
	
	@MessageField(order = 8, requiredBits = 27, fieldType = "laitutde")
	public double latitude = AbstractAisSentence.LATITUDE_NOT_AVAILABLE;
	
	@MessageField(order = 9, requiredBits = 12, fieldType = "course")
	public double courseOverGround = AbstractAisSentence.COURSE_NOT_AVAILABLE;
	
	@MessageField(order = 10, requiredBits = 9, fieldType = "heading")
	public short trueHeading = AbstractAisSentence.HEADING_NOT_AVAILABLE;
	
	@MessageField(order = 11, requiredBits = 6, fieldType = "short")
	public short timestamp = 60;
	
	@MessageField(order = 12, requiredBits = 2, fieldType = "short")
	public short spare2;
	
	@MessageField(order = 13, requiredBits = 1, fieldType = "short")
	public short classBUnitFlag;
	
	@MessageField(order = 14, requiredBits = 1, fieldType = "short")
	public short classBDisplayFlag;
	
	@MessageField(order = 15, requiredBits = 1, fieldType = "short")
	public short classBDSCFlag;
	
	@MessageField(order = 16, requiredBits = 1, fieldType = "short")
	public short classBBandFlag;
	
	@MessageField(order = 17, requiredBits = 1, fieldType = "short")
	public short classBMessage22Flag;
	
	@MessageField(order = 18, requiredBits = 1, fieldType = "short")
	public short modeFlag;
	
	@MessageField(order = 19, requiredBits = 1, fieldType = "short")
	public short raimFlag;
	
	@MessageField(order = 20, requiredBits = 1, fieldType = "short")
	public short communicationStateSelectorFlag;
	
	@MessageField(order = 21, requiredBits = 19, fieldType = "bits")
	public List<Byte> communicationState;
}
