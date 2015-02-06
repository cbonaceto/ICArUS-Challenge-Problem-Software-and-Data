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
package org.mitre.icarus.cps.assessment.survey.phase_2;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.mitre.icarus.cps.app.experiment.IcarusSubjectData;
import org.mitre.icarus.cps.assessment.assessment_processor.phase_2.BatchFileProcessor;
import org.mitre.icarus.cps.assessment.data_aggregator.DataAggregatorUtils;
import org.mitre.icarus.cps.assessment.survey.CsvSurveyResponseAggregator;
import org.mitre.icarus.cps.assessment.survey.ISurveyFileNameFormatter;
import org.mitre.icarus.cps.assessment.survey.ISurveyResponseProcessor;
import org.mitre.icarus.cps.assessment.survey.SurveyFileNameFormatterFormat1;
import org.mitre.icarus.cps.assessment.survey.SurveyFileNameFormatterFormat2;
import org.mitre.icarus.cps.assessment.survey.SurveyFileNameFormatterFormat3;
import org.mitre.icarus.cps.assessment.survey.pdf_tools.PDFSurveyResponseLoader;

/**
 * Contains static methods to aggregate survey responses from Phase 2 exams.
 * 
 * @author CBONACETO
 */
public class SurveyResponseAggregator_Phase2 {
    
    private SurveyResponseAggregator_Phase2() {}
    
    /**
     * Aggregates Sample Exam 1 survey data into a CSV file.
     */
    public static void aggregate_SampleExam_1_Surveys() {
        File dataDir = new File(BatchFileProcessor.assessmentFolder + "Sample-Exam-1/Human_Data/All Subjects");
        File outputDir = new File(BatchFileProcessor.assessmentFolder + "Sample-Exam-1/Human_Data/Aggregated Data");
        List<String> surveyNames = Arrays.asList("Pre-test-Questionnaire", "Pre-test Questionnaire");
        List<? extends ISurveyFileNameFormatter> surveyNameFormatters
                = Arrays.asList(new SurveyFileNameFormatterFormat1(), new SurveyFileNameFormatterFormat1());
        aggregateSurveyData(dataDir, outputDir, surveyNames, surveyNameFormatters,
                new PilotExamSurveyResponseProcessor());
    }

    /**
     * Aggregates Sample Exam 2 survey data into a CSV file.
     */
    public static void aggregate_SampleExam_2_Surveys() {
        File dataDir = new File(BatchFileProcessor.assessmentFolder + "Sample-Exam-2/Human_Data/All Subjects");
        File outputDir = new File(BatchFileProcessor.assessmentFolder + "Sample-Exam-2/Human_Data/Aggregated Data");
        List<String> surveyNames = Arrays.asList("Pre-test Questionnaire _ ICArUS Study _ Jul 2013 _ 000-1");
        List<? extends ISurveyFileNameFormatter> surveyNameFormatters
                = Arrays.asList(new SurveyFileNameFormatterFormat2());
        aggregateSurveyData(dataDir, outputDir, surveyNames, surveyNameFormatters,
                new PilotExamSurveyResponseProcessor());
    }

    /**
     * Aggregates Sample Exam 1 and Sample Exam 2 survey data into a CSV file.
     */
    public static void aggregate_SampleExam_1_2_Combined_Surveys() {
        File dataDir = new File(BatchFileProcessor.assessmentFolder + "Sample-Exam-1-2-Combined/Human_Data/All Subjects");
        File outputDir = new File(BatchFileProcessor.assessmentFolder + "Sample-Exam-1-2-Combined/Human_Data/Aggregated Data");
        List<String> surveyNames = Arrays.asList("Pre-test-Questionnaire", "Pre-test Questionnaire",
                "Pre-test Questionnaire _ ICArUS Study _ Jul 2013 _ 000-1");
        List<? extends ISurveyFileNameFormatter> surveyNameFormatters
                = Arrays.asList(new SurveyFileNameFormatterFormat1(), new SurveyFileNameFormatterFormat1(),
                        new SurveyFileNameFormatterFormat2());
        aggregateSurveyData(dataDir, outputDir, surveyNames, surveyNameFormatters,
                new PilotExamSurveyResponseProcessor());
    }
    
    /**
     * Aggregates Final Exam survey data into a CSV file.
     */
    public static void aggregate_FinalExam_1_Surveys() {
        File dataDir = new File(BatchFileProcessor.assessmentFolder + "Final-Exam-1/Human_Data/All Subjects");
        File outputDir = new File(BatchFileProcessor.assessmentFolder + "Final-Exam-1/Human_Data/Aggregated Data");
        List<String> surveyNames = Arrays.asList("Questionnaire", 
                "Participant Questionnaire _ ICArUS Study _ 2014");
        List<? extends ISurveyFileNameFormatter> surveyNameFormatters
                = Arrays.asList(new SurveyFileNameFormatterFormat1(), new SurveyFileNameFormatterFormat3());
        aggregateSurveyData(dataDir, outputDir, surveyNames, surveyNameFormatters,
                new FinalExamSurveyResponseProcessor());
    }
    
    /**
     * Aggregates Mission 1-5 Pilot Study survey data and creates CSV files
     * containing survey data.
     *
     * @param dataDir
     * @param outputDir
     * @param surveyNameVariations
     * @param surveyResponseProcessor
     * @param surveyFileNameFormatters
     */
    public static void aggregateSurveyData(File dataDir, File outputDir, List<String> surveyNameVariations,
            List<? extends ISurveyFileNameFormatter> surveyFileNameFormatters, 
            ISurveyResponseProcessor surveyResponseProcessor) {
        try {
            CsvSurveyResponseAggregator aggregator = new CsvSurveyResponseAggregator();
            File outputFileRaw = new File(outputDir, "all_survey_responses.csv");
            File outputFileProcessed = new File(outputDir, "processed_survey_responses.csv");

            //Get the subjects
            Set<IcarusSubjectData> subjects = DataAggregatorUtils.getSubjectsInFolder(
                    dataDir, true);

            //Create the CSV files
            Iterator<String> nameIter = surveyNameVariations.iterator();
            Iterator<? extends ISurveyFileNameFormatter> formatterIter = surveyFileNameFormatters.iterator();
            boolean append = false;
            while (nameIter.hasNext()) {
                String surveyName = nameIter.next();
                ISurveyFileNameFormatter surveyNameFormatter = formatterIter.next();
                aggregator.aggregateSurveyData(subjects, surveyName,
                        "000-1", "pdf", new PDFSurveyResponseLoader(), surveyResponseProcessor,
                        surveyNameFormatter, dataDir,
                        outputFileRaw, outputFileProcessed,
                        true, append, true);
                append = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
       //aggregate_SampleExam_1_Surveys();       
       //aggregate_SampleExam_2_Surveys();
       
       //aggregate_SampleExam_1_2_Combined_Surveys();
       
       aggregate_FinalExam_1_Surveys();
    }
}
