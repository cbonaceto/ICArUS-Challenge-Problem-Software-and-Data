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
public class SurveyField {

    /**
     * Field types
     */
    public static enum FieldType {
        NumericValue, TextValue, SingleOptionSelection,
        MultiOptionSelection, Other
    }

    /**
     * The field type
     */
    protected FieldType fieldType;

    /**
     * The field name
     */
    protected String name;

    /**
     * The field value
     */
    protected String value;

    public SurveyField() {
    }

    public SurveyField(FieldType fieldType) {
        this.fieldType = fieldType;
    }

    public SurveyField(FieldType fieldType, String name) {
        this.fieldType = fieldType;
        this.name = name;
    }

    public FieldType getFieldType() {
        return fieldType;
    }

    public void setFieldType(FieldType fieldType) {
        this.fieldType = fieldType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
