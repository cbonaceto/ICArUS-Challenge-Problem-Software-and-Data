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
package org.mitre.icarus.cps.assessment.survey.pdf_tools;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDCheckbox;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDRadioCollection;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextbox;
import org.apache.pdfbox.pdmodel.interactive.form.PDVariableText;
import org.mitre.icarus.cps.app.experiment.IcarusSubjectData;
import org.mitre.icarus.cps.assessment.assessment_processor.phase_2.BatchFileProcessor;
import org.mitre.icarus.cps.assessment.survey.ISurveyResponseLoader;
import org.mitre.icarus.cps.assessment.survey.SurveyField;
import org.mitre.icarus.cps.assessment.survey.SurveyField.FieldType;
import org.mitre.icarus.cps.assessment.survey.SurveyResponse;

/**
 * @author CBONACETO
 *
 */
public class PDFSurveyResponseLoader implements ISurveyResponseLoader {

    @Override
    public SurveyResponse loadSurveyResponse(String surveyName,
            String surveyId, IcarusSubjectData subject, URL surveyResponseUrl) throws Exception {
        SurveyResponse surveyResponse = new SurveyResponse();
        surveyResponse.setName(surveyName);
        surveyResponse.setId(surveyId);
        surveyResponse.setSubject(subject);
        surveyResponse.setFields(loadSurveyResponseFields(surveyResponseUrl));
        return surveyResponse;
    }

    /**
     * @param surveyResponseUrl
     * @return
     * @throws IOException
     */
    public List<SurveyField> loadSurveyResponseFields(URL surveyResponseUrl) throws IOException {
        return loadSurveyResponseFields(surveyResponseUrl, false);
    }

    /**
     * @param surveyResponseUrl
     * @param displayFieldsToConsole
     * @return
     * @throws IOException
     */
    public List<SurveyField> loadSurveyResponseFields(URL surveyResponseUrl, boolean displayFieldsToConsole) throws IOException {
        PDDocument pdf = null;
        try {
            pdf = PDDocument.load(surveyResponseUrl);
            if (pdf != null) {
                List<SurveyField> surveyFields = new LinkedList<SurveyField>();
                PDDocumentCatalog docCatalog = pdf.getDocumentCatalog();
                PDAcroForm acroForm = docCatalog.getAcroForm();
                List<?> fields = acroForm.getFields();
                if (fields != null && !fields.isEmpty()) {
                    Iterator<?> fieldsIter = fields.iterator();
                    while (fieldsIter.hasNext()) {
                        PDField field = (PDField) fieldsIter.next();
                        List<SurveyField> currSurveyFields = processField(field);
                        if (currSurveyFields != null && !currSurveyFields.isEmpty()) {
                            surveyFields.addAll(currSurveyFields);
                        }
                        if (displayFieldsToConsole) {
                            displayField(field, "|--", field.getPartialName());
                        }
                    }
                }
                return surveyFields;
            } else {
                return null;
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if(pdf != null) {
                pdf.close();
            }
        }
    }

    /**
     * @param field
     * @return
     * @throws IOException
     */
    private List<SurveyField> processField(PDField field) throws IOException {
        if (field instanceof PDRadioCollection) {
            SurveyField surveyField = new SurveyField(FieldType.MultiOptionSelection);
            surveyField.setName(field.getPartialName());
            List<?> kids = field.getKids();
            StringBuilder values = new StringBuilder();
            if (kids != null && !kids.isEmpty()) {
                Iterator<?> kidsIter = kids.iterator();
                boolean valueAppended = false;
                while (kidsIter.hasNext()) {
                    Object pdfObj = kidsIter.next();
                    if (pdfObj instanceof PDCheckbox) {
                        PDCheckbox checkBox = (PDCheckbox) pdfObj;
                        if (checkBox.isChecked()) {
                            if (valueAppended) {
                                values.append(",");
                            } else {
                                valueAppended = true;
                            }
                            values.append(checkBox.getOnValue());
                        }
                    }
                }
            }
            surveyField.setValue(values.toString());
            return Arrays.asList(surveyField);
        } else {
            List<?> kids = field.getKids();
            if (kids != null && !kids.isEmpty()) {
                List<SurveyField> surveyFields = new LinkedList<SurveyField>();
                Iterator<?> kidsIter = kids.iterator();
                while (kidsIter.hasNext()) {
                    Object pdfObj = kidsIter.next();
                    if (pdfObj instanceof PDField) {
                        PDField kid = (PDField) pdfObj;
                        surveyFields.addAll(processField(kid));
                    }
                }
                return surveyFields;
            } else {
                SurveyField surveyField = new SurveyField();
                if (field instanceof PDCheckbox) {
                    PDCheckbox checkBox = (PDCheckbox) field;
                    surveyField.setFieldType(FieldType.SingleOptionSelection);
                    surveyField.setName(checkBox.getPartialName());
                    surveyField.setValue(checkBox.isChecked() ? checkBox.getOnValue() : checkBox.getOffValue());
					//surveyField.setName(checkBox.getOnValue());					
                    //surveyField.setValue(checkBox.isChecked() ? "Checked" : "Unchecked");
                } else {
                    if (field instanceof PDTextbox || field instanceof PDVariableText) {
                        surveyField.setFieldType(FieldType.TextValue);
                    } else {
                        surveyField.setFieldType(FieldType.Other);
                    }
                    surveyField.setName(field.getPartialName());
                    surveyField.setValue(field.getValue());
                }
                return Arrays.asList(surveyField);
            }
        }
    }

    /**
     * @param field
     * @param sLevel
     * @param sParent
     * @throws IOException
     */
    private void displayField(PDField field, String sLevel, String sParent) throws IOException {
        List<?> kids = field.getKids();
        if (kids != null && !kids.isEmpty()) {
            Iterator<?> kidsIter = kids.iterator();
            if (!sParent.equals(field.getPartialName())) {
                sParent = sParent + "." + field.getPartialName();
            }
            System.out.println(sLevel + sParent + ", type = " + field.getClass().getSimpleName());
            while (kidsIter.hasNext()) {
                Object pdfObj = kidsIter.next();
                if (pdfObj instanceof PDField) {
                    PDField kid = (PDField) pdfObj;
                    displayField(kid, "|  " + sLevel, sParent);
                }
            }
        } else {
            StringBuilder sb = new StringBuilder(sLevel + sParent);
            if (field.getPartialName() != null) {
                sb.append("." + field.getPartialName());
            }
            if (field instanceof PDCheckbox) {
                PDCheckbox checkBox = (PDCheckbox) field;
                sb.append("." + checkBox.getOnValue() + " = ");
                sb.append(checkBox.isChecked() ? "Checked" : "Unchecked");
            } else {
                sb.append(" = ");
                sb.append(field.getValue());
            }
            sb.append(",  type=" + field.getClass().getSimpleName());
			//String outputString = sLevel + sParent + "." + field.getPartialName() + " = " + value +
            //		",  type=" + field.getClass().getName();			
            System.out.println(sb.toString());

        }
    }

    public static void main(String[] args) {
        try {
            PDFSurveyResponseLoader surveyLoader = new PDFSurveyResponseLoader();
            //File surveyFile = new File(BatchFileProcessor.assessmentFolder + "Sample-Exam-2/Human_Data/All Subjects/S_PSU_021/"
            //              + "Pre-test Questionnaire _ ICArUS Study _ Jul 2013 _ 000-1.tester.021.pdf");
            File surveyFile = new File(BatchFileProcessor.assessmentFolder + "Final-Exam-1/Human_Data/All Subjects/S_MCL_210/"
                          + "Participant Questionnaire _ ICArUS Study _ 2014 _ 210.pdf");
            List<SurveyField> fields = surveyLoader.loadSurveyResponseFields(
                    surveyFile.toURI().toURL(), false);
            for (SurveyField field : fields) {
                System.out.println(field.getName() + " = " + field.getValue() + ", type = " + field.getFieldType());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
