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
package org.mitre.icarus.cps.app.experiment.data_recorder;

import java.io.File;
import java.io.IOException;

import org.mitre.icarus.cps.app.util.CPSFileUtils;
import org.mitre.icarus.cps.exam.base.IcarusExam;
import org.mitre.icarus.cps.exam.base.IcarusExamPhase;
import org.mitre.icarus.cps.exam.base.training.IcarusTutorialPhase;


/**
 * 
 * 
 * @author CBONACETO
 *
 */
public abstract class AbstractSubjectDataRecorder<E extends IcarusExam<?>, P extends IcarusExamPhase, T extends IcarusTutorialPhase<?>> 
	implements ISubjectDataRecorder<E, P, T> {
	
	/**
	 * Creates the folder to store the response data for a subject
	 * Folder Name Format: data/response_data/S_siteId_subjectId
	 * 
	 * @param siteId the site ID of the subject
	 * @param subjectId the subject ID of the subject
	 * @return the folder
	 * @throws IOException 
	 */
	public static File createResponseDataFolder(File outputFolder, String siteId, String subjectId) throws IOException {
		File responseDataFolder =  null;
		if(siteId != null) {
			responseDataFolder = new File(outputFolder, "S_" + siteId + "_" + subjectId);
		} else {
			responseDataFolder = new File(outputFolder, "S_" + subjectId);
		}
		if(!responseDataFolder.exists()) {
			//Create the response data folder if it doesn't exist
			CPSFileUtils.makeDirectory(responseDataFolder);
		}		
		//new File(responseDataFolder, "");
		return responseDataFolder;
	}
	
	/**
	 * Formats the file name for files containing human subject response data for a task.
	 * File Name Format: S_siteId_subjectId_examId_examPhaseId.xml
	 * 
	 * @param siteId the site ID of the subject
	 * @param subjectId the subject ID of the subject
	 * @param examId the exam ID
	 * @param examPhaseId the exam phase ID 
	 * @return the file name
	 */
	public static String formatDataFileName(String siteId, String subjectId, String examId, String examPhaseId) {		
		if(siteId != null) {
			return "S_" + siteId + "_" + subjectId + "_" + examId + "_" + examPhaseId + ".xml";
		}
		else {
			return "S_" + subjectId + "_" + examId + "_" + examPhaseId + ".xml";
		}
	}
	
	/**
	 * If the subject Id is numeric, formats the number as a 3-character String (e.g., 1 becomes 001).
	 * 
	 * @param subjectId the subject ID to formats
	 * @return the formatted subject ID
	 */
	public static String formatSubjectId(String subjectId) {
		try {
			Long sid = Long.parseLong(subjectId);
			if(sid != null) {
				if(sid < 10) {
					return "00" + sid.toString();
				} else if (sid < 100) {
					return "0" + sid.toString();
				} 
				return subjectId;
			}
		} catch(NumberFormatException ex) {}
		return subjectId;
	}
}