package com.antu.nmea.sentence.ais;

import com.antu.nmea.annotation.MessageField;
import com.antu.nmea.util.Dimension;

public class AisMessage19 extends AbstractAisMessage {

	public AisMessage19() {
		this.messageId = 19;
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
	
	@MessageField(order = 12, requiredBits = 4, fieldType = "short")
	public short spare2;
	
	@MessageField(order = 13, requiredBits = 120, fieldType = "string")
	public String name;
	
	@MessageField(order = 14, requiredBits = 8, fieldType = "short")
	public short typeOfShipAndCargoType;
	
	@MessageField(order = 15, requiredBits = 30, fieldType = "short")
	public Dimension dimension;
	
	@MessageField(order = 16, requiredBits = 4, fieldType = "short")
	public short typeOfPositionFixingDevice;
	
	@MessageField(order = 17, requiredBits = 1, fieldType = "short")
	public short raimFlag;
	
	@MessageField(order = 18, requiredBits = 1, fieldType = "short")
	public short dte;
	
	@MessageField(order = 19, requiredBits = 1, fieldType = "short")
	public short assignedModeFlag;
	
	@MessageField(order = 20, requiredBits = 4, fieldType = "short")
	public short spare3;
}
