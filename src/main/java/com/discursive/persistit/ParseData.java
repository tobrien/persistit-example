package com.discursive.persistit;

import java.io.File;
import java.io.FileReader;
import java.util.Iterator;

import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.persistit.Exchange;
import com.persistit.TreeStatistics;
import com.persistit.exception.PersistitException;

@Component
public class ParseData implements Runnable {

	final static Logger log = LoggerFactory.getLogger(ParseData.class);

	@Autowired
	private PersistitManager persistitManager;

	public void run() {

		log.info( "Parsing Data...");
		if( isParsed() ) {
			log.info( "  Looks like you've already parsed this data." );
		} else {
			parseData();
		}

	}

	public boolean isParsed() {
		TreeStatistics stats;
		boolean parsed = false;
		
		try {
			Exchange exchange = persistitManager.getPersonExchange();
			stats = exchange.getTree().getStatistics();
			parsed = stats.getStoreCounter() > 2300000;

		} catch (PersistitException pe) {
			log.error("  Error calculating tree statistics", pe);
		}
		
		return parsed;
	}
	
	public void parseData() {
		File data = new File("freebase-person.tsv");

		try {
			int i = 0;

			Exchange exchange = persistitManager.getPersonExchange();

			log.info("  Parsing Data File " + data.toString());
			Iterator<String> lines = new LineIterator(new FileReader(data));
			String[] columns = lines.next().split("\t");
			while (lines.hasNext()) {
				String line = lines.next();
				String[] elements = line.split("\t");

				exchange.getKey().clear();
				exchange.getKey().append(elements[0].trim());
				exchange.getKey().append(elements[1].trim());
				exchange.getValue().put(elements);
				exchange.store();

				i++;
				if (i % 250000 == 0) {
					log.info("  Parsed record: " + i + ", "
							+ ArrayUtils.toString(elements));
				}

			}

		} catch (Exception e) {
			log.error("  Error parsing data", e);
		}

	}
}
