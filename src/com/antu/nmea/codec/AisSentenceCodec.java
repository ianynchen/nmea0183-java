package com.antu.nmea.codec;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.antu.nmea.message.field.codec.MessageFieldCodecHelper;
import com.antu.nmea.sentence.EncapsulationSentence;
import com.antu.nmea.sentence.IEncapsulatedSentence;
import com.antu.nmea.sentence.ais.IEncapsulatedAisMessage;
import com.antu.util.GenericFactory;

abstract public class AisSentenceCodec extends EncapsulationSentenceCodec {
	
	private GenericFactory<IEncapsulatedAisMessage> factory;

	static private Log logger = LogFactory.getLog(AisSentenceCodec.class);

	public AisSentenceCodec() {
		super();
		
		factory = new GenericFactory<IEncapsulatedAisMessage>("com.antu.nmea.sentence.ais", "AisMessage?");
	}

	@Override
	protected IEncapsulatedSentence createEncapsulatedSentence(
			EncapsulationSentence sentence) 
					throws ClassNotFoundException, 
					InstantiationException, 
					IllegalAccessException {
		
		int id = this.getMessageId(sentence);
		
		IEncapsulatedSentence sent = null;
		AisSentenceCodec.logger.info("message id is: " + new Integer(id).toString());
		if (id > 0) {
			if (id == 24) {
				List<Byte> encapsulatedData = sentence.getRawData();
				
				if (encapsulatedData.size() >= 40) {
					Byte part = MessageFieldCodecHelper.parseByte(encapsulatedData, 38, 2, false);
					
					if (part != null) {
						if (part == 0)
							sent = factory.createBySymbol("24A");
						else if (part == 1)
							sent = factory.createBySymbol("24B");
					}
				}
			} else {
				sent = factory.createBySymbol(new Integer(id).toString());
			}
		}
		
		if (sent != null)
			AisSentenceCodec.logger.info("encapsulated sentence type: " + sent.getClass().getName());
		else
			AisSentenceCodec.logger.info("encapsulated sentence is null");
		return sent;
	}

	@Override
	protected int getMessageId(EncapsulationSentence sentence) {
		
		List<Byte> bits = sentence.getRawData();
		
		if (bits != null && bits.size() > 0) {
			Integer val = MessageFieldCodecHelper.parseInteger(bits, 0, 6, false);
			
			if (val != null)
				return val;
			return 0;
		}
		return 0;
	}

}
