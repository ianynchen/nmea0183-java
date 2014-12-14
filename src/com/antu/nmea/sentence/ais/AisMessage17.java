package com.antu.nmea.sentence.ais;

import java.util.List;

import com.antu.nmea.annotation.MessageField;
import com.antu.nmea.util.DGNSSData;

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
	
	@MessageField(order = 8, requiredBits = 6, fieldType = "short")
	public Short messageType;
	
	@MessageField(order = 9, requiredBits = 10, fieldType = "short")
	public Short stationId;
	
	@MessageField(order = 10, requiredBits = 13, fieldType = "short")
	public Short zCount;
	
	@MessageField(order = 11, requiredBits = 3, fieldType = "short")
	public Short sequenceNumber;
	
	@MessageField(order = 12, requiredBits = 5, fieldType = "short")
	public Short N;
	
	@MessageField(order = 13, requiredBits = 3, fieldType = "short")
	public Short health;
	
	@MessageField(order = 14, fieldType = "list", isGroup = true, groupItemClass = "com.antu.nmea.util.DGNSSData")
	public List<DGNSSData> dgnssData;
}
