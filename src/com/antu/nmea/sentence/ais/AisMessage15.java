package com.antu.nmea.sentence.ais;

public class AisMessage15 implements IEncapsulatedAisMessage {

	public AisMessage15() {
		super();
	}

	@Override
	public int messageType() {
		return 15;
	}

}
