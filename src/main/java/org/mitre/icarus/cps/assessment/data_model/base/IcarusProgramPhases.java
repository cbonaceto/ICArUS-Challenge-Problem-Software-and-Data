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
package org.mitre.icarus.cps.assessment.data_model.base;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.mitre.icarus.cps.assessment.data_model.phase_1.ExamData;
import org.mitre.icarus.cps.assessment.data_model.phase_1.ProgramPhase_Phase1;
import org.mitre.icarus.cps.assessment.data_model.phase_1.Site_Phase1;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.MetricsInfo;
import org.mitre.icarus.cps.assessment.data_model.phase_1.data_sets.AverageHumanDataSet_Phase1;
import org.mitre.icarus.cps.assessment.data_model.phase_1.data_sets.SingleModelDataSet_Phase1;
import org.mitre.icarus.cps.assessment.persistence.phase_1.xml.XMLCPADataPersister;
import org.mitre.icarus.cps.exam.phase_1.IcarusExam_Phase1;
import org.mitre.icarus.cps.exam.phase_1.loader.IcarusExamLoader_Phase1;

/**
 * @author CBONACETO
 *
 */
@Entity
public class IcarusProgramPhases {
	/** The phases in the ICArUS Program (1-N) */
	protected List<ProgramPhase<?, ?, ?, ?, ?, ?, ?, ?, ?>> programPhases;

	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinColumn(name="icarusProgramPhaseProgramPhases")
	@Fetch(value = FetchMode.SUBSELECT)
	public List<ProgramPhase<?, ?, ?, ?, ?, ?, ?, ?, ?>> getProgramPhases() {
		return programPhases;
	}

	public void setProgramPhases(List<ProgramPhase<?, ?, ?, ?, ?, ?, ?, ?, ?>> programPhases) {
		this.programPhases = programPhases;
	}	
	
	private int id;
	
	@Id @GeneratedValue
	@Column( name="icarusProgramPhaseId" )
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public static IcarusProgramPhases createSampleData() {		
		IcarusProgramPhases phases = new IcarusProgramPhases();

		// Program Phase List
		ArrayList<ProgramPhase<?, ?, ?, ?, ?, ?, ?, ?, ?>> programPhaseList = new ArrayList<ProgramPhase<?, ?, ?, ?, ?, ?, ?, ?, ?>>();
		phases.setProgramPhases(programPhaseList);

		ProgramPhase_Phase1 phase1 = new ProgramPhase_Phase1();
		programPhaseList.add(phase1);
		phase1.setDescription("Phase 1 (Abducting Hotspots of Activity)");
		phase1.setId(1);
		phase1.setName("Phase 1");

		//Create the exam data for phase 1 (2 exams: Final Exam and Pilot Exam)
		ArrayList<ExamData> examDataListPhase1 = new ArrayList<ExamData>();
		phase1.setExams(examDataListPhase1);

		ExamData pilotExam = new ExamData(loadExam(new File("data/Phase_1/Pilot_Exam/Exam_Data/PilotExam.xml")), 
				MetricsInfo.createDefaultMetricsInfo());
		examDataListPhase1.add(pilotExam);

		ExamData finalExam = new ExamData(loadExam(new File("data/Phase_1/Final_Exam/Exam_Data/FinalExam.xml")), 
				MetricsInfo.createDefaultMetricsInfo());
		examDataListPhase1.add(finalExam);		

		//Create the site list for Phase 1
		ArrayList<Site_Phase1> siteList = new ArrayList<Site_Phase1>();
		phase1.setSites(siteList);		

		Site_Phase1 bbnSite = new Site_Phase1();		
		siteList.add(bbnSite);
		bbnSite.setId(1);		
		bbnSite.setSite_id("BBN");
		bbnSite.setName("BBN");

		Site_Phase1 hrlSite = new Site_Phase1();
		siteList.add(hrlSite);
		hrlSite.setId(2);
		hrlSite.setSite_id("HRL");
		hrlSite.setName("HRL");			

		Site_Phase1 lmcSite = new Site_Phase1();
		siteList.add(lmcSite);
		lmcSite.setId(3);
		lmcSite.setSite_id("LMC");
		lmcSite.setName("Lockheed Martin");

		//Load the final exam data sets for each site and for the average human
		try {
			XMLCPADataPersister xml = new XMLCPADataPersister(new File("data/Phase_1/Final_Exam/Assessment_Results").toURI().toURL());
			
			//Load the average human data set for the Final Exam
			AverageHumanDataSet_Phase1 humanDataSet = xml.loadAverageHumanDataSet("Final-Exam-1");
			finalExam.setAvgHumanDataSet(humanDataSet);
			
			//Load the BBN data			
			SingleModelDataSet_Phase1 dataSet = xml.loadSingleModelDataSet("Final-Exam-1", "BBN", "Insight23");
			bbnSite.setDataSets(Collections.singletonList(dataSet));

			//Load the HRL data
			dataSet = xml.loadSingleModelDataSet("Final-Exam-1", "HRL", "aggregatedResponseTest3");
			hrlSite.setDataSets(Collections.singletonList(dataSet));

			//Load the LMC data
			dataSet = xml.loadSingleModelDataSet("Final-Exam-1", "LMC", "Model0-Run2");
			lmcSite.setDataSets(Collections.singletonList(dataSet));
		} catch(Exception ex) {
			ex.printStackTrace();
		}

		//Create an empty Phase 2
		//ProgramPhase phase2 = new ProgramPhase();
		//phase2.setDescription("Phase2 Description");
		//phase2.setId(2);
		//phase2.setName("Phase2");		

		return phases;
	}
	
	protected static IcarusExam_Phase1 loadExam(File examFile) {
		IcarusExam_Phase1 exam = null;
		try {
			exam = IcarusExamLoader_Phase1.unmarshalExam(examFile.toURI().toURL(), false);
		} catch(Exception ex) {
			ex.printStackTrace();
		}		
		return exam;
	}
	
	/*public static void main(String[] args) {
		 IcarusProgramPhases.createSampleData();
	}*/
}