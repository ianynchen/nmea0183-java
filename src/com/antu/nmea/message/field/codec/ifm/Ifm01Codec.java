package com.antu.nmea.message.field.codec.ifm;

import java.util.List;

import com.antu.nmea.message.field.codec.MessageFieldCodecHelper;
import com.antu.nmea.sentence.ais.ifm.AbstractIfmSegment;
import com.antu.nmea.sentence.ais.ifm.IfmMessage01;

public class Ifm01Codec extends AbstractIfmCodec {

	public Ifm01Codec() {
	}

	@Override
	public Integer decode(AbstractIfmSegment segment, List<Byte> bits,
			int startIndex) {
		
		assert(segment instanceof IfmMessage01);
		
		IfmMessage01 msg = (IfmMessage01)segment;
		msg.message = MessageFieldCodecHelper.parseString(bits, startIndex, bits.size() - startIndex, false);
		
		if (msg.message == null)
			return null;
		return bits.size() - startIndex;
	}

	@Override
	public boolean encode(AbstractIfmSegment segment, List<Byte> bits) {
		
		assert(segment instanceof IfmMessage01);
		
		IfmMessage01 msg = (IfmMessage01)segment;
		assert(msg.message != null);
		
		if (MessageFieldCodecHelper.stringToBits(bits, msg.message, msg.message.length())) {
			return true;
		}
		return false;
	}

}
