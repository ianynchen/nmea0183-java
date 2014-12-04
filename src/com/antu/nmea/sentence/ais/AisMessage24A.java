package com.antu.nmea.sentence.ais;

public class AisMessage24A implements IEncapsulatedAisMessage {

	public AisMessage24A() {
		super();
	}

	@Override
	public int messageType() {
		return 24;
	}

}
