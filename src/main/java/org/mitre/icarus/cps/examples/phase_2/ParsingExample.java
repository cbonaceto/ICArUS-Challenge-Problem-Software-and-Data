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
package org.mitre.icarus.cps.examples.phase_2;

import java.net.URL;

import javax.xml.bind.JAXBException;

import org.mitre.icarus.cps.app.util.CPSFileUtils;
import org.mitre.icarus.cps.exam.phase_2.IcarusExam_Phase2;
import org.mitre.icarus.cps.exam.phase_2.loader.IcarusExamLoader_Phase2;

/**
 * Demonstrates an example use of the exam package XML binding and feature vector parsing for Phase 2.
 * 
 * @author CBONACETO
 *
 */
public class ParsingExample {
	
	/**
	 * Loads an exam from an XML file and associated feature vector files for the exam.
	 * 
	 * @param examFileURL the URL to the exam XML file
	 * @return the exam
	 * @throws Exception
	 */
	public static IcarusExam_Phase2 loadExamAndFeatureVectors(URL examFileURL) throws Exception {
		//Load the exam 
		IcarusExam_Phase2 exam = null;		
		exam = IcarusExamLoader_Phase2.unmarshalExam(examFileURL, true);

		//Initialize the feature vector data for each mission in the exam
		if(exam != null) {
			IcarusExamLoader_Phase2.initializeExamFeatureVectorData(exam, examFileURL, null);
		}
		
		return exam;
	}
	
	/**
	 * Example main. Loads an exam.
	 * 
	 * @param args Specify the exam file name in args[0]. If null, uses the default file name "data/Phase_2_CPD/exams/Sample-Exam-DG/Sample-Exam-DG.xml".
	 */
	public static void main(String[] args) {
		//Load the sample exam or an exam passed through args 
		String fileName = "data/Phase_2_CPD/exams/Sample-Exam-DG/Sample-Exam-DG.xml";
		if(args != null && args.length > 0) {
			fileName = args[0];
		}	
		IcarusExam_Phase2 exam = null;
		URL examFileURL = null;
		try {
			examFileURL = CPSFileUtils.createFile(fileName).toURI().toURL();
			exam = loadExamAndFeatureVectors(examFileURL);
		} catch(Exception ex) {
			ex.printStackTrace();
		}		
		
		//Marshal the sample exam back to XML
		if(exam != null) {
			System.out.println();
			System.out.println("Exam XML:");
			try {
				System.out.println(IcarusExamLoader_Phase2.marshalExam(exam));
			} catch (JAXBException e) {
				e.printStackTrace();
			}
		}		
		
		System.out.println("Press ENTER to close this window");
		try {
			System.in.read();
		} catch(Exception err) {}
	}
}