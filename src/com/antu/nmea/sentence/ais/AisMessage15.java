package com.antu.nmea.sentence.ais;

import com.antu.nmea.annotation.MessageField;

public class AisMessage15 extends AbstractAisMessage {

	public AisMessage15() {
		this.messageId = 15;
	}

	@MessageField(order = 4, requiredBits = 2, fieldType = "short")
	public short spare;
	
	@MessageField(order = 5, requiredBits = 30, fieldType = "integer")
	public int destinationId1;
	
	@MessageField(order = 6, requiredBits = 6, fieldType = "short")
	public short messageId11;
	
	@MessageField(order = 7, requiredBits = 12, fieldType = "short")
	public short slotOffset11;
	
	@MessageField(order = 8, requiredBits = 2, fieldType = "short")
	public short spare2;
	
	@MessageField(order = 7, requiredBits = 6, fieldType = "short")
	public short messageId12;
	
	@MessageField(order = 8, requiredBits = 12, fieldType = "short")
	public short slotOffset12;
	
	@MessageField(order = 9, requiredBits = 2, fieldType = "short")
	public short spare3;
	
	@MessageField(order = 10, requiredBits = 30, fieldType = "integer")
	public int destinationId2;
	
	@MessageField(order = 11, requiredBits = 6, fieldType = "short")
	public short messageId21;
	
	@MessageField(order = 12, requiredBits = 12, fieldType = "short")
	public short slotOffset21;
	
	@MessageField(order = 13, requiredBits = 2, fieldType = "short")
	public short spare4;
}
