package com.antu.nmea.sentence.ais;

public class AisMessage12 implements IEncapsulatedAisMessage {

	public AisMessage12() {
		super();
	}

	@Override
	public int messageType() {
		return 12;
	}

}
