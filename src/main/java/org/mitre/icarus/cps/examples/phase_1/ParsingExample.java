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
package org.mitre.icarus.cps.examples.phase_1;

import java.io.File;
import java.net.URL;

import javax.xml.bind.JAXBException;

import org.mitre.icarus.cps.app.util.CPSFileUtils;
import org.mitre.icarus.cps.exam.phase_1.IcarusExam_Phase1;
import org.mitre.icarus.cps.exam.phase_1.loader.IcarusExamLoader_Phase1;
import org.mitre.icarus.cps.exam.phase_1.testing.TaskTestPhase;

/**
 * Demonstrates an example use of the exam package XML binding and feature vector parsing.
 * 
 * @author CBONACETO
 *
 */
public class ParsingExample {
	
	/**
	 * Loads an exam from an XML file and associated feature vector files for the exam.
	 * 
	 * @param examFileURL the URL to the exam XML file
	 * @param useKml if true, uses the KML feature vector files, otherwise the CSV files are used
	 * @return the exam
	 */
	public static IcarusExam_Phase1 loadExamAndFeatureVectors(URL examFileURL, boolean useKml) throws Exception {
		//Load the exam 
		IcarusExam_Phase1 exam = null;		
		exam = IcarusExamLoader_Phase1.unmarshalExam(examFileURL, true);		
		exam.setOriginalPath(examFileURL);		

		//Initialize the feature vector data for each task in the exam
		if(exam != null && exam.getTasks() != null) {
			for(TaskTestPhase<?> task : exam.getTasks()) {
				try {
					long startTime = System.currentTimeMillis();
					System.out.print("Loading feature vector for " + task.getName() + "...");
					IcarusExamLoader_Phase1.initializeTaskFeatureVectorData(task, 
							exam.getOriginalPath(), exam.getGridSize(), 
							useKml, null);
					System.out.println("loaded in " + (System.currentTimeMillis() - startTime) + "ms");
				} catch (Exception e) {
					System.err.println("Error loading feature vector files for Task " + task.getName() + ", details: ");
					e.printStackTrace();
				} 
			}
		}
		
		return exam;
	}
	
	/**
	 * Example main. Loads an exam and exam response.
	 * 
	 * @param args Specify the exam file name in args[0] and the exam response file name in args[1]. 
	 * Default exam file name is "data/Phase_1_CPD/examples/SampleExam.xml" and default exam
	 * response file name is "data/Phase_1_CPD/examples/SampleExam_withResponses.xml".
	 */
	public static void main(String[] args) {		
		//Flag to set whether to use CSV or KML feature vector files. If false, CSV files are used.
		boolean useKml = false;
		
		//Load the sample exam or an exam passed through args 
		String fileName = "data/Phase_1_CPD/examples/SampleExam.xml"; 
		if(args != null && args.length > 0) {
			fileName = args[0];
		}	
		IcarusExam_Phase1 exam = null;
		URL examFileURL = null;
		try {
			examFileURL = CPSFileUtils.createFile(fileName).toURI().toURL();
			exam = loadExamAndFeatureVectors(examFileURL, useKml);
		} catch(Exception ex) {
			ex.printStackTrace();
		}		
		
		//Marshal the sample exam back to XML
		if(exam != null) {
			System.out.println();
			System.out.println("Loaded exam with " + exam.getTasks().size() + " tasks.");
			System.out.println("Exam XML:");
			try {
				System.out.println(IcarusExamLoader_Phase1.marshalExam(exam));
			} catch (JAXBException e) {
				e.printStackTrace();
			}
		}
		
		//Load the sample exam with responses
		fileName = "data/Phase_1_CPD/examples/SampleExam_withResponses.xml"; 
		if(args != null && args.length > 1) {
			fileName = args[1];
		}
		IcarusExam_Phase1 response = null;
		try {
			response = IcarusExamLoader_Phase1.unmarshalExam(
					new File(fileName).toURI().toURL(), true);
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
		//Marshal the sample exam with responses back to XML
		if(response != null) {
			System.out.println("Loaded exam response with " + response.getTasks().size() + "  tasks.");
			System.out.println("Response XML:");
			try {
				System.out.println(IcarusExamLoader_Phase1.marshalExam(response));
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