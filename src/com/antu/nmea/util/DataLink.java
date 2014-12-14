package com.antu.nmea.util;

import com.antu.nmea.annotation.GroupItem;

public class DataLink {

	public DataLink() {
	}

	@GroupItem(order = 1, fieldWidth = 12, itemType = "short")
	public short offsetNumber;
	
	@GroupItem(order = 2, fieldWidth = 4, itemType = "short")
	public short numberOfSlots;
	
	@GroupItem(order = 3, fieldWidth = 3, itemType = "short")
	public short timeout;
	
	@GroupItem(order = 4, fieldWidth = 11, itemType = "short")
	public short increment;
}
