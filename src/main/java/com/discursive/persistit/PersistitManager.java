package com.discursive.persistit;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.persistit.Exchange;
import com.persistit.Persistit;
import com.persistit.Volume;
import com.persistit.exception.PersistitException;

@Component
public class PersistitManager {
	
	final static Logger log = LoggerFactory.getLogger( PersistitManager.class );

	private Persistit db;
	private Volume people;
	
	@PostConstruct
	public void init() {
		log.info( "Initializing Persistit Manager" );
		
		db = new Persistit();
		InputStream is = getClass().getClassLoader().getResourceAsStream( "persistit.properties");
		Properties p = new Properties();
		
        try {
    		p.load( is );
            db.initialize(p);
            people = db.loadVolume( "people" );
        } catch( PersistitException pe ) {
        	log.error( "Error initializing Persistit", pe );
        } catch (IOException e) {
        	log.error( "Error reading Persistit config", e );
        }
	}
	
	@PreDestroy
	public void cleanup() {
		log.info( "Closing Persistit Database" ); 
		try {
		db.flush();
		db.close();
		} catch( PersistitException pe ) {
			log.error( "Error closing datbases" );
		}
	}
	
	public Exchange getPersonExchange() throws PersistitException {
		return db.getExchange( people, "person", true );
	}
	
	
	

}
