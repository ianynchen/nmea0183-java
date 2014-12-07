package com.antu.nmea.message.field.codec;

import java.lang.reflect.Field;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.antu.nmea.annotation.FieldSetting;
import com.antu.nmea.util.Dimension;

public class DimensionMessageFieldCodec extends AbstractMessageFieldCodec {
	
	static private Log logger = LogFactory.getLog(DimensionMessageFieldCodec.class);

	public DimensionMessageFieldCodec() {
	}

	@Override
	public String fieldCodecType() {
		return "dimension";
	}

	@Override
	protected boolean doDecode(List<Byte> bits, int startIndex, Object obj,
			Field field, FieldSetting setting) {
		
		Dimension dim = new Dimension();
		
		Short value = MessageFieldCodecHelper.parseShort(bits, startIndex, 9, false);
		if (value != null) {
			dim.a = value;
		} else
			return false;
		
		value = MessageFieldCodecHelper.parseShort(bits, startIndex + 9, 9, false);
		if (value != null) {
			dim.b = value;
		} else
			return false;
		
		value = MessageFieldCodecHelper.parseShort(bits, startIndex + 18, 6, false);
		if (value != null) {
			dim.c = value;
		} else
			return false;
		
		value = MessageFieldCodecHelper.parseShort(bits, startIndex + 24, 6, false);
		if (value != null) {
			dim.d = value;
		} else
			return false;

		try {
			field.set(obj, dim);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			logger.error("unable to set field: " + field.getName(), e);
			return false;
		}
		return true;
	}

	@Override
	protected boolean doEncode(List<Byte> bits, Object obj, Field field,
			FieldSetting setting) {
		
		try {
			Object val = field.get(obj);
			
			if (val instanceof Dimension) {
				Dimension dim = (Dimension)val;
				
				if (MessageFieldCodecHelper.shortToBits(bits, dim.a, 9, false)) {
					if (MessageFieldCodecHelper.shortToBits(bits, dim.b, 9, false)) {
						if (MessageFieldCodecHelper.shortToBits(bits, dim.c, 6, false)) {
							if (MessageFieldCodecHelper.shortToBits(bits, dim.d, 6, false)) {
								return true;
							}
						}
					}
				}
				return false;
			} else {
				logger.error("unable field is not of type Dimension: " + field.getName());
				return false;
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			logger.error("unable to get value from field: " + field.getName(), e);
			return false;
		}
	}

}
