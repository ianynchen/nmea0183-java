package com.antu.nmea.util;

import java.util.List;

import com.antu.nmea.annotation.GroupItem;

public class DGNSSData {

	public DGNSSData() {
	}

	@GroupItem(order = 1, itemType = "bits", fieldWidth = 24)
	public List<Byte> data;
}
