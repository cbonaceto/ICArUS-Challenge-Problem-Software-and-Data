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
package org.mitre.icarus.cps.examples.phase_05;

import java.io.File;

import javax.xml.bind.JAXBException;

import org.mitre.icarus.cps.exam.phase_05.IcarusExam_Phase05;
import org.mitre.icarus.cps.exam.phase_05.loader.IcarusExamLoader_Phase05;
import org.mitre.icarus.cps.exam.phase_05.response.IcarusExamResponse;

/**
 * Demonstrates an example use of the exam package and XML binding.
 * 
 * @author CBONACETO
 *
 */
public class Example_Phase05 {
	
	/**
	 * Example main.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		//Load the sample exam
		IcarusExam_Phase05 exam = null;
		try {
			exam = IcarusExamLoader_Phase05.unmarshalExam(
					new File("data/Phase_05_CPD/exams/SampleExam/Sample_Exam_032511.xml").toURI().toURL(), true);
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
		
		//Marshall the sample exam back to XML
		if(exam != null) {
			System.out.println("Loaded exam with " + exam.getPhases().size() + " phases.");
			System.out.println("Exam XML:");
			try {
				System.out.println(IcarusExamLoader_Phase05.marshalExam(exam));
			} catch (JAXBException e) {
				e.printStackTrace();
			}
		}
		
		//Load the sample response
		IcarusExamResponse response = null;
		try {
			response = IcarusExamLoader_Phase05.unmarshalExamResponse(
					new File("data/Phase_05_CPD/exams/SampleExam/Sample_Response_032511.xml").toURI().toURL(), true);
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
		//Marshall the sample response back to XML
		if(response != null) {
			System.out.println("Loaded response with " + response.getPhaseResponses().size() + " phases.");
			System.out.println("Response XML:");
			try {
				System.out.println(IcarusExamLoader_Phase05.marshalExamResponse(response));
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
