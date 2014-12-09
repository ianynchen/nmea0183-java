package com.antu.nmea.message.field.codec;

import java.lang.reflect.Field;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.antu.nmea.annotation.FieldSetting;
import com.antu.nmea.util.DateTime;

public class DateTimeMessageFieldCodec extends AbstractMessageFieldCodec {
	
	static private Log logger = LogFactory.getLog(DateTimeMessageFieldCodec.class);

	public DateTimeMessageFieldCodec() {
	}

	@Override
	public String fieldCodecType() {
		return "dateTime";
	}

	@Override
	protected Integer doDecode(List<Byte> bits, int startIndex, Object obj,
			Field field, FieldSetting setting) {
		
		assert(setting.getFieldWidth() == 20);
		
		if (startIndex + 20 > bits.size())
			return null;
		
		DateTime time = new DateTime();
		
		Short value = MessageFieldCodecHelper.parseShort(bits, startIndex, 4, false);
		if (value != null) {
			time.month = value;
		} else
			return null;
		
		value = MessageFieldCodecHelper.parseShort(bits, startIndex + 4, 5, false);
		if (value != null) {
			time.day = value;
		} else
			return null;
		
		value = MessageFieldCodecHelper.parseShort(bits, startIndex + 9, 5, false);
		if (value != null) {
			time.hour = value;
		} else
			return null;
		
		value = MessageFieldCodecHelper.parseShort(bits, startIndex + 14, 6, false);
		if (value != null) {
			time.minute = value;
		} else
			return null;
		
		try {
			field.set(obj, time);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			logger.error("unable to set field: " + field.getName(), e);
			return null;
		}
		
		return 20;
	}

	@Override
	protected boolean doEncode(List<Byte> bits, Object obj, Field field,
			FieldSetting setting) {
		
		try {
			Object val = field.get(obj);
			
			if (val instanceof DateTime) {
				DateTime time = (DateTime)val;
				
				if (MessageFieldCodecHelper.shortToBits(bits, time.month, 4, false)) {
					if (MessageFieldCodecHelper.shortToBits(bits, time.day, 5, false)) {
						if (MessageFieldCodecHelper.shortToBits(bits, time.hour, 5, false)) {
							if (MessageFieldCodecHelper.shortToBits(bits, time.minute, 6, false)) {
								return true;
							}
						}
					}
				}
				return false;
			} else {
				logger.error("unable field is not of type DateTime: " + field.getName());
				return false;
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			logger.error("unable to get value from field: " + field.getName(), e);
			return false;
		}
	}

}
