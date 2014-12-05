nmea0183-java
=============

java implementation of NMEA 0183 coder/decoder, along with transportation support

This is a java implementation of NMEA 0183 encoder/decoder.

Decoding process refers to converting an NMEA 0183 text string to corresponding Java objects, and 
encoding process is the process that converts Java object into NMEA 0183 text string(s).

The outcome of this project will contain three parts:

1, NMEA 0183 encoder/decoder - this is now partially completed, with the structure for parametric/query/encapsulation
   sentences done, more testing need to be completed. Currently encoding for encapsulation sentences not working.
2, NMEA 0183 data sources, which allows one to easily connect to serial ports/files/TCP servers, or allow TCP/clients
   to be connected to feed NMEA 0183 text strings.
3, A transmitter mechanism that allows 0mq/tcp transmission of decoded/raw NMEA sentences to be broadcast over the network
   such that applications can receive NMEA information as necessary.
