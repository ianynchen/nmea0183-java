package com.antu.nmea.sentence.ais;

public class AisMessage7 implements IEncapsulatedAisMessage {

	public AisMessage7() {
		super();
	}

	@Override
	public int messageType() {
		return 7;
	}

}
