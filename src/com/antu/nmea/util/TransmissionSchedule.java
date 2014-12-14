package com.antu.nmea.util;

import com.antu.nmea.annotation.GroupItem;

public class TransmissionSchedule {

	public TransmissionSchedule() {
	}

	@GroupItem(order = 1, fieldWidth = 30, itemType = "integer")
	public int destinationId;

	@GroupItem(order = 2, fieldWidth = 12, itemType = "short")
	public short offSet1;
	
	@GroupItem(order = 3, fieldWidth = 10, itemType = "short")
	public short increment1;
}
