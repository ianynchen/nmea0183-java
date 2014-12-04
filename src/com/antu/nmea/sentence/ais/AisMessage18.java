package com.antu.nmea.sentence.ais;

public class AisMessage18 implements IEncapsulatedAisMessage {

	public AisMessage18() {
		super();
	}

	@Override
	public int messageType() {
		return 18;
	}

}
