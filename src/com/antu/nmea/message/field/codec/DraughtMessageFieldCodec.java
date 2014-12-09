package com.antu.nmea.message.field.codec;

import java.lang.reflect.Field;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.antu.nmea.annotation.FieldSetting;

public class DraughtMessageFieldCodec extends AbstractMessageFieldCodec {

	static private Log logger = LogFactory.getLog(DraughtMessageFieldCodec.class);

	public DraughtMessageFieldCodec() {
	}

	@Override
	public String fieldCodecType() {
		return "draught";
	}

	@Override
	protected Integer doDecode(List<Byte> bits, int startIndex, Object obj,
			Field field, FieldSetting setting) {
		
		if (bits.size() - startIndex < setting.getFieldWidth())
			return null;
		
		Short value = MessageFieldCodecHelper.parseShort(bits, startIndex, setting.getFieldWidth(), false);
		
		if (value != null) {
			try {
				field.set(obj, value / 10.0);
				return setting.getFieldWidth();
			} catch (IllegalArgumentException | IllegalAccessException e) {
				logger.error("unable to set field: " + field.getName(), e);
				return null;
			}
		}
		return null;
	}

	@Override
	protected boolean doEncode(List<Byte> bits, Object obj, Field field,
			FieldSetting setting) {
		
		try {
			Double val = field.getDouble(obj);
			
			if (val != null) {
				int draught = (int)(val * 10);
				
				if (MessageFieldCodecHelper.integerToBits(bits, draught, setting.getFieldWidth(), false)) {
					return true;
				} else {
					return false;
				}
			} else {
				logger.error("unable to get value for field: " + field.getName());
				return false;
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			logger.error("unable to get value from field: " + field.getName(), e);
			return false;
		}
	}

}
