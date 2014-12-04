package com.antu.nmea.sentence.ais;

public class AisMessage5 implements IEncapsulatedAisMessage {

	public AisMessage5() {
		super();
	}

	@Override
	public int messageType() {
		return 5;
	}

}
