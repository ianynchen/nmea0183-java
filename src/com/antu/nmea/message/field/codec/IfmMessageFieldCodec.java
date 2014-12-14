package com.antu.nmea.message.field.codec;

import java.lang.reflect.Field;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.antu.nmea.annotation.FieldSetting;
import com.antu.nmea.message.field.codec.ifm.AbstractIfmCodec;
import com.antu.nmea.message.field.codec.ifm.RawIfmCodec;
import com.antu.nmea.sentence.ais.ifm.AbstractIfmSegment;
import com.antu.nmea.sentence.ais.ifm.RawIfmSegment;
import com.antu.util.GenericFactory;

public class IfmMessageFieldCodec extends AbstractMessageFieldCodec {
	
	static private Log logger = LogFactory.getLog(IfmMessageFieldCodec.class);
	
	private GenericFactory<AbstractIfmSegment> segmentFactory = new GenericFactory<AbstractIfmSegment>(
			"com.antu.nmea.sentence.ais.ifm", "IfmMessage?");
	private GenericFactory<AbstractIfmCodec> ifmCodecFactory = new GenericFactory<AbstractIfmCodec>(
			"com.antu.nmea.message.field.codec.ifm", "Ifm?Codec");

	public IfmMessageFieldCodec() {
		super();
	}

	@Override
	public String fieldCodecType() {
		return "ifm";
	}

	@Override
	protected Integer doDecode(List<Byte> bits, int startIndex, Object obj,
			Field field, FieldSetting setting) {
		
		assert(bits.size() >= startIndex + 16);
		
		Short dac = MessageFieldCodecHelper.parseShort(bits, startIndex, 10, false);
		Short fi = MessageFieldCodecHelper.parseShort(bits, startIndex + 10, 6, false);
		
		if (dac != null && fi != null) {

			String combined = String.format("%d%d", dac, fi);
			
			try {
				AbstractIfmSegment segment = this.segmentFactory.createBySymbol(combined);
				AbstractIfmCodec codec = this.ifmCodecFactory.createBySymbol(combined);
				
				if (segment == null || codec == null) {
					logger.error("unable to create ifm object/codec: " + combined + ", object returned null");
					
					codec = new RawIfmCodec();
					segment = new RawIfmSegment();
				}
				
				segment.dac = dac;
				segment.functionIdentifier = fi;
				
				Integer size = codec.decode(segment, bits, startIndex + 16);
				if (size != null) {
					
					field.set(obj, segment);
					return size;
				}
			} catch (InstantiationException | IllegalAccessException e) {
				logger.error("error in decoding: " + combined, e);
			}
		} 
		return null;
	}

	@Override
	protected boolean doEncode(List<Byte> bits, Object obj, Field field,
			FieldSetting setting) {
		
		try {
			Object value = field.get(obj);
			
			if (value != null && value instanceof AbstractIfmSegment) {
				AbstractIfmSegment segment = (AbstractIfmSegment)value;
				
				String combined = String.format("%d%d", segment.dac, segment.functionIdentifier);
				AbstractIfmCodec codec = this.ifmCodecFactory.createBySymbol(combined);
				
				if (codec == null) {
					logger.error("unable to create ifm codec: " + combined + ", object returned null");
					
					if (segment instanceof RawIfmSegment) {
						
						codec = new RawIfmCodec();
					} else
						return false;
				}
				
				if (codec.encode(segment, bits)) {
					return true;
				}
			} else {
				if (value == null)
					logger.error("ifm part is null");
				else
					logger.error("value object is not AbstractIfmSegment: " + value.getClass().getName());
				
				return false;
			}
		} catch (IllegalArgumentException | IllegalAccessException | InstantiationException e) {
			logger.error("error in encoding", e);
		}
		return false;
	}

}
