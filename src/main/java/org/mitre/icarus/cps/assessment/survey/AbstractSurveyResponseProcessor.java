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

import java.util.HashMap;
import java.util.Map;

/**
 * @author CBONACETO
 *
 */
public abstract class AbstractSurveyResponseProcessor implements ISurveyResponseProcessor {

    /**
     * @param originalResponse
     * @param processedResponse
     */
    public void copyBasicSurveyResponseData(SurveyResponse originalResponse, SurveyResponse processedResponse) {
        if (originalResponse != null && processedResponse != null) {
            processedResponse.setName(originalResponse.getName());
            processedResponse.setId(originalResponse.getId());
            processedResponse.setSubject(originalResponse.getSubject());
            processedResponse.setCompletionTime(originalResponse.getCompletionTime());
        }
    }

    /**
     * @param response
     * @return
     */
    public Map<String, String> createSurveyFieldsMap(SurveyResponse response) {
        if (response != null && response.getFields() != null && !response.getFields().isEmpty()) {
            HashMap<String, String> fieldsMap = new HashMap<String, String>(
                    response.getFields().size());
            for (SurveyField field : response.getFields()) {
                fieldsMap.put(field.getName(), field.getValue());
            }
            return fieldsMap;
        }
        return null;
    }
}
