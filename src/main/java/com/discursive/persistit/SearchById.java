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
import com.persistit.KeyFilter.Term;
import com.persistit.KeyParser;

@Component
public class SearchById implements Runnable {

	final static Logger log = LoggerFactory.getLogger(SearchById.class);

	@Autowired
	private PersistitManager persistItManager;

	@Override
	public void run() {

		try {
			Exchange exchange = persistItManager.getPersonExchange();
			
			String id = "/m/0484q";
			KeyParser parser = new KeyParser("{*,\"" + id + "\"}");
			KeyFilter filter = parser.parseKeyFilter();
			
			
			exchange.append(Key.BEFORE);
			exchange.next(filter);
				log.info( "Fetched Key: " + exchange.getKey().toString() );
				log.info( "Fetched Value: " + exchange.getValue().toString() );

		} catch (Exception e) {
			log.error( "There was an error searching for a record", e );

		}

	}
}
