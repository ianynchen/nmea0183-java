package com.antu.nmea.codec;

import java.util.Date;

import com.antu.nmea.sentence.INmeaSentence;
import com.antu.nmea.sentence.VdmSentence;

public class VdmSentenceCodec extends AisSentenceCodec {

	public VdmSentenceCodec() {
		super();
	}

	@Override
	public String getCodecType() {
		return "vdm";
	}

	@Override
	protected INmeaSentence createSentence(Date timestamp, String sentenceType) {
		return new VdmSentence(timestamp);
	}

}
