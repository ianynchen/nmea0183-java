package com.antu.nmea.source;

import java.util.List;

public class AcceptanceSetting {
	
	private List<MessageSetting> messageSettings;
	
	private List<String> proprietarySettings;
	
	private List<String> parametricSettings;

	public AcceptanceSetting() {
	}

	public List<MessageSetting> getMessageSettings() {
		return messageSettings;
	}

	public void setMessageSettings(List<MessageSetting> messageSettings) {
		this.messageSettings = messageSettings;
	}

	public List<String> getProprietarySettings() {
		return proprietarySettings;
	}

	public void setProprietarySettings(List<String> proprietarySettings) {
		this.proprietarySettings = proprietarySettings;
	}

	public List<String> getParametricSettings() {
		return parametricSettings;
	}

	public void setParametricSettings(List<String> parametricSettings) {
		this.parametricSettings = parametricSettings;
	}

}
