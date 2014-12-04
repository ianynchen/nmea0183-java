package com.antu.nmea.sentence.field.codec;

import com.antu.nmea.annotation.SentenceField;

/**
 * 
 * @author yining
 *
 */
public interface IGroupSentenceFieldCodec extends ISentenceFieldCodec {
	
	Object createGroupItem(String fullName);
	
	int segmentsPerItem(Object groupItem);
	
	int totalItems(SentenceField annotation, int startIndex, int segmentsLength, int segmentsPerItem);
}
