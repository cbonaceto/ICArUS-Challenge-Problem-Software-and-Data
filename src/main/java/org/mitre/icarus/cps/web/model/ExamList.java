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
package org.mitre.icarus.cps.web.model;

import static com.sun.org.apache.xerces.internal.util.PropertyState.is;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.mitre.icarus.cps.web.model.ExamList.ExamListEntry;

/**
 * Contains the names and paths to Phase 1 and Phase 2 exams. Used to locate
 * exams when packaged in a jar file as part of the application.
 *
 * @author CBONACETO
 */
@XmlRootElement(name = "Exams")
public class ExamList implements Iterable<ExamList.ExamListEntry> {

    /**
     * Contains the exams
     */
    protected List<ExamListEntry> exams;    

    public ExamList() {
    }
    
    public ExamList(List<ExamListEntry> exams) {
        this.exams = exams;
    }

    public ExamList(InputStream is) throws Exception {
        ExamList examList = unmarshalExams(is);
        if (examList != null) {
            this.exams = examList.exams;
        }
    }

    @XmlElement(name = "Exam")
    public List<ExamListEntry> getExams() {
        return exams;
    }

    public void setExams(List<ExamListEntry> exams) {
        this.exams = exams;
    }

    public static ExamList unmarshalExams(InputStream is) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(ExamList.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return (ExamList) unmarshaller.unmarshal(is);
    }
    
    public boolean isEmpty() {
        return exams == null || exams.isEmpty();
    }

    @Override
    public Iterator<ExamListEntry> iterator() {
        return exams != null ? exams.iterator() : null;
    }

    public static class ExamListEntry {

        /**
         * The exam name
         */
        @XmlAttribute
        public String examName;

        /**
         * The exam file location
         */
        @XmlAttribute
        public String examFileLocation;
        
        /** 
         * The exam phase ID ("1" or "2") 
         */
        @XmlAttribute
        public String phaseId;
        
        public ExamListEntry() {}
        
        public ExamListEntry(String examName, String examFileLocation,
                String phaseId) {
            this.examName = examName;
            this.examFileLocation = examFileLocation;
            this.phaseId = phaseId;
        }

        @Override
        public String toString() {
            return examName;
        }
    }
    
    /**
     * Creates the default list of Phase 1 and Phase 2 exams.
     * 
     * @return
     */
    public static ExamList createDefaultExamList() {
        return new ExamList(Arrays.asList(
                new ExamListEntry("Pilot Exam", "data/Phase_1_CPD/exams/PilotExam-1-15/PilotExam-1-15.xml", "1"),
                new ExamListEntry("Final Exam", "data/Phase_1_CPD/exams/Final-Exam-1/Final-Exam-1.xml", "1"),
                new ExamListEntry("Sample Exam 1", "data/Phase_2_CPD/exams/Sample-Exam-1/Sample-Exam-1.xml", "2"),
                new ExamListEntry("Sample Exam 1 (Short)", "data/Phase_2_CPD/exams/Sample-Exam-1/Sample-Exam-1-Short.xml", "2"),
                new ExamListEntry("Sample Exam 2", "data/Phase_2_CPD/exams/Sample-Exam-2/Sample-Exam-2.xml", "2"),
                new ExamListEntry("Final Exam", "data/Phase_2_CPD/exams/Final-Exam-1/Final-Exam-1.xml", "2"),
                new ExamListEntry("Final Exam (Short)", "data/Phase_2_CPD/exams/Final-Exam-1/Final-Exam-1-Short.xml", "2")
        ));
    }
    
    public static void main(String[] args) {
        try {
            //Create a sample list of Phase 1 and Phase 2 exams
            ExamList examList = createDefaultExamList();
            JAXBContext jaxbContext = JAXBContext.newInstance(ExamList.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            marshaller.marshal(examList, output);
            System.out.println(output.toString());
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }
    }
}