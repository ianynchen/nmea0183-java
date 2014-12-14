package com.antu.nmea.source.tcp.client;

import com.antu.nmea.source.IDataSourceSetting;

public class TcpClientDataSourceSetting implements IDataSourceSetting {
	
	private String name;
	
	private boolean isAutoRecover = true;
	
	private int port = 8904;

	public TcpClientDataSourceSetting() {
	}

	@Override
	public boolean getAutoRecover() {
		return this.isAutoRecover;
	}
	
	public void setAutoRecover(boolean isAutoRecover) {
		this.isAutoRecover = isAutoRecover;
	}

	@Override
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
}
