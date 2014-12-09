package com.antu.nmea.message.field.codec.ifm;

import java.util.List;

import com.antu.nmea.sentence.ais.ifm.AbstractIfmSegment;

public abstract class AbstractIfmCodec {

	public AbstractIfmCodec() {
	}

	abstract public Integer decode(AbstractIfmSegment segment, List<Byte> bits, int startIndex);
	
	abstract public boolean encode(AbstractIfmSegment segment, List<Byte> bits);
}
