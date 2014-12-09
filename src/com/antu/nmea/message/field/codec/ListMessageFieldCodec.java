package com.antu.nmea.message.field.codec;

import java.lang.reflect.Field;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.antu.nmea.annotation.FieldSetting;
import com.antu.nmea.annotation.GroupItem;
import com.antu.nmea.annotation.MessageField;
import com.antu.nmea.codec.AbstractNmeaSentenceCodec;
import com.antu.util.GenericFactory;
import com.antu.util.PrintableList;

public class ListMessageFieldCodec extends AbstractMessageFieldCodec {
	
	static private Log logger = LogFactory.getLog(ListMessageFieldCodec.class);
	
	private GenericFactory<Object> groupItemFactory = new GenericFactory<Object>("", "");
	private GenericFactory<IMessageFieldCodec> messageFieldCodecFactory = 
			new GenericFactory<IMessageFieldCodec>("com.antu.nmea.message.field.codec", "?MessageFieldCodec");

	public ListMessageFieldCodec() {
	}

	@Override
	public String fieldCodecType() {
		return "list";
	}
	
	protected int bitsAfterGroupItem(Object obj, Field groupItem) {
		
		int bits = 0;
		List<Field> fields = AbstractNmeaSentenceCodec.getMessageFields(obj);
		
		boolean found = false;
		for (int i = 0; i < fields.size(); i++) {
			MessageField annotation = fields.get(i).getAnnotation(MessageField.class);
			
			if (fields.get(i).getName().equals(groupItem.getName())) {
				if (annotation.isGroup()) {
					found = true;
				}
			} else if (found) {
				bits += annotation.requiredBits();
			}
		}
		return bits;
	}
	
	protected int bitsPerGroupItem(Object groupItem) {
		int bits = 0;
		
		List<Field> fields = AbstractNmeaSentenceCodec.getGroupItems(groupItem);
		
		for (Field field : fields) {
			GroupItem annotation = field.getAnnotation(GroupItem.class);
			bits += annotation.fieldWidth();
		}
		return bits;
	}
	
	protected boolean decodeItem(List<Byte> bits, int startIndex, Object groupItem) {
		
		List<Field> fields = AbstractNmeaSentenceCodec.getGroupItems(groupItem);
		
		for (Field field : fields) {
			GroupItem annotation = field.getAnnotation(GroupItem.class);

			try {
				IMessageFieldCodec codec = this.messageFieldCodecFactory.createBySymbol(annotation.itemType());
				
				if (codec != null) {
					if (codec.decode(bits, startIndex, groupItem, field) == null) {
						logger.error("error decoding group item field: " + field.getName());
						return false;
					}
				} else {
					logger.error("no codec for group item field: " + field.getName());
					return false;
				}
			} catch (InstantiationException | IllegalAccessException e) {
				logger.error("error decoding item", e);
				return false;
			}
		}
		return true;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Integer doDecode(List<Byte> bits, int startIndex, Object obj,
			Field field, FieldSetting setting) {
		
		int bitsAfterGroupItem = this.bitsAfterGroupItem(obj, field);
		try {
			List list = new PrintableList();
			Object groupItem = this.groupItemFactory.createByFullName(setting.groupItemClass());
			
			int bitsAvailable = bits.size() - startIndex - bitsAfterGroupItem;
			int bitsPerItem = this.bitsPerGroupItem(groupItem);
			
			if (bitsAvailable % bitsPerItem != 0) {
				logger.error("cannot for integral items for group.");
				return null;
			}
			
			int items = bitsAvailable / bitsPerItem;
			for (int i = 0; i < items; i++) {
				
				if (!decodeItem(bits, startIndex + i * bitsPerItem, groupItem)) {
					return null;
				}
				list.add(groupItem);
				groupItem = this.groupItemFactory.createByFullName(setting.groupItemClass());
			}
			
			field.set(obj, list);
			return bitsPerItem * items;
		} catch (InstantiationException | IllegalAccessException e) {
			logger.error("exception during decoding", e);
			return null;
		}
	}
	
	protected boolean encodeItem(List<Byte> bits, Object item) {
		
		List<Field> fields = AbstractNmeaSentenceCodec.getGroupItems(item);
		
		for (Field field : fields) {
			GroupItem annotation = field.getAnnotation(GroupItem.class);
			try {
				IMessageFieldCodec codec = this.messageFieldCodecFactory.createBySymbol(annotation.itemType());
				
				if (codec != null) {
					if (!codec.encode(bits, item, field)) {
						logger.error("error encoding group item field: " + field.getName());
						return false;
					}
				} else {
					logger.error("no codec for group item field: " + field.getName());
					return false;
				}
			} catch (InstantiationException | IllegalAccessException e) {
				logger.error("error encoding item", e);
				return false;
			}
		}
		return true;
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	protected boolean doEncode(List<Byte> bits, Object obj, Field field,
			FieldSetting setting) {
		
		try {
			Object valueObj = field.get(obj);
			
			if (valueObj == null || !(valueObj instanceof List)) {
				logger.error("error, value null or not of type list");
				return false;
			}
			
			List value = (List)valueObj;
			for (Object item : value) {
				if (!encodeItem(bits, item)) {
					return false;
				}
			}
			return true;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			logger.error("error during encoding", e);
		}
		return false;
	}

}
