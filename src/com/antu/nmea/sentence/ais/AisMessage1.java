package com.antu.nmea.sentence.ais;

import com.antu.nmea.annotation.MessageField;
import com.antu.util.PrintableList;

public class AisMessage1 extends AbstractAisMessage {

	public AisMessage1() {
		this.messageId = 1;
	}
	
	@MessageField(order = 4, requiredBits = 4, fieldType = "short")
	public short navigationalStatus;
	
	@MessageField(order = 5, requiredBits = 8, fieldType = "rateOfTurn")
	public double rateOfTurn = AbstractAisSentence.RATE_OF_TURN_NOT_AVAILABLE;
	
	@MessageField(order = 6, requiredBits = 10, fieldType = "speed")
	public double speedOverGround = 102.3;
	
	@MessageField(order = 7, requiredBits = 1, fieldType = "short")
	public short positionAccuracy;
	
	@MessageField(order = 8, requiredBits = 28, fieldType = "longitude")
	public double longitude = 181;
	
	@MessageField(order = 9, requiredBits = 27, fieldType = "latitude")
	public double latitude = 91;
	
	@MessageField(order = 10, requiredBits = 12, fieldType = "course")
	public double courseOverGround = 360;
	
	@MessageField(order = 11, requiredBits = 9, fieldType = "heading")
	public short trueHeading = 511;
	
	@MessageField(order = 12, requiredBits = 6, fieldType = "short")
	public short timestamp = 60;
	
	@MessageField(order = 13, requiredBits = 2, fieldType = "short")
	public short specialManoeuvreIndicator = 0;
	
	@MessageField(order = 14, requiredBits = 3, fieldType = "short")
	public short spare = 0;
	
	@MessageField(order = 15, requiredBits = 1, fieldType = "short")
	public short raimFlag = 0;
	
	@MessageField(order = 16, requiredBits = 19, fieldType = "bits")
	public PrintableList<Byte> communicationState;
}
