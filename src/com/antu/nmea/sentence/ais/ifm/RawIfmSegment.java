package com.antu.nmea.sentence.ais.ifm;

import java.util.List;

import com.antu.util.PrintableList;

public class RawIfmSegment extends AbstractIfmSegment {

	public RawIfmSegment() {
	}

	public List<Byte> dataBits = new PrintableList<Byte>();
}
