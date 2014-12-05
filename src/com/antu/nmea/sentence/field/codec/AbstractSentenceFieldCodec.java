package com.antu.nmea.sentence.field.codec;

import java.lang.reflect.Field;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.antu.nmea.annotation.FieldSetting;
import com.antu.nmea.annotation.GroupItem;
import com.antu.nmea.annotation.SentenceField;

public abstract class AbstractSentenceFieldCodec implements ISentenceFieldCodec {
	
	static private Log logger = LogFactory.getLog(AbstractSentenceFieldCodec.class);
	
	@Override
	public boolean decode(String[] segments, Object obj, Field field, int startIndex) {
				
		if (segments.length < startIndex + this.requiredSegments())
			return false;
		
		if (field.isAnnotationPresent(SentenceField.class)) {
			SentenceField annotation = (SentenceField)field.getAnnotation(SentenceField.class);
			
			if (segments[startIndex].isEmpty()) {
				if (!annotation.isRequired()) {
					try {
						field.set(obj, null);
						return true;
					} catch (IllegalArgumentException |
							 IllegalAccessException e) {
						AbstractSentenceFieldCodec.logger.error("unable to set value for not required field: " + field.getName());
						return false;
					}
				}
				return false;
			}
			
			return this.doDecode(segments, obj, field, new FieldSetting(annotation), startIndex);
		} else if (field.isAnnotationPresent(GroupItem.class)) {
			GroupItem annotation = (GroupItem)field.getAnnotation(GroupItem.class);
		
			return this.doDecode(segments, obj, field, new FieldSetting(annotation), startIndex);
		}
		
		return false;
	}

	abstract protected boolean doDecode(String[] segments, Object obj, 
			Field field, FieldSetting setting,
			int startIndex);

	@Override
	public boolean encode(StringBuilder builder, Object obj, Field field) {
		
		if (field.isAnnotationPresent(SentenceField.class)) {
			return this.doEncode(builder, obj, field, new FieldSetting((SentenceField)field.getAnnotation(SentenceField.class)));
		} else if (field.isAnnotationPresent(GroupItem.class)) {
			return this.doEncode(builder, obj, field, new FieldSetting((GroupItem)field.getAnnotation(GroupItem.class)));
		}
		return false;
	}
	
	abstract protected boolean doEncode(StringBuilder builder,
			Object obj, Field field, FieldSetting setting);
	
	protected boolean isNeededSegmentsEmpty(String[] segments, int startIndex) {
		
		for (int i = startIndex; i < startIndex + this.requiredSegments(); i++) {
			
			if (segments[i].isEmpty()) {
				return true;
			}
		}
		return false;
	}
}
