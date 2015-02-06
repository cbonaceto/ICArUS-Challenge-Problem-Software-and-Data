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
package org.mitre.icarus.cps.app.experiment.ui_study.exam;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.mitre.icarus.cps.exam.base.loader.IcarusExamLoader;

/**
 * @author CBONACETO
 *
 */
public class UIStudyExamLoader {

	/**
	 * Marshalls a UIStudyExam to XML.
	 * 
	 * @param exam the exam to marshall
	 * @return the exam XML string
	 * @throws JAXBException
	 */
	public static String marshalExam(UIStudyExam exam) throws JAXBException {
		Marshaller marshaller = IcarusExamLoader.createMarshallerForClass(UIStudyExam.class);
		
		ByteArrayOutputStream output = new ByteArrayOutputStream (); 
		marshaller.marshal(exam, output);
		
		return output.toString();
	}
	
	
	
	/**
	 * Unmarshalls a UIStudyExam from an XML file. 
	 *
	 * @param examFile the URL to the exam file
	 * @return a UIStudyExam instance
	 * @throws JAXBException
	 */
	public static UIStudyExam unmarshalExam(URL examFile) throws JAXBException {
		boolean validate = false;
		Unmarshaller unmarshaller = IcarusExamLoader.createUnmarshallerForClass(UIStudyExam.class, null, validate);		
		
		UIStudyExam exam = (UIStudyExam)(unmarshaller.unmarshal(examFile));
		if(exam != null) {
			exam.setOriginalPath(examFile);
		}		
		
		if(validate && unmarshaller.getEventHandler() != null && !unmarshaller.getEventHandler().toString().isEmpty()) {
			throw new JAXBException(unmarshaller.getEventHandler().toString());
		}
		
		return exam;
	}
	
	/**
	 * Marshalls a UIStudyPhase to XML.
	 * 
	 * @param task the UI study phase
	 * @return the task XML string
	 * @throws JAXBException
	 */
	public static String marshalTask(UIStudyPhase<?> task) throws JAXBException {
		Marshaller marshaller = IcarusExamLoader.createMarshallerForClass(task.getClass());
		
		ByteArrayOutputStream output = new ByteArrayOutputStream (); 
		marshaller.marshal(task, output);
		
		return output.toString();
	}
	
	/**
	 * Unmarshalls a UIStudyPhase from XML.
	 * 
	 * @param taskFile the URL to the task file
	 * @param validate whether to validate the file against the schema
	 * @return a TaskTestPhase instance
	 * @throws JAXBException
	 */
	public static UIStudyPhase<?> unmarshalTask(URL taskFile) throws JAXBException {
		Unmarshaller unmarshaller = IcarusExamLoader.createUnmarshallerForClass(UIStudyPhase.class, null, false);		
		
		UIStudyPhase<?> task = (UIStudyPhase<?>)(unmarshaller.unmarshal(taskFile));		
		
		return task;
	}
	
	/**
	 * Test main.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			//String fileName = "data/UI Study/UIstudydraft.xml";
			String fileName = "data/UI Study/UIStudy1.xml";
			URL examFileUrl = new File(fileName).toURI().toURL();
			UIStudyExam exam = UIStudyExamLoader.unmarshalExam(examFileUrl);
			System.out.println("Exam XML:");
			System.out.println(UIStudyExamLoader.marshalExam(exam));
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}