package com.antu.nmea.sentence;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import com.antu.nmea.sentence.exception.SentenceCreationException;
import com.antu.nmea.util.StringHelper;

public class GenericSentenceFactory {

	private HashMap<String, Class<INmeaSentence>> sentenceClasses = new HashMap<String, Class<INmeaSentence>>();
	
	private INmeaSentence createSentence(java.util.Date timestamp, Class<INmeaSentence> clz) 
			throws SentenceCreationException {
		
		try {
			Constructor<INmeaSentence> constructor = clz.getConstructor(java.util.Date.class);
			
			INmeaSentence obj = constructor.newInstance(timestamp);
			
			return obj;
		} catch (NoSuchMethodException | SecurityException | 
				IllegalArgumentException | InvocationTargetException | 
				InstantiationException | IllegalAccessException |
				NullPointerException e) {
			throw new SentenceCreationException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public INmeaSentence createSentence(java.util.Date timestamp, String sentenceType) 
			throws SentenceCreationException {
		if (sentenceClasses.containsKey(sentenceType)) {
			return this.createSentence(timestamp, sentenceClasses.get(sentenceType));
		} else {
			
			StringBuilder sb = new StringBuilder(this.getClass().getPackage().getName());
			sb.append('.').append(StringHelper.capitalizeFirstChar(sentenceType)).append("Sentence");
			
			try {
				Class<INmeaSentence> clz = (Class<INmeaSentence>) Class.forName(sb.toString());
				sentenceClasses.put(sentenceType, clz);

				return this.createSentence(timestamp, clz);
			} catch (ClassNotFoundException e) {
				throw new SentenceCreationException(e);
			}
		}
	}
	
	static public GenericSentenceFactory Instance = new GenericSentenceFactory();
}
