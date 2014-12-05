package com.antu.nmea.codec;

import java.util.Date;

import com.antu.nmea.sentence.AbmSentence;
import com.antu.nmea.sentence.BbmSentence;
import com.antu.nmea.sentence.EncapsulationSentence;
import com.antu.nmea.sentence.IEncapsulatedSentence;
import com.antu.nmea.sentence.INmeaSentence;
import com.antu.nmea.sentence.ais.IEncapsulatedAisMessage;
import com.antu.util.GenericFactory;

public class BbmSentenceCodec extends EncapsulationSentenceCodec {
	
	private GenericFactory<IEncapsulatedAisMessage> factory;

	public BbmSentenceCodec() {
		
		factory = new GenericFactory<IEncapsulatedAisMessage>("com.antu.nmea.sentence.ais", "AisMessage?");
	}

	@Override
	protected IEncapsulatedSentence createEncapsulatedSentence(
			EncapsulationSentence sentence) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException {
		
		if (sentence instanceof BbmSentence) {
			return factory.createBySymbol(new Integer(((BbmSentence) sentence).messageId).toString());
		} else {
			return null;
		}
	}

	@Override
	protected int getMessageId(EncapsulationSentence sentence) {
		
		if (sentence instanceof BbmSentence) {
			return ((BbmSentence) sentence).messageId;
		} else {
			return 0;
		}
	}

	@Override
	public String getCodecType() {
		return "bbm";
	}

	@Override
	protected INmeaSentence createSentence(Date timestamp, String sentenceType) {
		return new AbmSentence(timestamp);
	}
}
