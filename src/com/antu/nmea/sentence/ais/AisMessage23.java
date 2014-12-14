package com.antu.nmea.sentence.ais;

import com.antu.nmea.annotation.MessageField;

public class AisMessage23 extends AbstractAisMessage {

	public AisMessage23() {
		this.messageId = 23;
	}

	@MessageField(order = 1, requiredBits = 2, fieldType = "short")
	public short spare;
	
	@MessageField(order = 2, requiredBits = 18, fieldType = "shortLongitude")
	public double longitude1;
	
	@MessageField(order = 3, requiredBits = 17, fieldType = "shortLatitude")
	public double latitude1;
	
	@MessageField(order = 4, requiredBits = 18, fieldType = "shortLongitude")
	public double longitude2;
	
	@MessageField(order = 5, requiredBits = 17, fieldType = "shortLatitude")
	public double latitude2;
	
	@MessageField(order = 6, requiredBits = 4, fieldType = "short")
	public short stationType;
	
	@MessageField(order = 7, requiredBits = 8, fieldType = "short")
	public short typeOfShipAndCargoType;
	
	@MessageField(order = 8, requiredBits = 22, fieldType = "integer")
	public int spare2;
	
	@MessageField(order = 9, requiredBits = 2, fieldType = "short")
	public short tranceivingMode;
	
	@MessageField(order = 10, requiredBits = 4, fieldType = "short")
	public short reportingInterval;
	
	@MessageField(order = 11, requiredBits = 4, fieldType = "short")
	public short quietTime;
	
	@MessageField(order = 12, requiredBits = 6, fieldType = "short")
	public short spare3;
}
