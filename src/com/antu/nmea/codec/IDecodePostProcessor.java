package com.antu.nmea.codec;

import com.antu.nmea.sentence.INmeaSentence;

/**
 * Used for post processing of sentences after segments are decoded.
 * @author yining
 *
 */
public interface IDecodePostProcessor {
	INmeaSentence process(INmeaSentence sentence);
}
