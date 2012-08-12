package com.discursive.persistit;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;

public class SimpleExample {

	final static Logger log = LoggerFactory.getLogger(SimpleExample.class);

	public static void main(String[] args) {
		new SimpleExample().run();
	}

	private GenericApplicationContext context;

	public void run() {

		createDirs();
		configureSpring();

		runComponent("downloadDataset");
		runComponent("parseData");
		runComponent("searchByName");
		runComponent("searchById");

		log.info("Closing Spring Context...");
		context.close();

	}

	private void runComponent(String component) {
		log.info("Running Component: " + component);

		Runnable runnable = (Runnable) context.getBean(component);

		Thread t = new Thread(runnable);
		t.run();

		try {
			t.join();
		} catch (InterruptedException e) {
			log.error("Process Interrupted", e);
		}
	}

	public void configureSpring() {
		// Configure Spring Framework
		log.info("Configuring Spring Framework...");
		GenericApplicationContext ctx = new GenericApplicationContext();
		XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(ctx);
		xmlReader.loadBeanDefinitions(new ClassPathResource(
				"applicationContext.xml"));
		ctx.refresh();
		context = ctx;
	}

	public void createDirs() {
		try {
			FileUtils.forceMkdir(new File("./data"));
			FileUtils.forceMkdir(new File("./log"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
