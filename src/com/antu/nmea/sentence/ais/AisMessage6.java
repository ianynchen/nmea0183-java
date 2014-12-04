package com.antu.nmea.sentence.ais;

public class AisMessage6 implements IEncapsulatedAisMessage {

	public AisMessage6() {
		super();
	}

	@Override
	public int messageType() {
		return 6;
	}

}
