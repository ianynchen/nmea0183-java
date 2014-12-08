package com.antu.nmea.sentence.ais;

import java.util.List;

import com.antu.nmea.annotation.MessageField;
import com.antu.nmea.util.Message6Acknowledgement;

public class AisMessage7 extends AbstractAisMessage {

	public AisMessage7() {
		this.messageId = 7;
	}

	@MessageField(order = 4, requiredBits = 2, fieldType = "short")
	public short spare;
	
	@MessageField(order = 5, isGroup = true, fieldType = "list", groupItemClass = "com.antu.nmea.util.Message6Acknowledgement", requiredBits = 0)
	public List<Message6Acknowledgement> acknowledgements;
}
