package com.antu.nmea.sentence.ais;

public class AisMessage10 implements IEncapsulatedAisMessage {

	public AisMessage10() {
		super();
	}

	@Override
	public int messageType() {
		return 10;
	}

}
