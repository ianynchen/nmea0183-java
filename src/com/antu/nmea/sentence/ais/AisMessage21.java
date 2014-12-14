package com.antu.nmea.sentence.ais;

import java.util.List;

import com.antu.nmea.annotation.MessageField;
import com.antu.nmea.util.Dimension;
import com.antu.util.PrintableList;

public class AisMessage21 extends AbstractAisMessage {

	public AisMessage21() {
		this.messageId = 21;
	}

	@MessageField(order = 4, requiredBits = 5, fieldType = "short")
	public short typeOfAidsToNavigation;
	
	@MessageField(order = 5, requiredBits = 120, fieldType = "string")
	public String nameOfAidsToNavigation;
	
	@MessageField(order = 6, requiredBits = 1, fieldType = "short")
	public short positionAccuracy;
	
	@MessageField(order = 7, requiredBits = 28, fieldType = "longitude")
	public double longitude = AbstractAisSentence.LONGITUDE_NOT_AVAILABLE;
	
	@MessageField(order = 8, requiredBits = 27, fieldType = "latitude")
	public double latitude = AbstractAisSentence.LATITUDE_NOT_AVAILABLE;
	
	@MessageField(order = 9, requiredBits = 30, fieldType = "short")
	public Dimension dimension;
	
	@MessageField(order = 10, requiredBits = 4, fieldType = "short")
	public short typeOfPositionFixingDevice;
	
	@MessageField(order = 11, requiredBits = 6, fieldType = "short")
	public short timestamp;
	
	@MessageField(order = 12, requiredBits = 1, fieldType = "short")
	public short offPositionIndicator;
	
	@MessageField(order = 13, requiredBits = 8, fieldType = "short")
	public short atonStatus;
	
	@MessageField(order = 14, requiredBits = 1, fieldType = "short")
	public short raimFlag;
	
	@MessageField(order = 15, requiredBits = 1, fieldType = "short")
	public short virtualAtonFlag;
	
	@MessageField(order = 16, requiredBits = 1, fieldType = "short")
	public short assignedModeFlag;
	
	@MessageField(order = 17, requiredBits = 1, fieldType = "short")
	public short spare;
	
	// 0, 6, ... 84
	@MessageField(order = 18, requiredBits = 0, fieldType = "string")
	public String nameOfAidsToNavigationExtension;
	
	// 0, 2, 4, 6
	@MessageField(order = 19, requiredBits = 0, fieldType = "bits")
	public List<Byte> spare2 = new PrintableList<Byte>();
}
