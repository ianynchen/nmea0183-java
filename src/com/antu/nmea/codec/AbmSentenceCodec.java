package com.antu.nmea.codec;

import java.util.Date;

import com.antu.nmea.sentence.AbmSentence;
import com.antu.nmea.sentence.EncapsulationSentence;
import com.antu.nmea.sentence.IEncapsulatedSentence;
import com.antu.nmea.sentence.INmeaSentence;
import com.antu.nmea.sentence.ais.IEncapsulatedAisMessage;
import com.antu.util.GenericFactory;

public class AbmSentenceCodec extends EncapsulationSentenceCodec{
	
	private GenericFactory<IEncapsulatedAisMessage> factory;

	public AbmSentenceCodec() {
		
		factory = new GenericFactory<IEncapsulatedAisMessage>("com.antu.nmea.sentence.ais", "AisMessage?");
	}

	@Override
	protected IEncapsulatedSentence createEncapsulatedSentence(
			EncapsulationSentence sentence) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException {
		
		if (sentence instanceof AbmSentence) {
			return factory.createBySymbol(new Integer(((AbmSentence) sentence).messageId).toString());
		} else {
			return null;
		}
	}

	@Override
	protected int getMessageId(EncapsulationSentence sentence) {
		
		if (sentence instanceof AbmSentence) {
			return ((AbmSentence) sentence).messageId;
		} else {
			return 0;
		}
	}

	@Override
	public String getCodecType() {
		return "abm";
	}

	@Override
	protected INmeaSentence createSentence(Date timestamp, String sentenceType) {
		return new AbmSentence(timestamp);
	}

}
