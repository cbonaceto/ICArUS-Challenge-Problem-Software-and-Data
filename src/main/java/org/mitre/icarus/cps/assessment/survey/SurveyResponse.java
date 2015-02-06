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

import java.util.List;

import org.mitre.icarus.cps.app.experiment.IcarusSubjectData;

/**
 * @author CBONACETO
 *
 */
public class SurveyResponse {

    /**
     * The survey name
     */
    protected String name;

    /**
     * The survey ID
     */
    protected String id;

    /**
     * The subject who took the survey
     */
    protected IcarusSubjectData subject;

    /**
     * The survey fields
     */
    protected List<SurveyField> fields;

    /**
     * The completion time (in milliseconds since 1970 format)
     */
    protected Long completionTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public IcarusSubjectData getSubject() {
        return subject;
    }

    public void setSubject(IcarusSubjectData subject) {
        this.subject = subject;
    }

    public List<SurveyField> getFields() {
        return fields;
    }

    public void setFields(List<SurveyField> fields) {
        this.fields = fields;
    }

    public Long getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(Long completionTime) {
        this.completionTime = completionTime;
    }
}
