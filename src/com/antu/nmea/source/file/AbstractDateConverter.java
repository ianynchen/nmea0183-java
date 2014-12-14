package com.antu.nmea.source.file;

import java.util.Date;

public abstract class AbstractDateConverter {

	public AbstractDateConverter() {
	}

	abstract public Date convert(String value);
}
