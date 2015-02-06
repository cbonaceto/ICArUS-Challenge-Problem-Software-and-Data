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
package org.mitre.icarus.cps.assessment.data_aggregator;

import org.mitre.icarus.cps.app.experiment.IcarusSubjectData;
import org.mitre.icarus.cps.web.model.Site;

/**
 *  Contains the exam name, task name, task number, and trial number for a subject
 *  or model response file.
 * 
 * @author CBONACETO
 *
 */
public class TaskFileAttributes {

    /** The exam name */
    public String examName;

    /** The task name */
    public String taskName;

    /** The task number */
    public int taskNum;

    /** The trial number */
    public int trialNum;

    /** The subject who generated the response */
    public IcarusSubjectData subjectData;

    public static TaskFileAttributes parseTaskFileAttributes(String fileName) {
		//S001_Pilot Exam_Task5 or S_MITRE_001_Pilot Exam_Task5 or MITRE_001_Pilot Exam_Task5 (human subjects) or
        //HRL_emergent_Pilot Exam_Task5_Trial1_07312012 (models)
        TaskFileAttributes attributes = new TaskFileAttributes();
        String[] parts = fileName.split("_");

        if (parts == null || (parts.length != 3 && parts.length != 5 && parts.length != 6)) {
            throw new IllegalArgumentException("Error, malformed file name:  " + fileName);
        }

        attributes.subjectData = new IcarusSubjectData();
        int siteIdIndex = -1;
        int examIdIndex = 1;
        int taskIndex = 2;
        int trialIndex = -1;
        if (parts.length == 3) {
			//Parse format S001_Pilot Exam_Task5				
            //Get the subject ID
            //attributes.subjectId = parts[0];
            String sid = parts[0];
            if (sid.length() > 1 && (sid.startsWith("s") || sid.startsWith("S"))) {
                attributes.subjectData.setSubjectId(sid.substring(1));
            } else {
                attributes.subjectData.setSubjectId(sid);
            }

            //Get the exam name
            attributes.examName = parts[1];
        } else if (parts.length == 5) {
			//Parse format S_MITRE_001_Pilot Exam_Task5				

            //Get the subject ID
            attributes.subjectData.setSubjectId(parts[2]);

            siteIdIndex = 1;
            examIdIndex = 3;
            taskIndex = 4;
        } else {
			//Parse format MITRE_001_Pilot Exam_Task5_Trial1_07312012 (models)

            //Get the subject ID
            attributes.subjectData.setSubjectId(parts[1]);

            siteIdIndex = 0;
            examIdIndex = 2;
            taskIndex = 3;
            trialIndex = 4;
        }

        //Get the site ID
        if (siteIdIndex > -1) {
            attributes.subjectData.setSite(new Site(parts[siteIdIndex], parts[siteIdIndex]));
        }

        //Get the exam ID
        attributes.examName = parts[examIdIndex];

        //Get the task name and number
        if (taskIndex > -1) {
            if (taskIndex == 2 || taskIndex == 4) {
                attributes.taskName = parts[taskIndex].substring(0, parts[taskIndex].lastIndexOf('.'));
            } else {
                attributes.taskName = parts[taskIndex];
            }
            int endIndex = attributes.taskName.length() - 1;
            attributes.taskNum = Integer.parseInt(
                    attributes.taskName.substring(endIndex));
        }

        //Get the trial number
        if (trialIndex > -1) {
            //TODO: Account for 2 or more digit trial numbers
            String trialName = parts[trialIndex];
            int endIndex = trialName.length() - 1;
            attributes.trialNum = Integer.parseInt(trialName.substring(endIndex));
        }

        return attributes;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Exam: ");
        sb.append(examName);
        sb.append(", Task: ");
        sb.append(taskName);
        sb.append(", Subject: ");
        if (subjectData != null) {
            sb.append(subjectData.toString());
        } else {
            sb.append("null");
        }
        return sb.toString();
    }
}
