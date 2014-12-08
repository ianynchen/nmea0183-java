package com.antu.nmea.sentence.ais;

import com.antu.nmea.annotation.MessageField;

public class AisMessage16 extends AbstractAisMessage {

	public AisMessage16() {
		this.messageId = 16;
	}

	@MessageField(order = 4, requiredBits = 2, fieldType = "short")
	public short spare;
	
	@MessageField(order = 5, requiredBits = 30, fieldType = "integer")
	public int destinationId1;

	@MessageField(order = 6, requiredBits = 12, fieldType = "short")
	public short offSet1;
	
	@MessageField(order = 7, requiredBits = 10, fieldType = "short")
	public short increment1;
	
	@MessageField(order = 8, requiredBits = 30, fieldType = "integer")
	public int destinationId2;

	@MessageField(order = 9, requiredBits = 12, fieldType = "short")
	public short offSet2;
	
	@MessageField(order = 10, requiredBits = 10, fieldType = "short")
	public short increment2;
	
	@MessageField(order = 11, requiredBits = 4, fieldType = "short")
	public short spare2;
}
