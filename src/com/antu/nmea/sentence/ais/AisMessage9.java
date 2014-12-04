package com.antu.nmea.sentence.ais;

public class AisMessage9 implements IEncapsulatedAisMessage {

	public AisMessage9() {
		super();
	}

	@Override
	public int messageType() {
		return 9;
	}

}
