package com.antu.nmea.sentence.ais;

import com.antu.nmea.sentence.IEncapsulatedSentence;

public interface IEncapsulatedAisMessage extends IEncapsulatedSentence {

	public int messageType();
}
