package com.antu.nmea.message.field.codec;

import java.lang.reflect.Field;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.antu.nmea.annotation.FieldSetting;

public class ShortLatitudeMessageFieldCodec extends AbstractMessageFieldCodec {
	
	static private Log logger = LogFactory.getLog(ShortLatitudeMessageFieldCodec.class); 

	public ShortLatitudeMessageFieldCodec() {
	}

	@Override
	public String fieldCodecType() {
		return "shortLatitude";
	}

	@Override
	protected Integer doDecode(List<Byte> bits, int startIndex, Object obj,
			Field field, FieldSetting setting) {
		
		assert (setting.getFieldWidth() == 17);
		
		Short val = MessageFieldCodecHelper.parseShort(bits, startIndex, setting.getFieldWidth(), true);
		
		if (val != null) {
			
			try {
				field.setDouble(obj, val / 60.0 / 10);
				return setting.getFieldWidth();
			} catch (IllegalArgumentException | IllegalAccessException e) {
				ShortLatitudeMessageFieldCodec.logger.error("error parsing value", e);
			}
		}
		
		return null;
	}

	@Override
	protected boolean doEncode(List<Byte> bits, Object obj, Field field,
			FieldSetting setting) {
		
		try {
			Double val = field.getDouble(obj);
			
			if (val == null)
				val = (double) 91;
			
			return MessageFieldCodecHelper.shortToBits(bits, (short) (val * 60 * 10), setting.getFieldWidth(), true);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			ShortLatitudeMessageFieldCodec.logger.error("error encode value", e);
			return false;
		}
	}

}
