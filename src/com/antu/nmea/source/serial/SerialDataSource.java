package com.antu.nmea.source.serial;

import javax.comm.SerialPort;
import javax.comm.SerialPortEvent;
import javax.comm.SerialPortEventListener;

import com.antu.nmea.sentence.INmeaSentence;
import com.antu.nmea.source.AbstractNmeaDataSource;
import com.antu.nmea.source.AcceptanceSetting;

public class SerialDataSource extends AbstractNmeaDataSource implements SerialPortEventListener {
	
	private SerialDataSourceSetting setting;
	
	private SerialPort port;
	
	private boolean stopRequested = false;

	public SerialDataSource(String talker, AcceptanceSetting accSetting, SerialDataSourceSetting setting) {
		super(talker, accSetting);
		
		assert(setting != null);
		this.setting = setting;
	}

	@Override
	public String getName() {
		return this.setting.getName();
	}

	@Override
	public void stop() {
		this.stopRequested = true;
	}

	@Override
	public void send(INmeaSentence sentence) {
		// TODO Auto-generated method stub

	}

	@Override
	public void run() {

		this.stopRequested = false;
		while (!this.stopRequested) {
			
			
		}
		
		this.port.close();
	}

	@Override
	public void serialEvent(SerialPortEvent evt) {
		// TODO Auto-generated method stub
		
		switch (evt.getEventType()) {
		
		}
	}

}
