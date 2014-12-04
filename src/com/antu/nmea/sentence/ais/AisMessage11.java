package com.antu.nmea.sentence.ais;

public class AisMessage11 implements IEncapsulatedAisMessage {

	public AisMessage11() {
		super();
	}

	@Override
	public int messageType() {
		return 11;
	}

}
