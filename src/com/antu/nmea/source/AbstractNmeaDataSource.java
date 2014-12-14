package com.antu.nmea.source;

import java.util.Observable;
import java.util.Observer;

import com.antu.nmea.codec.CodecManager;

abstract public class AbstractNmeaDataSource implements INmeaDataSource, Observer {
	
	protected AcceptanceSetting acceptanceSetting;
	
	protected CodecManager manager;
	
	protected String talker;

	public AbstractNmeaDataSource(String talker, AcceptanceSetting accSetting) {
		
		assert(accSetting != null);
		manager = new CodecManager();
		
		this.acceptanceSetting = accSetting;
		
		manager.addObserver(this);
		AbstractNmeaDataSource.updateAcceptanceList(manager, acceptanceSetting);
	}
	
	static public void updateAcceptanceList(CodecManager manager, AcceptanceSetting setting) {
		
		for (String type : setting.getParametricSettings()) {
			manager.acceptanceList().addPublicSentence(type);
		}
		
		for (String type : setting.getProprietarySettings()) {
			manager.acceptanceList().addProprietarySentence(type);
		}
		
		for (MessageSetting msgSetting : setting.getMessageSettings()) {
			
			for (int msgId : msgSetting.getMessages()) {
				manager.acceptanceList().addMessage(msgSetting.getSentenceType(), msgId);
			}
		}
	}

	public AcceptanceSetting getAcceptanceSetting() {
		return acceptanceSetting;
	}

	@Override
	public void update(Observable o, Object arg) {
		//TODO
	}
}
