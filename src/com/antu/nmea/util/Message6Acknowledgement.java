package com.antu.nmea.util;

import com.antu.nmea.annotation.GroupItem;

public class Message6Acknowledgement {

	public Message6Acknowledgement() {
	}

	@GroupItem(order = 1, itemType = "integer", fieldWidth = 30)
	public int destinationId;
	
	@GroupItem(order = 2, itemType = "short", fieldWidth = 2)
	public short sequenceNumber;
}
