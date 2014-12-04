package com.antu.nmea.util;

import java.util.ArrayList;

public class Buffer {
	private final int BUFFER_SIZE = 500;
	private char[] buffer;
	private int capacity;
	private int length;
	
	public Buffer() {
		buffer = new char[BUFFER_SIZE];
		capacity = BUFFER_SIZE;
		length = 0;
	}
	
	public Buffer(int size) {
		buffer = new char[size];
		this.capacity = size;
		length = 0;
	}
	
	static public void copy(char[] from, int fromIndex, int length, char[] to, int startIndex) {
		for (int i = fromIndex, j = startIndex; i < length && j < to.length; i++, j++) {
			to[j] = from[i];
		}
	}
	
	public ArrayList<String> append(char[] content) {
		
		char[] newContent = new char[this.length + content.length];

		Buffer.copy(this.buffer, 0, this.length, newContent, 0);
		Buffer.copy(content, 0, content.length, newContent, this.length);
		
		int begin = -1, end = -1, searchIndex = 0;
		
		ArrayList<String> result = new ArrayList<String>();
		do {
			begin = this.findBeginMark(newContent, searchIndex);
			
			if (begin >= searchIndex) {
				end = this.findEndMark(newContent, begin + 1);
			}
			
			if (begin >= searchIndex && end > begin) {
				String sentence = new String(newContent, begin, end - begin);
				result.add(sentence);
				searchIndex = end;
			}
		} while (begin >= 0 && end > 0);
		
		if (searchIndex < newContent.length) {
			
			int len = newContent.length - searchIndex;
			
			if (len < this.capacity) {
				Buffer.copy(newContent, searchIndex, len, this.buffer, 0);
				this.length = len;
			} else {
				this.length = 0;
			}
		} else {
			this.length = 0;
		}
		
		return result;
	}
	
	public ArrayList<String> append(String content) {
		return this.append(content.toCharArray());
	}
	
	public int capacity() {
		return this.capacity;
	}
	
	public int length() {
		return this.length;
	}
	
	public boolean isEmpty() {
		return this.length == 0;
	}
	
	/**
	 * Finds the beginning of an nmea sentence, a sentence begins with '!' or '$'
	 * @param p the char string to search the beginning of the sentence
	 * @param startIndex search from the startIndex position
	 * @return returns the index of the beginning, or -1 if not found.
	 */
	private int findBeginMark(char[] p, int startIndex) {
				
		for (int index = startIndex; index < p.length; index++) {

			char c = p[index];
			if (c == '!' || c == '$')
				return index;
		}
	    return -1;
	}
	
	/**
	 * Finds the end of an nmea sentence, the end of a sentence is "\r\n",
	 * if only '\n' or '\r' found, it is also treated as the end of a sentence
	 * if none of the above found, but the beginning of a sentence is found, sentence end is also found.
	 * @param p the character string to search for
	 * @param startIndex search from the startIndex position
	 * @return the index of the beginning of the next sentence, or -1 if not found
	 */
	private int findEndMark(char[] p, int startIndex) {
		
		for (int index = startIndex; index < p.length; index++) {
			
			char c = p[index];
			if (c == '\n' || c == '\r') {
				if (c == '\r') {
					if (index < p.length - 1 && p[index + 1] == '\n') {
						return index + 2;
					}
				}
				return index + 1;
			} else if (c == '!' || c == '$') {
				return index;
			}
		}
		
		return -1;
	}
}
