/* 
 * NOTICE
 * This software was produced for the office of the Director of National Intelligence (ODNI)
 * Intelligence Advanced Research Projects Activity (IARPA) ICArUS program, 
 * BAA number IARPA-BAA-10-04, under contract 2009-0917826-016, and is subject 
 * to the Rights in Data-General Clause 52.227-14, Alt. IV (DEC 2007).
 * 
 * This software and data is provided strictly to support demonstrations of ICArUS challenge problems
 * and to assist in the development of cognitive-neuroscience architectures. It is not intended to be used
 * in operational systems or environments.
 * 
 * Copyright (C) 2015 The MITRE Corporation. All Rights Reserved.
 * 
 */
package org.mitre.icarus.cps.exam.base.loader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.ValidationEventLocator;
import javax.xml.validation.Schema;

import org.mitre.icarus.cps.exam.base.IcarusExam;
import org.mitre.icarus.cps.exam.base.IcarusExamPhase;
import org.mitre.icarus.cps.exam.base.training.IcarusTutorialPhase;
import org.mitre.icarus.cps.experiment_core.condition.InstructionsPage;

/**
 * Base class for ICArUS exam loaders.
 * 
 * @author CBONACETO
 *
 */
public abstract class IcarusExamLoader<E extends IcarusExam<?>, P extends IcarusExamPhase,
	T extends IcarusTutorialPhase<?>> {
	
	/** Event handler to use when validating against a schema */
	protected static final ValidateEventHandler validatEventHandler = new ValidateEventHandler();
	
	/** Event handler to use when not validating against a schema */
	protected static final SilentEventHandler silentEventHandler = new SilentEventHandler();	
	
	/**
	 * Marshalls an IcarusExam to XML.
	 * 
	 * @param exam the exam to marshall
	 * @return the exam XML string
	 * @throws JAXBException
	 */
	public abstract String marshalExamToXml(E exam) throws JAXBException;
	
	/**	  
	 * Unmarshalls an IcarusExam from an XML file and validates the XML against the schema if validate is true.
	 * 
	 * @param examXmlFile the URL to the exam XML file
	 * @param validate whether or not to validate the XML against the schema
	 * @return an IcarusExam instance
	 * @throws JAXBException
	 */
	public abstract E unmarshalExamFromXml(URL examXmlFile, boolean validate) throws JAXBException;
	
	/**	  
	 * Unmarshalls an IcarusExam from an XML string and validates the XML against the schema if validate is true.
	 * 
	 * @param examXmlString a string containing the exam XML
	 * @param validate whether or not to validate the XML against the schema
	 * @return an IcarusExam instance
	 * @throws JAXBException
	 */
	public abstract E unmarshalExamFromXml(String examXmlString, boolean validate) throws JAXBException;
	
	/**
	 * Marshalls an IcarusExamPhase to XML.
	 * 
	 * @param examPhase the exam phase
	 * @return the exam phase XML string
	 * @throws JAXBException
	 */
	public abstract String marshalExamPhase(P examPhase) throws JAXBException;
	
	/**
	 * Unmarshalls an IcarusExamPhase from an XML file and validates the file against the schema if validate is true.
	 * 
	 * @param examPhaseXmlFile the URL to the exam phase XML file
	 * @param validate whether to validate the file against the schema
	 * @return an IcarusExamPhase instance
	 * @throws JAXBException
	 */
	public abstract P unmarshalExamPhase(URL examPhaseXmlFile, boolean validate) throws JAXBException;
	
	/**
	 * Unmarshalls an IcarusExamPhase from XML and validates the XML against the schema if validate is true.
	 * 
	 * @param examPhaseXml a string containing the exam phase XML
	 * @param validate whether to validate the XML against the schema
	 * @return an IcarusExamPhase instance
	 * @throws JAXBException
	 */
	public abstract P unmarshalExamPhase(String examPhaseXml, boolean validate) throws JAXBException;
	
	public abstract String marshalTutorialPhase(T tutorialPhase) throws JAXBException;
	public abstract T unmarshalTutorialPhase(URL tutorialPhaseXmlFile, boolean validate) throws JAXBException;
	public abstract T unmarshalTutorialPhase(String tutorialPhaseXml, boolean validate) throws JAXBException;
	
	/**
	 * Creates a JAXB Marshaller instance to marshall the given class type.
	 * 
	 * @param clazz the class type
	 * @return a Marshaller instance for the given class type
	 * @throws JAXBException
	 */
	public static Marshaller createMarshallerForClass(Class<?> clazz) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
		Marshaller marshaller = jaxbContext.createMarshaller();

		marshaller.setEventHandler(silentEventHandler);
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);		
		return marshaller;
	}
	
	/**
	 * Create a JAXB Unmarshaller instance to unmarshall the given class type.
	 * 
	 * @param clazz the class type
	 * @param schema the schema
	 * @param validate whether the unmarshaller should validate against the schema
	 * @return an Unmarshaller instance for the given class type
	 * @throws JAXBException
	 */
	public static Unmarshaller createUnmarshallerForClass(Class<?> clazz, Schema schema, boolean validate) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(clazz);			
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		ValidateEventHandler eventHandler = null;
		if(validate) {
			unmarshaller.setSchema(schema);
			eventHandler = new ValidateEventHandler();
			unmarshaller.setEventHandler(eventHandler);
		}
		else {
			unmarshaller.setEventHandler(silentEventHandler);
		}
		return unmarshaller;	
	}	
	
	/**
	 * @param tutorialPhase
	 * @param baseUrl
	 * @throws IOException 
	 * @throws MalformedURLException 
	 */
	public static void initializeTutorialPhase(IcarusTutorialPhase<?> tutorialPhase, URL baseUrl) 
			throws MalformedURLException, IOException {
		if(tutorialPhase != null && tutorialPhase.getTutorialPages() != null) {
			initializeTutorialPages(tutorialPhase.getTutorialPages(), baseUrl);
		}
	}	
	
	/**
	 * @param examPhase
	 * @param baseUrl
	 * @throws IOException 
	 * @throws MalformedURLException 
	 */
	public static void initializeExamPhaseTutorial(IcarusExamPhase examPhase, URL baseUrl)
			throws MalformedURLException, IOException {
		if(examPhase != null && examPhase.getInstructionPages() != null) {
			initializeTutorialPages(examPhase.getInstructionPages(), baseUrl);
		}
	}	
	
	/**
	 * @param pages
	 * @param baseUrl
	 * @throws IOException 
	 * @throws MalformedURLException 
	 */
	public static void initializeTutorialPages(List<? extends InstructionsPage> pages, URL baseUrl) 
			throws MalformedURLException, IOException {
		if(pages != null && !pages.isEmpty()) {
			for(InstructionsPage page : pages) {
				if(page.getImageURL() != null && page.getPageImage() == null) {
					//Load the image for the page
					page.setPageImage(ImageIO.read(
						baseUrl == null ? new URL(page.getImageURL()) :
						new URL(baseUrl, page.getImageURL())));
					//System.out.println("Loaded image: " + baseUrl + ", " + page.getImageURL() + ", " + page.getPageImage());
				}
			}
		}
	}
	
	/**
	 * Writes the a file containing the given contents to disk.
	 * 
	 * @param contents
	 * @param file
	 * @throws IOException
	 */
	public static void writeFile(String contents, File file) throws IOException {
		IOException e = null;
		FileWriter fw = null;
		try {
			fw = new FileWriter(file);
			fw.write(contents);
			fw.flush();
			fw.close();
		} catch(IOException ex) {
			e = ex;
		} finally {
			if(fw != null) {
				try {fw.close();} catch(Exception ex) {};
			}
		}
		if(e != null) {
			throw e;
		}
	}
	
	/**
	 * Event handler that creates a string of validation errors.
	 * 
	 * @author CBONACETO
	 *
	 */
	public static class ValidateEventHandler  implements ValidationEventHandler {
		
		protected StringBuilder sb = new StringBuilder();
		
		public void resetHandler() {
			sb = new StringBuilder();
		}
		
		public String getErrorString() {
			return sb.toString();
		}
		
		@Override
		public String toString() {
			return sb.toString();
		}

		public boolean handleEvent(ValidationEvent ve) {
			if (ve.getSeverity()==ValidationEvent.FATAL_ERROR ||  
					ve .getSeverity()==ValidationEvent.ERROR) {
				ValidationEventLocator  locator = ve.getLocator();
				if(locator != null) {
					sb.append("Invalid XML file: " 
							+ locator.getURL() + "\n");
					sb.append("Error: " + ve.getMessage() + "\n");
					sb.append("Error at column " + 
							locator.getColumnNumber() + 
							", line " 
							+ locator.getLineNumber() + "\n");					
					/*System.out.println("Invalid XML file: " 
							+ locator.getURL());
					System.out.println("Error: " + ve.getMessage());
					System.out.println("Error at column " + 
							locator.getColumnNumber() + 
							", line " 
							+ locator.getLineNumber());*/
				}
				else {
					sb.append("JAXB error: " + ve.getMessage() + "\n");
					//System.out.println("JAXB error: " + ve.getMessage());
				}					
			}
			return true;
		}
	}
	
	/**
	 * Event handler that does nothing and always returns true.
	 * 
	 * @author CBONACETO
	 *
	 */
	public static class SilentEventHandler  implements ValidationEventHandler {				
		public boolean handleEvent(ValidationEvent ve) {			
			return true;
		}
	}
}