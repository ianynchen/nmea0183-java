package com.antu.nmea.sentence.ais;

public class AisMessage16 implements IEncapsulatedAisMessage {

	public AisMessage16() {
		super();
	}

	@Override
	public int messageType() {
		return 16;
	}

}
