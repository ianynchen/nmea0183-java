package com.antu.nmea.sentence.ais;

public class AisMessage20 implements IEncapsulatedAisMessage {

	public AisMessage20() {
		super();
	}

	@Override
	public int messageType() {
		return 20;
	}

}
