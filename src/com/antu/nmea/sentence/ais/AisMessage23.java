package com.antu.nmea.sentence.ais;

public class AisMessage23 implements IEncapsulatedAisMessage {

	public AisMessage23() {
		super();
	}

	@Override
	public int messageType() {
		return 23;
	}

}
