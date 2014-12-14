package com.antu.nmea.source.file;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.antu.nmea.codec.exception.SentenceCodecNotFoundException;
import com.antu.nmea.codec.exception.SentenceFieldCodecNotFoundException;
import com.antu.nmea.sentence.INmeaSentence;
import com.antu.nmea.source.AbstractNmeaDataSource;
import com.antu.nmea.source.AcceptanceSetting;
import com.antu.util.GenericFactory;

public class FileDataSource extends AbstractNmeaDataSource {

	static private Log logger = LogFactory.getLog(FileDataSource.class);
	
	private FileDataSourceSetting setting;

	private boolean stopRequested = false;
	
	private GenericFactory<AbstractDateConverter> converterFactory;

	public FileDataSource(String talker, FileDataSourceSetting setting,
			AcceptanceSetting accSetting) {
		super(talker, accSetting);
		
		assert(setting != null);
		this.setting = setting;
		
		this.converterFactory = new GenericFactory<AbstractDateConverter>("com.antu.nmea.source.file", "?DateConverter");
	}

	@Override
	public void stop() {
		this.stopRequested = true;
	}

	@Override
	public void run() {
		
		stopRequested = false;
		String fileName = this.setting.getFileName();
		
		while (!stopRequested) {
			try (BufferedReader reader = 
					new BufferedReader(
							new InputStreamReader(
									new FileInputStream(fileName), 
									"ASCII"))){
				
				do {
					String line = null;
					int lineIndex = 1;
					
					// read line
					while ((line = reader.readLine()) != null) {
						
						if (line.endsWith("\r\n")) {
							
						} else if (line.endsWith("\r")) {
							line += "\n";
						} else if (line.endsWith("\n")) {
							line.replace("\n", "\r\n");
						} else {
							line += "\r\n";
						}
						
						int index = line.indexOf(this.setting.getDelimiter());
						try {
							if (index >= 0) {
								Date time = Calendar.getInstance().getTime();
								String timeline = line.substring(0, index);
								
								AbstractDateConverter converter = this.converterFactory.getBySymbol(this.setting.getConverter());
								
								if (converter == null) {
									converter = this.converterFactory.getByFullName(this.setting.getConverter());
								}
								
								if (converter != null) {

									time = converter.convert(timeline);
									line = line.substring(index + 1);
									this.manager.decode(time, line);
								} else {
									FileDataSource.logger.error("unable to find converter for " + this.setting.getConverter() + ", timeline is: " + timeline);
								}
								
							} else {
								manager.decode(line);
							}
						} catch (ClassNotFoundException
								| InstantiationException
								| IllegalAccessException
								| SentenceFieldCodecNotFoundException
								| SentenceCodecNotFoundException e) {
							FileDataSource.logger.error("error parsing file: " + fileName + ", line: " + new Integer(lineIndex).toString(), e);
						}
						lineIndex++;
						Thread.sleep(((FileDataSourceSetting)setting).getReadIntervalInMillis());
					}
				} while (this.setting.getAutoRecover());
				// if not auto recover, terminate after file reaches end.
			} catch (IOException | InterruptedException e) {
				FileDataSource.logger.error("error reading file: " + fileName, e);
				break;
			}
		}
	}

	@Override
	public String getName() {
		return this.setting.getName();
	}

	@Override
	public void send(INmeaSentence sentence) {
		// do nothing
	}

}
