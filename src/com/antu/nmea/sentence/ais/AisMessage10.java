package com.antu.nmea.sentence.ais;

import com.antu.nmea.annotation.MessageField;

public class AisMessage10 extends AbstractAisMessage {

	public AisMessage10() {
		this.messageId = 10;
	}

	@MessageField(order = 4, requiredBits = 2, fieldType = "short")
	public short spare1;
	
	@MessageField(order = 5, requiredBits = 30, fieldType = "integer")
	public int destinationId;
	
	@MessageField(order = 6, requiredBits = 2, fieldType = "short")
	public short spare2;
}
