package com.antu.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GenericFactory<E> {
	
	private List<String> registeredPackages;
	private HashMap<String, String> symbolsToClassNameMapping;
	@SuppressWarnings("rawtypes")
	private HashMap<String, Class> initializedClasses;
	private HashMap<String, E> initializedObjects;
	
	private String pattern;
	
	static private Log logger = LogFactory.getLog(GenericFactory.class);

	@SuppressWarnings("rawtypes")
	public GenericFactory(String pattern) {
		
		this.registeredPackages = new ArrayList<String>();
		
		this.pattern = pattern;
		this.initializedClasses = new HashMap<String, Class>();
		this.initializedObjects = new HashMap<String, E>();
		this.symbolsToClassNameMapping = new HashMap<String, String>();
	}

	@SuppressWarnings("rawtypes")
	public GenericFactory(String defaultPackage, String pattern) {
		
		this.registeredPackages = new ArrayList<String>();
		this.registeredPackages.add(defaultPackage);
		this.pattern = pattern;
		this.initializedClasses = new HashMap<String, Class>();
		this.initializedObjects = new HashMap<String, E>();
		this.symbolsToClassNameMapping = new HashMap<String, String>();
	}
	
	public boolean isPackageRegistered(String packageName) {
		return this.registeredPackages.contains(packageName);
	}
	
	public void registerPackage(String packageName) {
		
		if (!this.registeredPackages.contains(packageName)) {
			this.registeredPackages.add(packageName);
		}
	}
	
	/**
	 * Finds a class from full class name.
	 * @param className
	 * @return returns null if class not found.
	 */
	protected Class<?> findClass(String className) {
		
		assert(className != null && !className.isEmpty());
		
		if (this.initializedClasses.containsKey(className)) {
			return this.initializedClasses.get(className);
		}
		
		try {
			GenericFactory.logger.debug("class found: " + className);
			Class<?> clz = Class.forName(className);
			this.initializedClasses.put(className, clz);
			return clz;
		} catch (ClassNotFoundException e) {
			GenericFactory.logger.fatal("class: " + className + " cannot be found", e);
			return null;
		}
	}
	
	/**
	 * Finds a class from symbol name
	 * @param symbol
	 * @return
	 */
	protected Class<?> findClassBySymbol(String symbol) {
		
		assert(symbol != null && !symbol.isEmpty());
		String actualName = this.pattern.replace("?", symbol);
		
		for (String packageName : this.registeredPackages) {

			String className = packageName + "." + actualName;
			Class<?> clz = this.findClass(className);
			if (clz != null) {
				
				this.symbolsToClassNameMapping.put(symbol, className);
				this.initializedClasses.put(className, clz);
				return clz;
			}
		}
		return null;
	}
	
	/**
	 * Retrieves an object by full class name, if already created,
	 * use what is cached. Otherwise, creates a new object.
	 * @param className
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings("unchecked")
	public E getByFullName(String className) 
			throws InstantiationException, IllegalAccessException {
		
		if (this.initializedObjects.containsKey(className)) {
			GenericFactory.logger.info("cached object found");
			return this.initializedObjects.get(className);
		} else if (this.initializedClasses.containsKey(className)) {
			E obj = (E)this.initializedClasses.get(className).newInstance();
			this.initializedObjects.put(className, obj);
			GenericFactory.logger.info("object not in cache, initialized");
			return obj;
		} else {
			Class<?> clz = this.findClass(className);
			
			if (clz != null) {
				this.initializedClasses.put(className, clz);
				E obj = (E)this.initializedClasses.get(className).newInstance();
				this.initializedObjects.put(className, obj);
				return obj;
			}
		}
		return null;
	}
	
	public E getBySymbol(String symbol) 
			throws InstantiationException, IllegalAccessException {
		
		if (this.symbolsToClassNameMapping.containsKey(symbol)) {
			return this.getByFullName(this.symbolsToClassNameMapping.get(symbol));
		} else {

			Class<?> clz = this.findClassBySymbol(symbol);
			if (clz != null) {
				this.symbolsToClassNameMapping.put(symbol, clz.getName());
				E obj = this.getByFullName(clz.getName());
				return obj;
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public E createBySymbol(String symbol) 
			throws InstantiationException, IllegalAccessException {
		Class<?> clz = this.findClassBySymbol(symbol);
		
		return (E)clz.newInstance();
	}
	
	@SuppressWarnings("unchecked")
	public E createByFullName(String className) 
			throws InstantiationException, IllegalAccessException {
		Class<?> clz = this.findClass(className);
		return (E)clz.newInstance();
	}
}
