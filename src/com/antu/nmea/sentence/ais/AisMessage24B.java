package com.antu.nmea.sentence.ais;

import com.antu.nmea.annotation.MessageField;
import com.antu.nmea.util.Dimension;

public class AisMessage24B extends AbstractAisMessage {

	public AisMessage24B() {
		this.messageId = 24;
	}

	@MessageField(order = 4, requiredBits = 2, fieldType = "short")
	public short partNumber;
	
	@MessageField(order = 5, requiredBits = 8, fieldType = "short")
	public short typeOfShipAndCargoType;
	
	@MessageField(order = 6, requiredBits = 42, fieldType = "string")
	public String vendorId;
	
	@MessageField(order = 7, requiredBits = 42, fieldType = "string")
	public String callSign;
	
	@MessageField(order = 8, requiredBits = 30, fieldType = "dimension")
	public Dimension dimension;
	
	@MessageField(order = 9, requiredBits = 6, fieldType = "short")
	public short spare;
}
