package com.antu.nmea.source;

import com.antu.nmea.sentence.INmeaSentence;

public interface INmeaDataSource extends Runnable {
	
	String getName();
	void stop();
	void send(INmeaSentence sentence);
}
