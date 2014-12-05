package com.antu.nmea.codec;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.antu.nmea.codec.exception.SentenceFieldCodecNotFoundException;
import com.antu.nmea.sentence.INmeaSentence;
import com.antu.nmea.sentence.NmeaSentence;
import com.antu.nmea.sentence.QuerySentence;

public class QuerySentenceCodec extends AbstractNmeaSentenceCodec {

	@Override
	public String getCodecType() {
		return "query";
	}

	@Override
	protected NmeaSentence createSentence(Date timestamp, String sentenceType) {
		return new QuerySentence(timestamp);
	}

	@Override
	protected boolean doDecode(String sentenceType, INmeaSentence sentence,
			String[] segments) {
		
		if (segments == null || segments.length != 2)
			return false;
		
		if (!(sentence instanceof QuerySentence))
			return false;
		
		QuerySentence s = (QuerySentence) sentence;
		
		if (segments[0].length() != 6)
			return false;
		
		s.requestTalker = segments[0].substring(1, 3);
		s.requestedTalker = segments[0].substring(3, 5);
		
		if (segments[1].indexOf('*') > 0) {
			s.query = segments[1].substring(0, segments[1].indexOf('*'));
		} else {
			s.query = segments[1];
		}
		
		return true;
	}

	@Override
	protected boolean postDecodeProcess(INmeaSentence sentence) {

		this.setChanged();
		this.notifyObservers(sentence);
		return true;
	}

	@Override
	protected boolean preEncodeProcess(INmeaSentence sentence) {
		return true;
	}

	@Override
	protected List<String> doEncode(String talker, INmeaSentence sentence)
			throws SentenceFieldCodecNotFoundException {
		
		ArrayList<String> result = new ArrayList<String>();
		
		if (sentence instanceof QuerySentence) {
			StringBuilder builder = new StringBuilder("$");
			
			QuerySentence query = (QuerySentence)sentence;
			
			if (query.requestTalker == null || query.requestTalker.length() != 2 ||
					query.requestedTalker == null || query.requestedTalker.length() != 2 ||
					query.query == null || query.query.length() == 0)
				return result;
			
			builder.append(query.requestTalker).append(query.requestedTalker).append('Q');
			builder.append(',').append(query.query);
			
			this.appendCheckSum(builder);
			builder.append("\r\n");
			result.add(builder.toString());
			
			return result;
		}
		return result;
	}
}
