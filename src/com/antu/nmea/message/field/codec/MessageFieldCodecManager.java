package com.antu.nmea.message.field.codec;

import java.util.HashMap;

import com.antu.nmea.codec.exception.MessageFieldCodecNotFoundException;
import com.antu.nmea.util.StringHelper;

public class MessageFieldCodecManager {
	
	private HashMap<String, IMessageFieldCodec> codecs = new HashMap<String, IMessageFieldCodec>();
	
	private static MessageFieldCodecManager Manager = new MessageFieldCodecManager();

	private MessageFieldCodecManager() {
		
	}
	
	public static MessageFieldCodecManager instance() {
		return Manager;
	}
	
	public void addCodec(IMessageFieldCodec codec) {
		if (codec == null)
			return;
		
		this.codecs.put(codec.fieldCodecType(), codec);
	}
	
	public void removeCodec(IMessageFieldCodec codec) {
		if (codec == null || !this.codecs.containsKey(codec.fieldCodecType()))
			return;
		
		this.codecs.remove(codec.fieldCodecType());
	}
	
	public IMessageFieldCodec getCodec(String type) throws MessageFieldCodecNotFoundException {
		if (this.codecs.containsKey(type))
			return this.codecs.get(type);
		else {

			String codecName = StringHelper.capitalizeFirstChar(type);
			String className = this.getClass().getName();
			int index = className.indexOf(this.getClass().getSimpleName());

			String name = "";
			if (index >= 0) {
				StringBuilder sb = new StringBuilder(this.getClass().getPackage().getName());
				sb.append('.').append(codecName).append("MessageFieldCodec");
				name = sb.toString();
			} else {
				StringBuilder sb = new StringBuilder("com.antu.nmea.message.field.codec.");
				sb.append(codecName).append("MessageFieldCodec");
				name = sb.toString();
			}
			
			try {
				Class<?> cls = Class.forName(name);
				IMessageFieldCodec codec = (IMessageFieldCodec) cls.newInstance();
				this.codecs.put(codec.fieldCodecType(), codec);
				return codec;
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
				throw new MessageFieldCodecNotFoundException(e);
			}
		}
	}
}
