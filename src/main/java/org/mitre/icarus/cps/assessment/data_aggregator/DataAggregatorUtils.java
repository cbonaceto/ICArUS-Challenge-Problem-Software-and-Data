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

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

import org.mitre.icarus.cps.app.experiment.IcarusSubjectData;
import org.mitre.icarus.cps.web.model.Site;

/**
 * Contains static utility methods for aggregating subject data.
 *
 * @author CBONACETO
 */
public class DataAggregatorUtils {

    /**
     * Gets the IDs of all subjects with in a folder.
     *
     * @param dataDir
     * @param useSubjectFolders
     * @return
     * @throws IOException
     */
    public static Set<IcarusSubjectData> getSubjectsInFolder(File dataDir, boolean useSubjectFolders) throws IOException {
        //S_MITRE_001 or S001
        TreeSet<IcarusSubjectData> subjects = new TreeSet<IcarusSubjectData>();
        File[] files = dataDir.listFiles();
        if (files != null) {
            if (useSubjectFolders) {
                for (File file : files) {
                    if (file.isDirectory() && file.getName().startsWith("S")
                            || file.getName().startsWith("s")) {
                        if (file.getName().contains("_")) {
                            String[] parts = file.getName().split("_");
                            if (parts.length == 3) {
                                String siteId = parts[1];
                                String subjectId = parts[2];
                                subjects.add(new IcarusSubjectData(subjectId, new Site(siteId, siteId), 0));
                            }
                        } else {
                            //subjects.add(file.getName());
                            String sid = file.getName();
                            if (sid.length() > 1 && (sid.startsWith("s") || sid.startsWith("S"))) {
                                subjects.add(new IcarusSubjectData(sid.substring(1), 0));
                            } else {
                                subjects.add(new IcarusSubjectData(sid, 0));
                            }
                        }
                    }
                }
            } else {
                for (File file : files) {
                    if (!file.isDirectory()) {// && file.getName().startsWith("S") || file.getName().startsWith("s")) {
                        try {
                            TaskFileAttributes fileAttributes = TaskFileAttributes.parseTaskFileAttributes(file.getName());
                            //if(fileAttributes != null && fileAttributes.subjectId != null) {
                            if (fileAttributes != null && fileAttributes.subjectData != null) {
                                //subjects.add(fileAttributes.subjectId);
                                subjects.add(fileAttributes.subjectData);
                            }
                        } catch (Exception ex) {
                        }
                    }
                }
            }
        }
        return subjects;
    }
}
