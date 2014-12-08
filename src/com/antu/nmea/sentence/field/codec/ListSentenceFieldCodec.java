package com.antu.nmea.sentence.field.codec;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.antu.nmea.annotation.FieldSetting;
import com.antu.nmea.annotation.GroupItem;
import com.antu.nmea.annotation.GroupItemAnnotationSorter;
import com.antu.nmea.annotation.SentenceField;
import com.antu.nmea.util.StringHelper;
import com.antu.util.GenericFactory;
import com.antu.util.PrintableList;

public class ListSentenceFieldCodec extends AbstractSentenceFieldCodec
		implements IGroupSentenceFieldCodec {
	
	private GenericFactory<Object> groupItemFactory;
	
	private GenericFactory<AbstractSentenceFieldCodec> codecFactory;

	static private Log logger = LogFactory.getLog(ListSentenceFieldCodec.class);

	public ListSentenceFieldCodec() {
		super();
		this.groupItemFactory = new GenericFactory<Object>("");
		this.codecFactory = new GenericFactory<AbstractSentenceFieldCodec>("com.antu.nmea.sentence.field.codec", "?SentenceFieldCodec");
	}

	@Override
	public String fieldCodecType() {
		return "list";
	}

	@Override
	public int requiredSegments() {
		return 0;
	}

	@Override
	public Object createGroupItem(String fullName) {
		
		try {
			return this.groupItemFactory.createByFullName(fullName);
		} catch (InstantiationException | IllegalAccessException e) {
			ListSentenceFieldCodec.logger.error("unable to init object: " + fullName, e);
			return null;
		}
	}
	
	protected List<Field> getGroupItems(Object item) {
		
		List<Field> result = new ArrayList<Field>();
		
		for (Field field : item.getClass().getFields()) {
			
			if (field.isAnnotationPresent(GroupItem.class)) {
				result.add(field);
			}
		}

		Collections.sort(result, new GroupItemAnnotationSorter());
		return result;
	}
	
	/**
	 * Works out number of segments required for 1 item in the group.
	 * @param item
	 * @return
	 */
	@Override
	public int segmentsPerItem(Object item) {
		
		List<Field> fields = this.getGroupItems(item);
		
		int segments = 0;
		for (Field field : fields) {
			GroupItem annotation = field.getAnnotation(GroupItem.class);
			segments += annotation.fieldWidth();
		}
		
		return segments;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected boolean decodeItem(PrintableList listItem, String[] segments, 
			int startIndex, String groupItemClass) {
		
		Object groupItem = this.createGroupItem(groupItemClass);
		
		if (groupItem == null)
			return false;
		
		List<Field> fields = this.getGroupItems(groupItem);
		
		int index = startIndex;
		for (Field field : fields) {
			GroupItem annotation = field.getAnnotation(GroupItem.class);
			try {
				AbstractSentenceFieldCodec codec = this.codecFactory.getBySymbol(annotation.itemType());
				
				if (codec.decode(segments, groupItem, field, index)) {
					index += annotation.fieldWidth();
				} else {
					ListSentenceFieldCodec.logger.error("unable to decode group item: " + field.getName());
					return false;
				}
			} catch (InstantiationException | IllegalAccessException e) {
				ListSentenceFieldCodec.logger.error("cannot create codec for group item: " + annotation.itemType());
				return false;
			}
		}
		
		listItem.add(groupItem);
		return true;
	}

	@Override
	public int totalItems(SentenceField annotation, int startIndex,
			int segmentsLength, int segmentsPerItem) {
		
		int totalSegmentsAvailable = segmentsLength - startIndex - annotation.reservedSegments();
		
		return totalSegmentsAvailable / segmentsPerItem;
	}

	@Override
	protected boolean doDecode(String[] segments, Object obj, Field field,
			FieldSetting setting, int startIndex) {

		SentenceField annotation = field.getAnnotation(SentenceField.class);
		
		@SuppressWarnings("rawtypes")
		PrintableList result = new PrintableList();
		
		int reservedSegmentsForOtherFields = annotation.reservedSegments();
		int totalFieldForGroup = segments.length - startIndex - reservedSegmentsForOtherFields;
		Object item = this.createGroupItem(annotation.groupItemClass());
		
		int fieldsForOneItem = this.segmentsPerItem(item);
		int totalItems = totalFieldForGroup / fieldsForOneItem;
		int remainder = totalFieldForGroup % fieldsForOneItem;
		
		if (remainder != 0) {
			ListSentenceFieldCodec.logger.error("cannot use all fields");
			return false;
		}
		
		if (item != null) {
			for (int i = 0; i < totalItems; i++) {
				if (!this.decodeItem(result, segments, 
						startIndex + i * fieldsForOneItem, annotation.groupItemClass())) {
					ListSentenceFieldCodec.logger.error("unable to decode group item.");
					return false;
				}
			}
		}
		
		return false;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected boolean doEncode(StringBuilder builder, Object obj, Field field,
			FieldSetting setting) {
		
		try {
			List items = (List)field.get(obj);
			
			for (Object item : items) {
				
				List<Field> fields = this.getGroupItems(item);
				
				for (Field itemField : fields) {
					
					GroupItem itemAnnotation = itemField.getAnnotation(GroupItem.class);
					
					AbstractSentenceFieldCodec codec = codecFactory.getBySymbol(StringHelper.capitalizeFirstChar(itemAnnotation.itemType()));
					
					if (codec != null) {
						codec.encode(builder, item, itemField);
					} else {
						ListSentenceFieldCodec.logger.error("unable to retrieve codec for item: " + itemAnnotation.itemType());
						return false;
					}
				}
			}
			
		} catch (IllegalArgumentException | IllegalAccessException | InstantiationException e) {
			ListSentenceFieldCodec.logger.error("unable to retrieve list object", e);
			return false;
		}
		return false;
	}

}
