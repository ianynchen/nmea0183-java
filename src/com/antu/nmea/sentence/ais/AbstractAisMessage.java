package com.antu.nmea.sentence.ais;

import com.antu.nmea.annotation.MessageField;

abstract public class AbstractAisMessage implements IEncapsulatedAisMessage {

	public AbstractAisMessage() {
	}
	
	@Override
	public int messageType() {
		return this.messageId;
	}

	@MessageField(order = 1, requiredBits = 6, fieldType = "short")
	public short messageId;
	
	@MessageField(order = 2, requiredBits = 2, fieldType = "short")
	public short repeatIndicator;
	
	@MessageField(order = 3, requiredBits = 30, fieldType = "integer")
	public int mmsi;

}
