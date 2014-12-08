package com.antu.nmea.sentence.ais;

import java.util.List;

import com.antu.nmea.annotation.MessageField;

public class AisMessage17 extends AbstractAisMessage {

	public AisMessage17() {
		this.messageId = 17;
	}

	@MessageField(order = 4, requiredBits = 2, fieldType = "short")
	public short spare;
	
	@MessageField(order = 5, requiredBits = 18, fieldType = "shortLongitude")
	public double longitude;
	
	@MessageField(order = 6, requiredBits = 17, fieldType = "shortLatitude")
	public double latitude;
	
	@MessageField(order = 7, requiredBits = 5, fieldType = "short")
	public short spare2;
	
	@MessageField(order = 8, requiredBits = 0, fieldType = "bits")
	public List<Byte> data;
}
