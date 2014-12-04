package com.antu.nmea.sentence.ais;

public class AisMessage13 implements IEncapsulatedAisMessage {

	public AisMessage13() {
		super();
	}

	@Override
	public int messageType() {
		return 13;
	}

}
