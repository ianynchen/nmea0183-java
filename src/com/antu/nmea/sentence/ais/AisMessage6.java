package com.antu.nmea.sentence.ais;

import com.antu.nmea.annotation.MessageField;
import com.antu.nmea.sentence.ais.ifm.AbstractIfmSegment;

public class AisMessage6 extends AbstractAisMessage {

	public AisMessage6() {
		this.messageId = 6;
	}

	@MessageField(order = 4, requiredBits = 2, fieldType = "short")
	public short sequenceNumber;
	
	@MessageField(order = 5, requiredBits = 30, fieldType = "integer")
	public int destinationId;
	
	@MessageField(order = 6, requiredBits = 1, fieldType = "byte")
	public byte retransmitFlag;
	
	@MessageField(order = 7, requiredBits = 1, fieldType = "byte")
	public byte spare;
	
	@MessageField(order = 8, requiredBits = 936, fieldType = "ifm")
	public AbstractIfmSegment ifm;
}
