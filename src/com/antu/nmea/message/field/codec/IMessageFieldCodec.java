package com.antu.nmea.message.field.codec;

import java.lang.reflect.Field;
import java.util.List;

import com.antu.nmea.annotation.MessageField;

public interface IMessageFieldCodec {

	String fieldCodecType();
	
	boolean decode(List<Byte> bits, int startIndex, Object obj, Field field);
	
	boolean encode(List<Byte> bits, Object obj, Field field, MessageField annotation);
}
