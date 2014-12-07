package com.antu.nmea.sentence.ais;

import com.antu.nmea.annotation.MessageField;
import com.antu.nmea.util.DateTime;
import com.antu.nmea.util.Dimension;

public class AisMessage5 extends AbstractAisMessage {

	public AisMessage5() {
		this.messageId = 5;
	}

	@MessageField(order = 4, requiredBits = 2, fieldType = "short")
	public short aisVersionIndicator;
	
	@MessageField(order = 5, requiredBits = 30, fieldType = "integer")
	public int imoNumber;
	
	@MessageField(order = 6, requiredBits = 42, fieldType = "string")
	public String callSign = "";
	
	@MessageField(order = 7, requiredBits = 120, fieldType = "string")
	public String name = "";
	
	@MessageField(order = 8, requiredBits = 8, fieldType = "short")
	public short typeOfShipAndCargoType;
	
	@MessageField(order = 9, requiredBits = 30, fieldType = "dimension")
	public Dimension overallDimension;
	
	@MessageField(order = 10, requiredBits = 4, fieldType = "short")
	public short typeOfElectronicPostionFixingDevice;
	
	@MessageField(order = 11, requiredBits = 20, fieldType = "dateTime")
	public DateTime eta;
	
	@MessageField(order = 12, requiredBits = 8, fieldType = "draught")
	public double maximumPresentStaticDraught;
	
	@MessageField(order = 13, requiredBits = 120, fieldType = "string")
	public String destination = "";
	
	@MessageField(order = 14, requiredBits = 1, fieldType = "short")
	public short dte = 1;
	
	@MessageField(order = 15, requiredBits = 1, fieldType = "short")
	public short spare;
}
