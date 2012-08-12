package com.discursive.persistit;

import java.io.File;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DownloadDataset implements Runnable {

	final static Logger log = LoggerFactory.getLogger(DownloadDataset.class);

	public void run() {

		log.info( "Checking for dataset..." );
		if( isCompleted() ) {
			log.info( "  It looks like you've the Freebase Person dataset, skipping download.");
		} else {
			download();
		}
	}	
	
	public boolean isCompleted() {
		File data = new File("freebase-person.tsv");
		return data.exists() && data.length() == 167370806;
	}
	
	public void download() {
		try {
			URL website = new URL("http://download.freebase.com/datadumps/2012-08-09/browse/people/person.tsv");
			File data = new File("freebase-person.tsv");

			log.info( "  Downloading Data Set from Freebase (2.0 GB)..." );
			log.info( "    from: " + website.toString() );
			log.info( "    to: " + data.toString() );
			FileUtils.copyURLToFile( website, data );
			
		} catch (Exception e) {
			log.error( "Error downloading datafile", e );
		}

	}

}
