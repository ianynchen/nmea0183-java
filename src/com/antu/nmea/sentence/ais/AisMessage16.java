package com.antu.nmea.sentence.ais;

import java.util.List;

import com.antu.nmea.annotation.MessageField;
import com.antu.nmea.util.TransmissionSchedule;
import com.antu.util.PrintableList;

public class AisMessage16 extends AbstractAisMessage {

	public AisMessage16() {
		this.messageId = 16;
	}

	@MessageField(order = 4, requiredBits = 2, fieldType = "short")
	public short spare;
	
	@MessageField(order = 5, isGroup = true, fieldType = "list", groupItemClass = "com.antu.nmea.util.TransmissionSchedule")
	public List<TransmissionSchedule> schedules;
	
	@MessageField(order = 6, fieldType = "bits")
	public List<Byte> spare2 = new PrintableList<Byte>();
}
