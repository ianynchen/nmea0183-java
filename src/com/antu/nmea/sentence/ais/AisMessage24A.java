package com.antu.nmea.sentence.ais;

import com.antu.nmea.annotation.MessageField;

public class AisMessage24A extends AbstractAisMessage {

	public AisMessage24A() {
		this.messageId = 24;
	}

	@MessageField(order = 4, requiredBits = 2, fieldType = "short")
	public short partNumber;
	
	@MessageField(order = 5, requiredBits = 120, fieldType = "string")
	public String name;
}
