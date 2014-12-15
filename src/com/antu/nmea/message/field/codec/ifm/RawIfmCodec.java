package com.antu.nmea.message.field.codec.ifm;

import java.util.List;

import com.antu.nmea.sentence.ais.ifm.AbstractIfmSegment;
import com.antu.nmea.sentence.ais.ifm.RawIfmSegment;
import com.antu.util.PrintableList;

public class RawIfmCodec extends AbstractIfmCodec {

	public RawIfmCodec() {
	}

	@Override
	public Integer decode(AbstractIfmSegment segment, List<Byte> bits,
			int startIndex) {
		
		assert(segment instanceof RawIfmSegment);
		
		RawIfmSegment seg = (RawIfmSegment)segment;
		seg.dataBits = new PrintableList<Byte>();
		
		for (int i = startIndex; i < bits.size(); i++) {
			seg.dataBits.add(bits.get(i));
		}
		
		return bits.size() - startIndex;
	}

	@Override
	public boolean encode(AbstractIfmSegment segment, List<Byte> bits) {
		assert(segment instanceof RawIfmSegment);

		RawIfmSegment seg = (RawIfmSegment)segment;
		for (int i = 0; i < seg.dataBits.size(); i++) {
			bits.add(seg.dataBits.get(i));
		}
		return true;
	}

}
