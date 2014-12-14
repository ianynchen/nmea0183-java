package com.antu.nmea.source.tcp.server;

import com.antu.nmea.source.IDataSourceSetting;

public class TcpServerDataSourceSetting implements IDataSourceSetting {
	
	private boolean isAutoRecover;
	
	private String name;
	
	private String host;
	
	private int port;
	
	private String username;
	
	private String password;

	public TcpServerDataSourceSetting() {
	}
	
	public void setAutoRecover(boolean isAutoRecover) {
		this.isAutoRecover = isAutoRecover;
	}

	@Override
	public boolean getAutoRecover() {
		return this.isAutoRecover;
	}

	@Override
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
