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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.mitre.icarus.cps.app.experiment.IcarusSubjectData;
import org.mitre.icarus.cps.app.experiment.data_recorder.SubjectDataRecorderUtils;

/**
 * @author CBONACETO
 *
 */
public class CsvSurveyResponseAggregator {

    /**
     * @param subjects
     * @param surveyName
     * @param surveyId
     * @param fileType
     * @param surveyResponseLoader
     * @param surveyResponseProcessor
     * @param surveyFileNameFormatter
     * @param dataDir
     * @param unprocessedFile
     * @param processedFile
     * @param outputUnprocessedSurveyResponses
     * @param appending
     * @param outputProgress
     * @throws IOException
     */
    public void aggregateSurveyData(Set<IcarusSubjectData> subjects,
            String surveyName, String surveyId, String fileType,
            ISurveyResponseLoader surveyResponseLoader,
            ISurveyResponseProcessor surveyResponseProcessor,
            ISurveyFileNameFormatter surveyFileNameFormatter,
            File dataDir, File unprocessedFile, File processedFile,
            boolean outputUnprocessedSurveyResponses,
            boolean appending, boolean outputProgress) throws IOException {
        if (outputProgress) {
            System.out.println("Aggregating data for survey " + surveyName);
        }
        FileWriter unprocessedSurveyWriter = null;
        FileWriter processedSurveyWriter = null;
        try {
            //Create the CSV file for the unprocessed survey data
            if (outputUnprocessedSurveyResponses) {
                unprocessedSurveyWriter = new FileWriter(unprocessedFile, appending);
            }

            //Create the CSV file for the processed survey data
            processedSurveyWriter = new FileWriter(processedFile, appending);

            boolean firstSubject = true;
            for (IcarusSubjectData subject : subjects) {
                //Load the survey response for the current subject
                String siteId = subject.getSite() != null ? subject.getSite().getTag() : null;
                String subjectId = subject.getSubjectId();
                File file = new File(dataDir, "/" + SubjectDataRecorderUtils.formatSubjectFolderName(subject) + "/"
                        + surveyFileNameFormatter.formatSurveyFileName(surveyName, fileType,
                                siteId, subjectId));
                SurveyResponse surveyResponse = null;
                if (file.exists()) {
                    try {
                        surveyResponse = surveyResponseLoader.loadSurveyResponse(surveyName, surveyId,
                                subject, file.toURI().toURL());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                if (surveyResponse != null && surveyResponse.getFields() != null
                        && !surveyResponse.getFields().isEmpty()) {
                    if (outputProgress) {
                        System.out.println("Processing survey data for Subject: " + subject.getSubjectId());
                    }

                    //Write the raw survey data to a CSV file
                    if (outputUnprocessedSurveyResponses) {
                        if (firstSubject && !appending) {
                            writeCsvFileHeader(surveyResponse.getFields(), unprocessedSurveyWriter);
                        }
                        unprocessedSurveyWriter.append("\n");
                        writeCsvFileRow(surveyResponse.getFields(), siteId, subjectId, unprocessedSurveyWriter);
                    }

                    //Process the survey data for the current subject and write the data to the CSV file
                    if (surveyResponseProcessor != null) {
                        SurveyResponse processedResponse = surveyResponseProcessor.processSurvey(surveyResponse);
                        if (firstSubject && !appending) {
                            writeCsvFileHeader(processedResponse.getFields(), processedSurveyWriter);
                        }
                        processedSurveyWriter.append("\n");
                        writeCsvFileRow(processedResponse.getFields(), siteId, subjectId, processedSurveyWriter);
                    }

                    firstSubject = false;
                }
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (unprocessedSurveyWriter != null) {
                unprocessedSurveyWriter.flush();
                unprocessedSurveyWriter.close();
            }
            if (processedSurveyWriter != null) {
                processedSurveyWriter.flush();
                processedSurveyWriter.close();
            }
        }
        if (outputProgress) {
            System.out.println("Finished aggregating data for survey " + surveyName);
        }
    }

    /**
     * @param fields
     * @param fw
     * @throws IOException
     */
    protected void writeCsvFileHeader(List<SurveyField> fields, FileWriter fw) throws IOException {
        if (fields != null && !fields.isEmpty()) {
            //int index = 0;
            fw.write("site_id,subject_id");
            for (SurveyField field : fields) {
                fw.write(",");
                //Replaces any commas with spaces in the field name before writing the name
                fw.write(field.getName().replace(',', ' '));
            }
        }
    }

    /**
     * @param fields
     * @param fw
     * @throws IOException
     */
    protected void writeCsvFileRow(List<SurveyField> fields, String siteId, String subjectId,
            FileWriter fw) throws IOException {
        if (fields != null && !fields.isEmpty()) {
            fw.write(siteId + "," + subjectId);
            for (SurveyField field : fields) {
                fw.write(",");
                //Replaces any commas with spaces in the field value before writing the value
                fw.write(field.getValue() != null ? field.getValue().replace(',', ' ') : "");
            }
        }
    }    
}
