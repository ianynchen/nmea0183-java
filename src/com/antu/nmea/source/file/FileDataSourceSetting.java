package com.antu.nmea.source.file;

import com.antu.nmea.source.IDataSourceSetting;

public class FileDataSourceSetting implements IDataSourceSetting {
	
	private String name;
	private String fileName;
	private boolean isAutoRecover;
	private int readIntervalInMillis;
	private char delimiter = ';';
	private String converter = "date";

	public FileDataSourceSetting() {
	}

	@Override
	public boolean getAutoRecover() {
		return this.isAutoRecover;
	}
	
	public void setAutoRecover(boolean autoRecover) {
		this.isAutoRecover = autoRecover;
	}

	@Override
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getReadIntervalInMillis() {
		return readIntervalInMillis;
	}

	public void setReadIntervalInMillis(int readIntervalInMillis) {
		this.readIntervalInMillis = readIntervalInMillis;
	}

	public char getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(char delimiter) {
		this.delimiter = delimiter;
	}

	public String getConverter() {
		return converter;
	}

	public void setConverter(String converter) {
		this.converter = converter;
	}
}
