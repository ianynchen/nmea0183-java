package com.antu.nmea.sentence.field.codec;

import java.util.HashMap;

import com.antu.nmea.codec.exception.SentenceFieldCodecNotFoundException;
import com.antu.nmea.util.StringHelper;

/**
 * 
 * @author yining
 *
 * By default, all sentence field codecs should locate under com.antu.nmea.sentence.field.codec package,
 * and have the name XXXSentenceFieldCodec, where XXX is the fieldType field you specify in SentenceField,
 * with the first letter in upper case.
 * 
 * If you wish to use a none standard sentence field codec, just add it. But then you need to do it every
 * time in your code.
 */
public class SentenceFieldCodecManager {
	
	private HashMap<String, ISentenceFieldCodec> codecs = new HashMap<String, ISentenceFieldCodec>();
	
	private static SentenceFieldCodecManager Manager = new SentenceFieldCodecManager();

	private SentenceFieldCodecManager() {
		
	}
	
	public static SentenceFieldCodecManager instance() {
		return Manager;
	}
	
	public void addCodec(ISentenceFieldCodec codec) {
		if (codec == null)
			return;
		
		this.codecs.put(codec.fieldCodecType(), codec);
	}
	
	public void removeCodec(ISentenceFieldCodec codec) {
		if (codec == null || !this.codecs.containsKey(codec.fieldCodecType()))
			return;
		
		this.codecs.remove(codec.fieldCodecType());
	}
	
	public ISentenceFieldCodec getCodec(String type) throws SentenceFieldCodecNotFoundException {
		if (this.codecs.containsKey(type))
			return this.codecs.get(type);
		else {

			String codecName = StringHelper.capitalizeFirstChar(type);
			String className = this.getClass().getName();
			int index = className.indexOf(this.getClass().getSimpleName());

			String name = "";
			if (index >= 0) {
				StringBuilder sb = new StringBuilder(this.getClass().getPackage().getName());
				sb.append('.').append(codecName).append("SentenceFieldCodec");
				name = sb.toString();
			} else {
				StringBuilder sb = new StringBuilder("com.antu.nmea.sentence.field.codec.");
				sb.append(codecName).append("SentenceFieldCodec");
				name = sb.toString();
			}
			
			try {
				Class<?> cls = Class.forName(name);
				ISentenceFieldCodec codec = (ISentenceFieldCodec) cls.newInstance();
				this.codecs.put(codec.fieldCodecType(), codec);
				return codec;
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
				throw new SentenceFieldCodecNotFoundException(e);
			}
		}
	}
}
