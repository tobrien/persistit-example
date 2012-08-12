package com.discursive.persistit;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.persistit.Exchange;
import com.persistit.Key;
import com.persistit.KeyFilter;
import com.persistit.KeyParser;

@Component
public class SearchByName implements Runnable {

	final static Logger log = LoggerFactory.getLogger(SearchByName.class);

	@Autowired
	private PersistitManager persistItManager;

	@Override
	public void run() {

		try {
			Exchange exchange = persistItManager.getPersonExchange();

			String name = "Will Smith";
			int num = 5;
			
			KeyParser parser = new KeyParser("{\""+ name + "\":\"Z\"}");
			KeyFilter filter = parser.parseKeyFilter();
			exchange.append(Key.BEFORE);
			exchange.next(filter);
			
			for( int i = 0; i < num; i++ ) {
				log.info( "Fetched Key: " + exchange.getKey().toString() );
				log.info( "Fetched Value: " + exchange.getValue().toString() );
				exchange.next(filter);
			}

		} catch (Exception e) {
			log.error( "There was an error finding a record", e );
		}

	}
}
