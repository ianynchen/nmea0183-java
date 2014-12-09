package com.antu.nmea.message.field.codec;

import java.lang.reflect.Field;
import java.util.List;

import com.antu.nmea.annotation.FieldSetting;
import com.antu.nmea.annotation.GroupItem;
import com.antu.nmea.annotation.MessageField;

abstract public class AbstractMessageFieldCodec implements IMessageFieldCodec {

	public AbstractMessageFieldCodec() {
	}
	
	@Override
	public Integer decode(List<Byte> bits, int startIndex, Object obj, Field field) {
		
		FieldSetting setting = null;
		
		if (field.isAnnotationPresent(MessageField.class)) {
			setting = new FieldSetting((MessageField)field.getAnnotation(MessageField.class));
		} else if (field.isAnnotationPresent(GroupItem.class)) {
			setting = new FieldSetting((GroupItem)field.getAnnotation(GroupItem.class));
		}
		
		if (setting == null)
			return null;
		
		return this.doDecode(bits, startIndex, obj, field, setting);
	}
	
	abstract protected Integer doDecode(List<Byte> bits, int startIndex, Object obj, Field field, FieldSetting setting);

	@Override
	public boolean encode(List<Byte> bits, Object obj, Field field) {
		
		FieldSetting setting = null;
		
		if (field.isAnnotationPresent(MessageField.class)) {
			setting = new FieldSetting((MessageField)field.getAnnotation(MessageField.class));
		} else if (field.isAnnotationPresent(GroupItem.class)) {
			setting = new FieldSetting((GroupItem)field.getAnnotation(GroupItem.class));
		}
		
		if (setting == null)
			return false;
		
		return this.doEncode(bits, obj, field, setting);
	}

	abstract protected boolean doEncode(List<Byte> bits, Object obj, Field field, FieldSetting setting);
}
