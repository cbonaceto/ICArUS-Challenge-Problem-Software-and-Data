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
package org.mitre.icarus.cps.experiment_core.subject_data;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

public class DefaultSubjectDataWriter implements ISubjectDataWriter {
	
	protected static final String newline = System.getProperty("line.separator");
	
	/** The delimiter string (e.g. a tab, comma, etc.) */
	protected String delimiterStr = "\t";
	
	public String getDelimiterStr() {
		return delimiterStr;
	}

	public void setDelimiterStr(String delimiterStr) {
		this.delimiterStr = delimiterStr;
	}

	@Override
	public void recordSubjectTrialData(File dataFile, String subjectID,
			int trialNum, List<SubjectDataElement> data, boolean append) {
		//System.out.println("recording trial data: " + dataFile.getName() + ", append: " + append);
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(dataFile, append);
			int numElements = data.size();
			int currentElement = 0;
			if(!append) {
				//Create the data file column headers
				
				//Add the subject ID as the first column
				fileWriter.append("S_ID");
				if(!data.isEmpty()) {
					fileWriter.append(delimiterStr);
				}
				
				for(SubjectDataElement dataElement : data) {
					fileWriter.append(dataElement.getElementName());
					if(currentElement < numElements - 1) {
						fileWriter.append(delimiterStr);
					}
					else {
						currentElement++;
					}
				}
			}
			//Write a newline
			fileWriter.append(newline);
			
			//Write the data elements
			currentElement = 0;
			
			//Add the subject ID as the first column
			fileWriter.append(subjectID);
			if(!data.isEmpty()) {
				fileWriter.append(delimiterStr);
			}
			
			for(SubjectDataElement dataElement : data) {
				if(dataElement.getElementValue() == null) {
					fileWriter.append("N/A");
				}
				else {
					fileWriter.append(dataElement.getElementValue().toString());	
				}				
				if(currentElement < numElements - 1) {
					fileWriter.append(delimiterStr);
				}
				else {
					currentElement++;
				}
			}	
			fileWriter.flush();
			fileWriter.close();
		} catch(Exception ex) {
			System.err.println("Error saving subject data, details");
				ex.printStackTrace();
		} finally {
			try {
				fileWriter.flush();
				fileWriter.close();
			} catch(Exception ex) {}
		}
	}
}
