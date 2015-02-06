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
package org.mitre.icarus.cps.exam.phase_05.loader;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.mitre.icarus.cps.exam.base.IcarusExamPhase;
import org.mitre.icarus.cps.exam.base.loader.IcarusExamLoader;
import org.mitre.icarus.cps.exam.phase_05.IcarusExam_Phase05;
import org.mitre.icarus.cps.exam.phase_05.response.IcarusExamPhaseResponse;
import org.mitre.icarus.cps.exam.phase_05.response.IcarusExamResponse;
import org.mitre.icarus.cps.exam.phase_05.training.IcarusTrainingPhase;

/**
 * Binds exam and response classes to XML using JAXB for the Phase 0.5 CPD format.
 * 
 * @author CBONACETO
 *
 */
public class IcarusExamLoader_Phase05 extends IcarusExamLoader<IcarusExam_Phase05, IcarusExamPhase, IcarusTrainingPhase> {
	
	/** The exam schema */
	protected static Schema ExamSchema;
	
	/** The response schema */
	protected static Schema ResponseSchema;
	
	/** Load the exam and response schemas */
	static {
		SchemaFactory s = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		try {
			ClassLoader classLoader = IcarusExamLoader_Phase05.class.getClassLoader();
			ExamSchema = s.newSchema(classLoader.getResource("schemas/Phase_05_CPD/ExamSchema.xsd"));
			ResponseSchema = s.newSchema(classLoader.getResource("schemas/Phase_05_CPD/ResponseSchema.xsd"));				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String marshalExamToXml(IcarusExam_Phase05 exam) throws JAXBException {
		return marshalExam(exam);
	}	

	/**
	 * Marshalls an IcarusExam to XML.
	 * 
	 * @param exam
	 * @return
	 * @throws JAXBException
	 */
	public static String marshalExam(IcarusExam_Phase05 exam) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(IcarusExam_Phase05.class);
		Marshaller marshaller = jaxbContext.createMarshaller();

		marshaller.setEventHandler(silentEventHandler);
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		ByteArrayOutputStream output = new ByteArrayOutputStream (); 
		marshaller.marshal(exam, output);
		
		return output.toString();
	}
	
	/**
	 * Unmarshalls an IcarusExam from an XML file. Does not validate
	 * against the schema. 
	 *
	 * @param examFile
	 * @return
	 * @throws JAXBException
	 */
	public static IcarusExam_Phase05 unmarshalExam(URL examFile) throws JAXBException {
		return unmarshalExam(examFile, false);
	}	
	
	@Override
	public IcarusExam_Phase05 unmarshalExamFromXml(URL examXmlFile, boolean validate) throws JAXBException {
		return unmarshalExam(examXmlFile, validate);
	}		
	
	/** 
	 * Unmarshalls an IcarusExam and validates against the schema if validate is true.
	 * 
	 * @param examFile
	 * @param validate
	 * @return
	 * @throws JAXBException
	 */
	public static IcarusExam_Phase05 unmarshalExam(URL examFile, boolean validate) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(IcarusExam_Phase05.class);			
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		ValidateEventHandler eventHandler = null;
		if(validate) {
			unmarshaller.setSchema(ExamSchema);
			eventHandler = new ValidateEventHandler();
			unmarshaller.setEventHandler(eventHandler);
		}
		else {
			unmarshaller.setEventHandler(silentEventHandler);
		}
		
		IcarusExam_Phase05 exam = (IcarusExam_Phase05)(unmarshaller.unmarshal(examFile));
		
		if(validate && eventHandler != null && !eventHandler.getErrorString().isEmpty()) {
			throw new JAXBException(eventHandler.getErrorString());
		}
		
		return exam;
	}	
	
	@Override
	public IcarusExam_Phase05 unmarshalExamFromXml(String examXmlString, boolean validate) throws JAXBException {
		return unmarshalExam(examXmlString, validate);
	}
	
	/**	  
	 * Unmarshalls an IcarusExam from an XML string and validates the XML against the schema if validate is true.
	 * 
	 * @param examXml a string containing the exam XML
	 * @param validate whether or not to validate the XML against the schema
	 * @return an IcarusExam_Phase1 instance
	 * @throws JAXBException
	 */
	public static IcarusExam_Phase05 unmarshalExam(String examXml, boolean validate) throws JAXBException {
		Unmarshaller unmarshaller = createUnmarshallerForClass(IcarusExam_Phase05.class, ExamSchema, validate);
		
		StringReader examXmlStream = new StringReader(examXml);
		IcarusExam_Phase05 exam = (IcarusExam_Phase05)(unmarshaller.unmarshal(examXmlStream));		
		
		if(validate && unmarshaller.getEventHandler() != null && !unmarshaller.getEventHandler().toString().isEmpty()) {
			throw new JAXBException(unmarshaller.getEventHandler().toString());
		}
		
		return exam;
	}	
	
	/**
	 * Marshall exam response data to XML.
	 * 
	 * @param examResponseData
	 * @return
	 * @throws JAXBException
	 */
	public static String marshalExamResponse(IcarusExamResponse examResponseData) throws JAXBException {
		
		JAXBContext jaxbContext = JAXBContext.newInstance(IcarusExamResponse.class);
		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setEventHandler(silentEventHandler);
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		ByteArrayOutputStream output = new ByteArrayOutputStream (); 
		marshaller.marshal(examResponseData, output);			
		return output.toString();
	}
	
	/**
	 *  Umarshalls exam response data from XML.  Does not validate against the schema.
	 * 
	 * @param examResponseFile
	 * @return
	 * @throws JAXBException
	 */
	public static IcarusExamResponse unmarshalExamResponse(URL examResponseFile) throws JAXBException {
		return unmarshalExamResponse(examResponseFile, false);
	}
	
	/**
	 * Unmarshalls exam response data from XML and validates against the schema if validate is true. 
	 * 
	 * @param examResponseFile
	 * @param validate
	 * @return
	 * @throws JAXBException
	 */
	public static IcarusExamResponse unmarshalExamResponse(URL examResponseFile, boolean validate) throws JAXBException {

		JAXBContext jaxbContext = JAXBContext.newInstance(IcarusExamResponse.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		ValidateEventHandler eventHandler = null;
		if(validate) {
			unmarshaller.setSchema(ResponseSchema);
			eventHandler = new ValidateEventHandler();
			unmarshaller.setEventHandler(eventHandler);
		}
		else {
			unmarshaller.setEventHandler(silentEventHandler);
		}

		IcarusExamResponse response = (IcarusExamResponse)(unmarshaller.unmarshal(examResponseFile));

		if(validate && eventHandler != null && !eventHandler.getErrorString().isEmpty()) {
			throw new JAXBException(eventHandler.getErrorString());
		}

		return response;
	}
	
	/**
	 * Marshalls exam phase response data to XML.
	 * 
	 * @param examPhaseResponseData
	 * @return
	 * @throws JAXBException
	 */
	public static String marshalExamPhaseResponse(IcarusExamPhaseResponse examPhaseResponseData) throws JAXBException {

		JAXBContext jaxbContext = JAXBContext.newInstance(IcarusExamPhaseResponse.class);
		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setEventHandler(silentEventHandler);
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		ByteArrayOutputStream output = new ByteArrayOutputStream (); 
		marshaller.marshal(examPhaseResponseData, output);			
		return output.toString();		
	}
	
	/**
	 * Umarshalls exam phase response data from XML and does not validate against the schema.
	 * 
	 * @param examPhaseResponseFile
	 * @return
	 * @throws JAXBException
	 */
	public static IcarusExamPhaseResponse unmarshalExamPhaseResponse(URL examPhaseResponseFile) throws JAXBException {
		return unmarshalExamPhaseResponse(examPhaseResponseFile, false);
	}
	
	/**
	 * Umarshalls exam phase response data from XML and validates against the schema if validate is true.
	 * 
	 * @param examPhaseResponseFile
	 * @param validate
	 * @return
	 * @throws JAXBException
	 */
	public static IcarusExamPhaseResponse unmarshalExamPhaseResponse(URL examPhaseResponseFile, boolean validate) throws JAXBException {

		JAXBContext jaxbContext = JAXBContext.newInstance(IcarusExamPhaseResponse.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		ValidateEventHandler eventHandler = null;
		if(validate) {
			unmarshaller.setSchema(ResponseSchema);
			eventHandler = new ValidateEventHandler();
			unmarshaller.setEventHandler(eventHandler);
		}
		else {
			unmarshaller.setEventHandler(silentEventHandler);
		}
		
		IcarusExamPhaseResponse response = (IcarusExamPhaseResponse)(unmarshaller.unmarshal(examPhaseResponseFile));

		if(validate && eventHandler != null && !eventHandler.getErrorString().isEmpty()) {
			throw new JAXBException(eventHandler.getErrorString());
		}

		return response;
	}
	
	@Override
	public String marshalExamPhase(IcarusExamPhase examPhase) throws JAXBException {
		Marshaller marshaller = createMarshallerForClass(examPhase.getClass());
		
		ByteArrayOutputStream output = new ByteArrayOutputStream (); 
		marshaller.marshal(examPhase, output);
		
		return output.toString();
	}

	@Override
	public IcarusExamPhase unmarshalExamPhase(URL examPhaseXmlFile, boolean validate) throws JAXBException {
		Unmarshaller unmarshaller = createUnmarshallerForClass(IcarusExamPhase.class, ExamSchema, validate);		
		
		IcarusExamPhase examPhase = (IcarusExamPhase)(unmarshaller.unmarshal(examPhaseXmlFile));
		
		if(validate && unmarshaller.getEventHandler() != null && !unmarshaller.getEventHandler().toString().isEmpty()) {
			throw new JAXBException(unmarshaller.getEventHandler().toString());
		}
		
		return examPhase;
	}

	@Override
	public IcarusExamPhase unmarshalExamPhase(String examPhaseXml, boolean validate) throws JAXBException {
		Unmarshaller unmarshaller = createUnmarshallerForClass(IcarusExamPhase.class, ExamSchema, validate);		
		
		StringReader examPhaseXmlStream = new StringReader(examPhaseXml);		
		IcarusExamPhase examPhase = (IcarusExamPhase)(unmarshaller.unmarshal(examPhaseXmlStream));
		
		if(validate && unmarshaller.getEventHandler() != null && !unmarshaller.getEventHandler().toString().isEmpty()) {
			throw new JAXBException(unmarshaller.getEventHandler().toString());
		}
		
		return examPhase;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.base.loader.IcarusExamLoader#marshalTutorialPhase(org.mitre.icarus.cps.exam.base.training.IcarusTutorialPhase)
	 */
	@Override
	public String marshalTutorialPhase(IcarusTrainingPhase tutorialPhase)
			throws JAXBException {
		Marshaller marshaller = createMarshallerForClass(tutorialPhase.getClass());
		
		ByteArrayOutputStream output = new ByteArrayOutputStream (); 
		marshaller.marshal(tutorialPhase, output);
		
		return output.toString();
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.base.loader.IcarusExamLoader#unmarshalTutorialPhase(java.net.URL, boolean)
	 */
	@Override
	public IcarusTrainingPhase unmarshalTutorialPhase(URL tutorialPhaseXmlFile,
			boolean validate) throws JAXBException {
		Unmarshaller unmarshaller = createUnmarshallerForClass(IcarusTrainingPhase.class, ExamSchema, validate);		
		
		IcarusTrainingPhase tutorialPhase = (IcarusTrainingPhase)(unmarshaller.unmarshal(tutorialPhaseXmlFile));
		
		if(validate && unmarshaller.getEventHandler() != null && !unmarshaller.getEventHandler().toString().isEmpty()) {
			throw new JAXBException(unmarshaller.getEventHandler().toString());
		}
		
		return tutorialPhase;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.base.loader.IcarusExamLoader#unmarshalTutorialPhase(java.lang.String, boolean)
	 */
	@Override
	public IcarusTrainingPhase unmarshalTutorialPhase(String tutorialPhaseXml,
			boolean validate) throws JAXBException {
		Unmarshaller unmarshaller = createUnmarshallerForClass(IcarusTrainingPhase.class, ExamSchema, validate);		
		
		StringReader tutorialPhaseXmlStream = new StringReader(tutorialPhaseXml);		
		IcarusTrainingPhase tutorialPhase = (IcarusTrainingPhase)(unmarshaller.unmarshal(tutorialPhaseXmlStream));
		
		if(validate && unmarshaller.getEventHandler() != null && !unmarshaller.getEventHandler().toString().isEmpty()) {
			throw new JAXBException(unmarshaller.getEventHandler().toString());
		}
		
		return tutorialPhase;
	}
}