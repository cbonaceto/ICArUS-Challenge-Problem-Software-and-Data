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

import java.util.Map;

import org.mitre.icarus.cps.assessment.survey.AbstractSurveyResponseProcessor;
import org.mitre.icarus.cps.assessment.survey.SurveyResponse;

/**
 * @author CBONACETO
 *
 */
public class PilotExamSurveyResponseProcessor extends AbstractSurveyResponseProcessor {

    @Override
    public SurveyResponse_Phase2 processSurvey(SurveyResponse surveyResponse) {
        if (surveyResponse != null) {
            SurveyResponse_Phase2 processedResponse = new SurveyResponse_Phase2();
            copyBasicSurveyResponseData(surveyResponse, processedResponse);
            if (surveyResponse.getFields() != null && !surveyResponse.getFields().isEmpty()) {
                Map<String, String> fields = createSurveyFieldsMap(surveyResponse);

                //Get the age
                String age = fields.get("age");
                if (age != null && !age.isEmpty()) {
                    try {
                        processedResponse.setAge(Integer.parseInt(age));
                    } catch (Exception ex) {
                    }
                }

                //Get the gender
                processedResponse.setGender(fields.get("gender"));
                
                //Get the degrees earned
                
                //Get the highest degree earned
                
                //Get the jobs

                //Get the total number of years of experience               
                Double yearsExperience = 0d;
                for (int i = 1; i <= 10; i++) {
                    //Number of YearsRow4
                    String years = fields.get("Number of YearsRow" + Integer.toString(i));
                    if (years != null && !years.isEmpty()) {
                        try {
                            yearsExperience += Double.parseDouble(years);
                        } catch (Exception ex) {
                        }
                    }
                }
                processedResponse.setYearsExperience(yearsExperience);
                
                //Get the number of years of intel analysis or geospatial analysis experience
                
                
                //Get the number of years of military experience
                
                
                //Get the last military branch served in
                
                
                //Get the level of probability and statistics training
                String probsAndStatsLevel = fields.get("training_probs_stats");
                if (probsAndStatsLevel != null) {
                    processedResponse.setProbsAndStatsTraining(letterToIntegerLevel(probsAndStatsLevel));
                }

                //Compute the BIS score
                Integer bis = computeBisScore(fields, "Bis_bas");
                processedResponse.setBis(bis);

                //Compute the BAS Drive score
                Integer basDrive = computeBasDriveScore(fields, "Bis_bas");
                processedResponse.setBasDrive(basDrive);

                //Compute the BAS Fun Seeking score
                Integer basFunSeeking = computeBasFunSeekingScore(fields, "Bis_bas");
                processedResponse.setBasFunSeeking(basFunSeeking);

                //Compute the BAS Reward Responsiveness score
                Integer basRewardResponsiveness = computeBasRewardResponsivenessScore(
                        fields, "Bis_bas");
                processedResponse.setBasRewardResponsiveness(basRewardResponsiveness);

                //Compute the BAS Total score
                processedResponse.setBasTotal(computeBasTotalScore(basDrive,
                        basFunSeeking, basRewardResponsiveness));
               
                //Compute the Fox/Hedgehog score
                
                //Compute the Cognitive Reflections Test score
               
            }
            return processedResponse;
        } else {
            return null;
        }
    }

    /**
     * @param letter
     */
    protected Integer letterToIntegerLevel(String letter) {
        if (letter != null && letter.length() > 0) {
            return letter.toLowerCase().charAt(0) - 96;
        }
        return null;
    }

    /**
     * @param fields
     * @return
     */
    protected int computeBisScore(Map<String, String> fields, String bisBasfieldName) {
        //BIS Questions: 2, 8, 13, 16, 19, 22, 24
        //Note: Questions 2 and 22 are not reverse-scored
        int[] bisQuestions = {2, 8, 13, 16, 19, 22, 24};
        int sum = 0;
        for (int question : bisQuestions) {
            String value = fields.get(bisBasfieldName + "_" + question);
            Integer level = letterToIntegerLevel(value);
            if (level != null) {
                if (question != 2 && question != 22) {
                    //Reverse-score the question
                    switch (level) {
                        case 1:
                            level = 4;
                            break;
                        case 2:
                            level = 3;
                            break;
                        case 3:
                            level = 2;
                            break;
                        case 4:
                            level = 1;
                            break;
                    }
                }
                sum += level;
            }
        }
        return sum;
    }

    /**
     * @param fields
     * @return
     */
    protected int computeBasDriveScore(Map<String, String> fields,
            String bisBasfieldName) {
        //BAS Drive Questions: 3, 9, 12, 21
        int[] basDriveQuestions = {3, 9, 12, 21};
        int sum = 0;
        for (int question : basDriveQuestions) {
            String value = fields.get(bisBasfieldName + "_" + question);
            Integer level = letterToIntegerLevel(value);
            if (level != null) {
                //Reverse-score the question
                switch (level) {
                    case 1:
                        level = 4;
                        break;
                    case 2:
                        level = 3;
                        break;
                    case 3:
                        level = 2;
                        break;
                    case 4:
                        level = 1;
                        break;
                }
                sum += level;
            }
        }
        return sum;
    }

    /**
     * @param fields
     * @return
     */
    protected int computeBasFunSeekingScore(Map<String, String> fields,
            String bisBasfieldName) {
        //BAS Fun Seeking Questions: 5, 10, 15, 20
        int[] basFunSeekingQuestions = {5, 10, 15, 20};
        int sum = 0;
        for (int question : basFunSeekingQuestions) {
            String value = fields.get(bisBasfieldName + "_" + question);
            Integer level = letterToIntegerLevel(value);
            if (level != null) {
                //Reverse-score the question
                switch (level) {
                    case 1:
                        level = 4;
                        break;
                    case 2:
                        level = 3;
                        break;
                    case 3:
                        level = 2;
                        break;
                    case 4:
                        level = 1;
                        break;
                }
                sum += level;
            }
        }
        return sum;
    }

    /**
     * @param fields
     * @return
     */
    protected int computeBasRewardResponsivenessScore(Map<String, String> fields,
            String bisBasfieldName) {
        //BAS Reward Responsiveness Questions: 4, 7, 14, 18, 23
        int[] basRewardResponsivenessQuestions = {4, 7, 14, 18, 23};
        int sum = 0;
        for (int question : basRewardResponsivenessQuestions) {
            String value = fields.get(bisBasfieldName + "_" + question);
            Integer level = letterToIntegerLevel(value);
            if (level != null) {
                //Reverse-score the question
                switch (level) {
                    case 1:
                        level = 4;
                        break;
                    case 2:
                        level = 3;
                        break;
                    case 3:
                        level = 2;
                        break;
                    case 4:
                        level = 1;
                        break;
                }
                sum += level;
            }
        }
        return sum;
    }

    /**
     * @param basDrive
     * @param basFunSeeking
     * @param basRewardResponsiveness
     * @return
     */
    protected Integer computeBasTotalScore(Integer basDrive, Integer basFunSeeking,
            Integer basRewardResponsiveness) {
        //Bas Total Score: Sum of Drive, Fun Seeking, and Reward Responsiveness
        int sum = 0;
        sum += basDrive != null ? basDrive : 0;
        sum += basFunSeeking != null ? basFunSeeking : 0;
        sum += basRewardResponsiveness != null ? basRewardResponsiveness : 0;
        return sum;
    }
}