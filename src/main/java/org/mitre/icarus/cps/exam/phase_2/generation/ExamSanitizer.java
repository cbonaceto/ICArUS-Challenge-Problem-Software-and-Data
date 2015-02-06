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
package org.mitre.icarus.cps.exam.phase_2.generation;

import java.io.File;
import java.net.URL;
import java.util.Calendar;
import javax.xml.bind.JAXBException;

import org.mitre.icarus.cps.exam.phase_2.IcarusExam_Phase2;
import org.mitre.icarus.cps.exam.phase_2.loader.IcarusExamLoader_Phase2;
import org.mitre.icarus.cps.exam.phase_2.testing.Mission;
import org.mitre.icarus.cps.exam.phase_2.testing.MissionType;
import org.mitre.icarus.cps.exam.phase_2.testing.Mission_1_2_3;
import org.mitre.icarus.cps.exam.phase_2.testing.Mission_1_2_3_Trial;
import org.mitre.icarus.cps.exam.phase_2.testing.Mission_4_5_6;
import org.mitre.icarus.cps.exam.phase_2.testing.Mission_4_5_6_Trial;
import org.mitre.icarus.cps.examples.phase_2.ParsingExample;

/**
 * Removes ground truth and normative solutions from an exam to create the
 * "sanitized" exam file.
 *
 * @author CBONACETO
 *
 */
public class ExamSanitizer {

    public static void main(String[] args) {	
        //String fileName = "data/Phase_2_CPD/exams/Sample-Exam-DG/Sample-Exam-DG.xml";
    	//String fileName = "data/Phase_2_CPD/exams/Sample-Exam-2-TH/Sample-Exam-2-TH.xml";
    	//String fileName = "data/Phase_2_CPD/exams/Sample-Exam-2/Sample-Exam-2.xml";
        String fileName = "data/Phase_2_CPD/exams/Final-Exam-2-1/Final-Exam-2-1-Gold.xml";
        if (args != null && args.length > 0) {
            fileName = args[0];
        }
        sanitizeExam(fileName, true, false);
    }

    /**
     * Removes ground truth (if removeGroundTruth true) and normative solutions 
     * from an exam to create the "sanitized" exam file that will be given to performers.
     *
     * @param fileName the exam file name
     * @param removeActualRedTactic whether to remove information about the tactic
     * Red is playing with (ground truth) from the exam
     * @param removeShowdownWinner whether to remove information about who (Red
     * or Blue) will win a showdown from the exam
     */
    public static void sanitizeExam(String fileName, boolean removeActualRedTactic, 
    		boolean removeShowdownWinner) {       
        //Load the exam
        IcarusExam_Phase2 exam = null;
        try {
            URL examFileURL = new File(fileName).toURI().toURL();
            exam = ParsingExample.loadExamAndFeatureVectors(examFileURL);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (exam != null) {
            exam.setExamTimeStamp(Calendar.getInstance().getTime());
            exam.setResponseGenerator(null);
            //exam.setNormalizationMode(null);
            exam.setTutorial(null);
        }

        if (exam != null && exam.getMissions() != null) {
            for (Mission<?> mission : exam.getMissions()) {                
                mission.setCountCondition(null);
                mission.setConditionNum(null);
                mission.setInstructionPages(null);
                mission.setInstructionText(null);
                mission.setShowInstructionPage(null);
                mission.setShowScore(null);
                mission.setPausePhase(null);                

                //Strip any responses and ground truth from each trial in the mission                               
                if (mission instanceof Mission_4_5_6) {
                    Mission_4_5_6 mission456 = (Mission_4_5_6) mission;
                    mission456.setNumBatchPlotsCreated(null);
                    mission456.clearResponseData();
                    if (mission.getTestTrials() != null) {
                        for (Mission_4_5_6_Trial trial : mission456.getTestTrials()) {
                            trial.setResponseGenerator(null);
                            if(removeActualRedTactic) {
                            	trial.setRedTactic(null);
                            }
                            if(removeShowdownWinner) {
                            	trial.setShowdownWinner(null);
                            }
                        }
                    }
                } else if (mission instanceof Mission_1_2_3) {
                    Mission_1_2_3 mission123 = (Mission_1_2_3) mission;
                    if(mission.getMissionType() == MissionType.Mission_2) {
                    	if(removeActualRedTactic) {
                    		mission123.setRedTactic(null);
                    	}
                    }
                    mission123.clearResponseData();
                    if (mission.getTestTrials() != null) {
                        for (Mission_1_2_3_Trial trial : mission123.getTestTrials()) {
                            trial.setResponseGenerator(null);
                            if(removeShowdownWinner) {
                            	trial.setShowdownWinner(null);
                            }
                        }
                    }
                }                
            }

            //Display the exam file with parameters
            System.out.println();
            System.out.println("Sanitized Exam:");
            try {
                System.out.println(IcarusExamLoader_Phase2.marshalExam(exam));
            } catch (JAXBException e) {
                e.printStackTrace();
            }
        }
    }
}