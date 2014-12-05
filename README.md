nmea0183-java
=============

java implementation of NMEA 0183 coder/decoder, along with transportation support

This is a java implementation of NMEA 0183 encoder/decoder.

Decoding process refers to converting an NMEA 0183 text string to corresponding Java objects, and 
encoding process is the process that converts Java object into NMEA 0183 text string(s).

The outcome of this project will contain three parts:

1, NMEA 0183 encoder/decoder - this is now partially completed, with the structure for parametric/query/encapsulation sentences done, more testing need to be completed. Currently encoding for encapsulation sentences not working.

2, NMEA 0183 data sources, which allows one to easily connect to serial ports/files/TCP servers, or allow TCP/clients to be connected to feed NMEA 0183 text strings.

3, A transmitter mechanism that allows 0mq/tcp transmission of decoded/raw NMEA sentences to be broadcast over the network such that applications can receive NMEA information as necessary.

To use the codec, first create a CodecManager object:

CodecManager manager = new CodecManager();

to allow decoding of a certain sentence, say VDM,

manager.acceptanceList().addAllMessages("vdm");

for other sentences, check the AcceptanceList.java file.

to decode a sentence:

manager.decode("!AIVDM,1,1,,1,1P000Oh1IT1svTP2r:43grwb05q4,0*01\r\n");

the text that's sent for decoding does not have to be a complete sentence. CodecManager will try to gather all bits and pieces needed for one sentence and then start decoding.

to retrieve decoding results, one needs to add a java.util.Obsever object to CodecManager, which is a subclass of java.util.Observable
