package com.antu.nmea.sentence.ais;

import java.util.List;

import com.antu.nmea.annotation.MessageField;
import com.antu.nmea.annotation.SentenceMessage;
import com.antu.nmea.util.DataLink;
import com.antu.util.PrintableList;

@SentenceMessage(alignBytes = true)
public class AisMessage20 extends AbstractAisMessage {

	public AisMessage20() {
		this.messageId = 20;
	}
	
	@MessageField(order = 4, fieldType = "short", requiredBits = 2)
	public short spare;
	
	@MessageField(order = 5, fieldType = "list", isGroup = true, groupItemClass = "com.antu.nmea.util.DataLink")
	public List<DataLink> dataLink;
	
	@MessageField(order = 6, fieldType = "bits")
	public List<Byte> spare2 = new PrintableList<Byte>();
}
