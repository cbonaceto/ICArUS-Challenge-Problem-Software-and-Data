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
package org.mitre.icarus.cps.assessment.survey;

/**
 * @author CBONACETO
 *
 */
public class SurveyFileNameFormatterFormat1 implements ISurveyFileNameFormatter {

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.assessment.survey.ISurveyFileNameFormatter#formatSurveyFileName(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String formatSurveyFileName(String surveyName, String fileType,
			String siteId, String subjectId) {
		//File Format: surveyName_subjectId.fileType
		//Example: Pre-test-Questionnaire_001.pdf
		StringBuilder sb = new StringBuilder(surveyName);
		sb.append("_");
		sb.append(subjectId);
		sb.append(".");
		sb.append(fileType);
		return sb.toString();		
	}
}