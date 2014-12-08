package com.antu.nmea.sentence.ais;

import com.antu.nmea.annotation.MessageField;
import com.antu.nmea.sentence.ais.ifm.AbstractIfmSegment;

public class AisMessage8 extends AbstractAisMessage {

	public AisMessage8() {
		this.messageId = 8;
	}
	
	@MessageField(order = 4, requiredBits = 1, fieldType = "byte")
	public byte spare;
	
	@MessageField(order = 5, requiredBits = 968, fieldType = "ifm")
	public AbstractIfmSegment ifm;
}
