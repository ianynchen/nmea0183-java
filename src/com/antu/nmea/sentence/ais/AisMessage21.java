package com.antu.nmea.sentence.ais;

public class AisMessage21 implements IEncapsulatedAisMessage {

	public AisMessage21() {
	}

	@Override
	public int messageType() {
		return 21;
	}

}
