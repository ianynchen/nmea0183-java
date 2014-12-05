package com.antu.util;

import java.util.ArrayList;
import java.util.List;

public class PrintableList<T> extends ArrayList<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4551918441044276598L;

	public PrintableList() {
		super();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("(");
		
		for (T t : this) {
			sb.append(t.toString()).append(",");
		}
		
		sb.append(")");
		return sb.toString();
	}
	
	public PrintableList<Byte> flip() {
		PrintableList<Byte> result = new PrintableList<Byte>();
		
		for (T elem : this) {
			if (elem.equals(0)) {
				result.add((byte) 1);
			} else {
				result.add((byte) 0);
			}
		}
		return result;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		
		if (obj == null || !(obj instanceof List))
			return false;
		
		List val = (List)obj;
		
		if (this.size() != val.size())
			return false;
		
		for (int i = 0; i < val.size(); i++) {
			
			if (!this.get(i).equals(val.get(i)))
				return false;
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		
		int hash = 0;
		
		for (T elem : this) {
			hash += elem.hashCode();
		}
		return hash;
	}
}
