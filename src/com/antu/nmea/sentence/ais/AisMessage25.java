package com.antu.nmea.sentence.ais;

public class AisMessage25 implements IEncapsulatedAisMessage {

	public AisMessage25() {
		super();
	}

	@Override
	public int messageType() {
		return 25;
	}

}
