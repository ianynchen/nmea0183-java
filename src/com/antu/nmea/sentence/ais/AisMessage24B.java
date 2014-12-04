package com.antu.nmea.sentence.ais;

public class AisMessage24B implements IEncapsulatedAisMessage {

	public AisMessage24B() {
		super();
	}

	@Override
	public int messageType() {
		return 24;
	}

}
