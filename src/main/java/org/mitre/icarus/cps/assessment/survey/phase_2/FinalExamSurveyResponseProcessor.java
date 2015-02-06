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

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.mitre.icarus.cps.assessment.survey.SurveyResponse;
import org.mitre.icarus.cps.assessment.survey.phase_2.AnswerTypes.ExpertiseLevelType;
import org.mitre.icarus.cps.assessment.survey.phase_2.AnswerTypes.FrequencyType;
import org.mitre.icarus.cps.assessment.survey.phase_2.AnswerTypes.SpatialAbilityType;
import org.mitre.icarus.cps.assessment.survey.phase_2.AnswerTypes.TrainingLevelType;

/**
 * Processes survey data for the Phase 2 Final Exam survey format.
 * 
 * @author CBONACETO
 */
public class FinalExamSurveyResponseProcessor extends PilotExamSurveyResponseProcessor {

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

                //Get the degrees earned and highest degree earned
                EducationalDegreeType highestDegree = null;
                String degree = fields.get("education_associate");
                if(degree != null && degree.equals("On")) {
                    highestDegree = EducationalDegreeType.Associate;
                    processedResponse.setAssociatesDegreeDiscipline(fields.get("education_associate_discipline"));
                }
                degree = fields.get("education_bachelor");
                if(degree != null && degree.equals("On")) {
                    highestDegree = EducationalDegreeType.Bachelor;
                    processedResponse.setBachelorsDegreeDiscipline(fields.get("education_bachelor_discipline"));
                }
                degree = fields.get("education_certificate");
                if(degree != null && degree.equals("On")) {
                    if (highestDegree == null) {
                        highestDegree = EducationalDegreeType.Certificate;
                    }
                    processedResponse.setCertificateDegreeDiscipline(fields.get("education_certificate_discipline"));
                }
                degree = fields.get("education_master");
                if(degree != null && degree.equals("On")) {
                    highestDegree = EducationalDegreeType.Master;
                    processedResponse.setMastersDegreeDiscipline(fields.get("education_master_discipline"));
                }
                degree = fields.get("education_phd");
                if(degree != null && degree.equals("On")) {
                    highestDegree = EducationalDegreeType.PhD;
                    processedResponse.setPhdDegreeDiscipline(fields.get("education_phd_discipline"));
                }
                processedResponse.setHighestDegree(highestDegree);
                
                //Get the current degree program and discipline
                degree = fields.get("education_current_degree");
                if (degree != null && !degree.isEmpty()) {
                    processedResponse.setCurrentDegree(classifyDegree(degree));
                }
                processedResponse.setCurrentDegreeDiscipline(
                        fields.get("education_current_discipline"));
                
                //Get the jobs, the total number of years of experience, and
                //the number of years of intel analysis or geospatial analysis experience             
                List<Job> jobs = new LinkedList<Job>();
                Double yearsExperience = 0d;
                Double yearsGeoIntExperience = 0d;
                for (int i = 1; i <= 10; i++) {
                    String employer = fields.get("employer_" + Integer.toString(i));
                    if (employer != null && !employer.isEmpty()) {
                        Job job = new Job();
                        jobs.add(job);
                        job.setEmployer(employer);
                        String title = fields.get("job_title_position_" + Integer.toString(i));
                        job.setTitle(title);
                        JobType jobType = classifyJob(employer, title);
                        job.setJobType(jobType);
                        String years = fields.get("number_years_" + Integer.toString(i));
                        if (years != null && !years.isEmpty()) {                           
                            try {
                                Double numYears = Double.parseDouble(years);
                                job.setYears(numYears);
                                yearsExperience += numYears;
                                if(jobType == JobType.IntelAnalysis || jobType == JobType.GeoIntAnalysis) {
                                    yearsGeoIntExperience += numYears;
                                }
                            } catch (Exception ex) {
                            }
                        }                       
                    } else {
                        jobs.add(null);
                    }
                }
                processedResponse.setJobs(jobs);
                processedResponse.setYearsExperience(yearsExperience);
                processedResponse.setYearsGeoIntExperience(yearsGeoIntExperience);
                
                //Get the level of probability and statistics training
                String value = fields.get("probability_statistics_training");
                if (value != null && !value.isEmpty()) {
                    TrainingLevelType probsAndStatsLevel = TrainingLevelType.valueOf(
                            value.toLowerCase());
                    processedResponse.setProbsAndStatsTraining(
                            probsAndStatsLevel != null ? probsAndStatsLevel.ordinal() : null);
                }
                
                //Get the frequency of probability and statistics usage
                value = fields.get("probability_statistics_usage");
                if (value != null && !value.isEmpty()) {
                    FrequencyType probsAndStatsUsage = FrequencyType.valueOf(
                            value.toLowerCase());
                    processedResponse.setProbsAndStatsUsageFrequency(
                            probsAndStatsUsage != null ? probsAndStatsUsage.ordinal() : null);
                }
                
                //Get the frequency of geospatial data usage
                value = fields.get("geospatial_data_usage");
                if (value != null && !value.isEmpty()) {
                    FrequencyType geospatialDataUsage = FrequencyType.valueOf(
                            value.toLowerCase());
                    processedResponse.setGeoDataUsageFrequency(
                            geospatialDataUsage != null ? geospatialDataUsage.ordinal() : null);
                }                
                
                //Get the level of expertise in geospatial intelligence analsyis
                value = fields.get("geospatial_analysis_expertise");
                if (value != null && !value.isEmpty()) {
                    ExpertiseLevelType geointExpertise = ExpertiseLevelType.valueOf(
                            value.toLowerCase());
                    processedResponse.setGeoIntAnalysisLevel(
                            geointExpertise != null ? geointExpertise.ordinal() : null);
                } 
                
                //Get the spatial ability rating
                value = fields.get("spatial_ability_rating");
                if (value != null && !value.isEmpty()) {
                    SpatialAbilityType spatialAbility = SpatialAbilityType.valueOf(
                            value.toLowerCase());
                    processedResponse.setSpatialAbility(
                            spatialAbility != null ? spatialAbility.ordinal() : null);
                }                 

                //Compute the BIS score
                Integer bis = computeBisScore(fields, "bis-bas");
                processedResponse.setBis(bis);

                //Compute the BAS Drive score
                Integer basDrive = computeBasDriveScore(fields, "bis-bas");
                processedResponse.setBasDrive(basDrive);

                //Compute the BAS Fun Seeking score
                Integer basFunSeeking = computeBasFunSeekingScore(fields, "bis-bas");
                processedResponse.setBasFunSeeking(basFunSeeking);

                //Compute the BAS Reward Responsiveness score
                Integer basRewardResponsiveness = computeBasRewardResponsivenessScore(
                        fields, "bis-bas");
                processedResponse.setBasRewardResponsiveness(basRewardResponsiveness);

                //Compute the BAS Total score
                processedResponse.setBasTotal(computeBasTotalScore(basDrive,
                        basFunSeeking, basRewardResponsiveness));

                //Compute the Fox/Hedgehog score
                processedResponse.setFoxHedgehogScore(computeFoxHedgehogScore(fields));
           
                //Compute Santa Barbara Sense of Direction Test (SBSDT) score
                processedResponse.setSbsdtScore(computeSbsdtScore(fields));
                
                //Compute the Water Level Test score
                processedResponse.setWltScore(computeWltScore(fields));
                
                //Compute the Cognitive Reflections Test score
                processedResponse.setCrtScore(computeCrtScore(fields));
                
                //Compute Video Game Experience
                processedResponse.setVge(computeVgeScore(fields));
            }
            return processedResponse;
        } else {
            return null;
        }
    }
    
    /**
     * Attempt to determine the degree type.
     * 
     * @param degree
     * @return 
     */
    protected EducationalDegreeType classifyDegree(String degree) {
        if(degree != null && !degree.isEmpty()) {
            degree = degree.toLowerCase().trim();
            if(degree.contains("assoc")) {
                return EducationalDegreeType.Associate;
            } else if (degree.contains("bach") || degree.contains("under") ||
                    degree.contains("b.s.") || degree.contains("bs")) {
                return EducationalDegreeType.Bachelor;
            } else if (degree.contains("cert")) {
                return EducationalDegreeType.Certificate;
            } else if (degree.contains("mast") || degree.contains("m.s.")
                    || degree.contains("ms") || degree.contains("grad")
                    || degree.contains("mps")) {
                return EducationalDegreeType.Master;
            } else if (degree.contains("ph")) {
                return EducationalDegreeType.PhD;
            } else {
                return null;
            }
        }
        return null;
    }
    
    /**
     * Attempt to determine the job type based on the employer and/or job title.
     * 
     * @param employer
     * @param title
     * @return
     */
    protected JobType classifyJob(String employer, String title) {
        JobType jobType = null;
        if (employer != null && !employer.isEmpty() 
                && title != null && !title.isEmpty()) {
            title = title.toLowerCase().trim();
            employer = employer.toLowerCase().trim();
            if (title.contains("intel")) {
                if (title.contains("geo") || title.contains("gis")) {
                    jobType = JobType.GeoIntAnalysis;
                } else {
                    jobType = JobType.IntelAnalysis;
                }
            } else if (title.contains("geo") || title.contains("gis")) {
                jobType = JobType.GeospatialAnalysis;
            } else if (employer.contains("air force") || employer.contains("army")
                    || employer.contains("usaf") || employer.contains("marines") 
                    || employer.contains("navy")) {
                jobType = JobType.Military;
            } else {
                jobType = JobType.Other;
            }
        }
        return jobType;
    }

    /**
     * Compute the Fox/Hedgehog score.
     * 
     * @param fields
     * @return
     */
    protected int computeFoxHedgehogScore(Map<String, String> fields) {
        int score = 0;
        int[] pointValues = {-3, -5, 4, -5, -2, 5, -6, -4, 5, -3, 4, 1};
        for (int i = 1; i <= 12; i++) {
            String value = fields.get("cognitive_style_" + Integer.toString(i));
            if (value != null && !value.isEmpty()) {
                boolean agree = value.equalsIgnoreCase("agree");
                if (agree) {
                    score += pointValues[i - 1];
                } else {
                    score -= pointValues[i - 1];
                }
            }
        }
        String value = fields.get("cognitive_style_13");
        if (value != null && !value.isEmpty()) {
            if (value.equalsIgnoreCase("fox")) {
                score += 7;
            } else {
                score -= 7;
            }
        }
        return score;
    }
    
    /**
     * Compute the Santa Barbara Sense of Direction Test (SBSDT) score.
     * 
     * @param fields
     * @return
     */
    protected double computeSbsdtScore(Map<String, String> fields) {
        double score = 0;
        int numValues = 0;
        
        int[][] pointValues = {
            {3, 2, 1, 0, -1, -2, -3},
            {-3, -2, -1, 0, 1, 2, 3},
            {3, 2, 1, 0, -1, -2, -3},
            {3, 2, 1, 0, -1, -2, -3},
            {3, 2, 1, 0, -1, -2, -3},
            {-3, -2, -1, 0, 1, 2, 3},
            {3, 2, 1, 0, -1, -2, -3},
            {-3, -2, -1, 0, 1, 2, 3},
            {3, 2, 1, 0, -1, -2, -3},
            {-3, -2, -1, 0, 1, 2, 3},
            {-3, -2, -1, 0, 1, 2, 3},
            {-3, -2, -1, 0, 1, 2, 3},
            {-3, -2, -1, 0, 1, 2, 3},
            {-3, -2, -1, 0, 1, 2, 3},
            {3, 2, 1, 0, -1, -2, -3},
            {-3, -2, -1, 0, 1, 2, 3},
        };
        
        for (int i = 1; i <= pointValues.length; i++) {
            String value = fields.get("spatial_ability_" + Integer.toString(i));
            if (value != null && !value.isEmpty()) {
                int level = classifyAgreementLevel(value);
                if (level >= 0) {
                    score += pointValues[i-1][level];
                    numValues++;
                }                
            }
        }        
        return score / numValues;
    }      
    
    /**
     * Return a numeric value for the given level of agreement in 
     * string form (e.g., "slightly_agree" = 2).
     * 
     * @param level
     * @return
     */
    protected int classifyAgreementLevel(String level) {
        if(level.equalsIgnoreCase("strongly_agree")) {
            return 0;
        } else if(level.equalsIgnoreCase("agree")) {
            return 1;
        } else if(level.equalsIgnoreCase("slightly_agree")) {
            return 2;
        } else if(level.equalsIgnoreCase("no_agree_disagree")) {
            return 3;
        } else if(level.equalsIgnoreCase("slightly_disagree")) {
            return 4;
        } else if(level.equalsIgnoreCase("disagree")) {
            return 5;
        } else if(level.equalsIgnoreCase("strongly_disagree")) {
            return 6;
        } else {
            return -1;
        }
    }
                
    /**
     * Compute the Water Level Test score.
     * 
     * @param fields
     * @return
     */
    protected double computeWltScore(Map<String, String> fields) {
   
        // 0 = correct, 1 = over, -1 = under,  -.033 = slightly under rotated,
        // -0.67 = very under rotated, 0.33 = slightly over rotated, 
        // 0.67 = very over rotated        
        double[][] wltValues = {
            {-0.67, 0.33, 0, -0.33, -1, 0.67, 1},
            {0.67, -0.33, 1, 0.33, -0.67, -1, 0},
            {-0.67, -1, 0.33, -0.33, 0, 1, 0.67},
            {1, -0.33, 0.67, 0, -1, -0.67, 0.33},
            {0, 0.67, -0.33, -1, -0.67, 0.33, 1},
            {-0.67, 0.33, 0.67, -1, -0.33, 0, 1}
        };               
        
        //For now, just compute the total number correct
        double numCorrect = 0;
        for (int i = 1; i <= wltValues.length; i++) {           
            String value = fields.get("water_test_" + Integer.toString(i));
            if (value != null && !value.isEmpty()) {
                int selection = classifyWaterTestSelection(value);
                if (selection >= 0) {                    
                    if (wltValues[i-1][selection] == 0) {
                        numCorrect++;
                    }
                }                
            }
        }        
        return numCorrect;
    }
    
     /**
     * Return a numeric value for the given water test selection
     * (e.g., 'a' = 0, 'g' = 6).
     * 
     * @param selection
     * @return
     */
    protected int classifyWaterTestSelection(String selection) {
        if (selection != null && !selection.isEmpty()) {
            return (int)selection.charAt(0) - 97;
        } else {
            return -1;
        }
    }

    /**
     * Compute number of correct answers to the Cognitive Reflections Test
     * questions.
     *
     * @param fields
     * @return
     */
    protected int computeCrtScore(Map<String, String> fields) {
        int numCorrect = 0;

        Integer cents = null;
        if (fields.containsKey("cognitive_reflection_cents")) {
            try {
                cents = Integer.parseInt(fields.get("cognitive_reflection_cents"));
            } catch (NumberFormatException ex) {
            }
        }
        if (cents != null && cents == 5) {
            numCorrect++;
        }

        Integer minutes = null;
        if (fields.containsKey("cognitive_reflection_minutes")) {
            try {
                minutes = Integer.parseInt(fields.get("cognitive_reflection_minutes"));
            } catch (NumberFormatException ex) {
            }
        }
        if (minutes != null && minutes == 5) {
            numCorrect++;
        }

        Integer days = null;
        if (fields.containsKey("cognitive_reflection_days")) {
            try {
                days = Integer.parseInt(fields.get("cognitive_reflection_days"));
            } catch (NumberFormatException ex) {
            }
        }
        if (days != null && days == 47) {
            numCorrect++;
        }

        return numCorrect;
    } 
    
    /**
     * Computes Video Game Experience just using the video game time field,
     * no entry = 0 (no experience), year = 1, month = 2, week = 3, 
     * day = 4 (highest experience).
     * 
     * @param fields
     * @return
     */
    protected double computeVgeScore(Map<String, String> fields) {
        //Just use the video game time field, 
        //no entry = 0, year = 1, month = 2, week = 3, day = 4
        String vgeTime = fields.get("video_game_time");
        if (vgeTime != null && !vgeTime.isEmpty()) {
            if (vgeTime.equalsIgnoreCase("year")) {
                return 1;
            } else if (vgeTime.equalsIgnoreCase("month")) {
                return 2;
            } else if (vgeTime.equalsIgnoreCase("week")) {
                return 3;
            } else if (vgeTime.equalsIgnoreCase("day")) {
                return 4;
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }
}
