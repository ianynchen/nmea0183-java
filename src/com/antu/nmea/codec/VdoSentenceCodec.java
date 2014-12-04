package com.antu.nmea.codec;

import java.util.Date;

import com.antu.nmea.sentence.INmeaSentence;
import com.antu.nmea.sentence.VdoSentence;

public class VdoSentenceCodec extends AisSentenceCodec {

	public VdoSentenceCodec() {
		super();
	}

	@Override
	public String getCodecType() {
		return "vdo";
	}

	@Override
	protected INmeaSentence createSentence(Date timestamp, String sentenceType) {
		return new VdoSentence(timestamp);
	}

}
