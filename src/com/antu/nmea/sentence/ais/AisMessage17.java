package com.antu.nmea.sentence.ais;

public class AisMessage17 implements IEncapsulatedAisMessage {

	public AisMessage17() {
		super();
	}

	@Override
	public int messageType() {
		return 17;
	}

}
