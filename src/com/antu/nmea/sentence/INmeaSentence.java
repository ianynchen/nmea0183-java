package com.antu.nmea.sentence;

import java.util.Date;

/**
 * @author yining
 *
 * This is the base of all NmeaSentence objects
 * 
 * NmeaSentence objects are divided into 5 groups:
 * 1. Parametric sentences
 * 2. Multi-sentence sentences
 * 3. Proprietary sentences
 * 4. Encapsulation sentences
 * 5. Query sentences
 * 
 * A parametric sentence is a simple form of NMEA sentence, with no embedded data. A parametric
 * sentence may be consisted of several sentences, but each sentence is independent of each other,
 * and hence the encoding and decoding of a particular sentence is not related to other sentences,
 * even if they are in the same group. A typical parametric sentence is RMC. Parametric sentences
 * should extend ParametricSentence, as this information is used when decoding and encoding takes place.
 * 
 * A multi-sentence sentence is a parametric sentence that would require several sentences together to
 * form a complete sentence. A typical multi-sentence sentence is TUT, TUT could require up to 255 TUT
 * sentences to form a complete sentence. Missing a sentence could cause loss of information.
 * 
 * A multi-sentence object should be first registered with the MultiSentenceTable, which is a singleton.
 * 
 * e.g.:
 *   MultiSentenceTable.instance().registerType("VDN");
 *   
 * TUT sentence is pre-registered.
 * 
 * A proprietary sentence is exactly as defined in the NMEA standards. Any sentence that begins with "$P"
 * or "!P" is treated as a proprietary sentence.
 * 
 * Encapsulation sentences and query sentences are defined exactly as in the standards. Encapsulation sentences
 * must extend the EncapsulationSentence class and query sentences are produced as a QuerySentence object.
 */
public interface INmeaSentence {
	
	String sentenceType();
	
	Date getReceiveDate();
}
