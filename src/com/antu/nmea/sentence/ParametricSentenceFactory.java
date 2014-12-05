package com.antu.nmea.sentence;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import com.antu.nmea.util.StringHelper;

public class ParametricSentenceFactory {

	private HashMap<String, Class<ParametricSentence>> sentences = new HashMap<String, Class<ParametricSentence>>();
	
	private ParametricSentence createSentence(java.util.Date timestamp, Class<ParametricSentence> clz) {
		
		try {
			Constructor<ParametricSentence> constructor = clz.getConstructor(java.util.Date.class);
			
			Object obj = constructor.newInstance(timestamp);

			if (obj instanceof ParametricSentence) {
				return (ParametricSentence)obj;
			} else {
				return null;
			}
		} catch (NoSuchMethodException | SecurityException | 
				IllegalArgumentException | InvocationTargetException | 
				InstantiationException | IllegalAccessException e) {
			return null;
		}
	}
	
	public ParametricSentence createSentence(java.util.Date timestamp, String sentenceType) {
		if (sentences.containsKey(sentenceType)) {
			return this.createSentence(timestamp, sentences.get(sentenceType));
		} else {
			
			StringBuilder sb = new StringBuilder(this.getClass().getPackage().getName());
			sb.append('.').append(StringHelper.capitalizeFirstChar(sentenceType)).append("Sentence");
			
			try {
				@SuppressWarnings("unchecked")
				Class<ParametricSentence> clz = (Class<ParametricSentence>) Class.forName(sb.toString());
				sentences.put(sentenceType, clz);

				return this.createSentence(timestamp, clz);
			} catch (ClassNotFoundException e) {
				return null;
			}
		}
	}
}
