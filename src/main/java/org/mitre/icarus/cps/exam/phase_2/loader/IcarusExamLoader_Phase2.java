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
package org.mitre.icarus.cps.exam.phase_2.loader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.mitre.icarus.cps.app.widgets.progress_monitor.IProgressMonitor;
import org.mitre.icarus.cps.exam.base.loader.IcarusExamLoader;
import org.mitre.icarus.cps.exam.phase_2.IcarusExam_Phase2;
import org.mitre.icarus.cps.exam.phase_2.feedback.TrialFeedback_Phase2;
import org.mitre.icarus.cps.exam.phase_2.testing.IcarusTestTrial_Phase2;
import org.mitre.icarus.cps.exam.phase_2.testing.Mission;
import org.mitre.icarus.cps.exam.phase_2.training.IcarusExamTutorial_Phase2;
import org.mitre.icarus.cps.feature_vector.phase_2.AreaOfInterest;
import org.mitre.icarus.cps.feature_vector.phase_2.BlueLocation;
import org.mitre.icarus.cps.feature_vector.phase_2.FeatureContainer;
import org.mitre.icarus.cps.feature_vector.phase_2.loader.FeatureVectorLoader;

/**
 * Binds exam and response classes to XML using JAXB for the Phase 2 CPD format.
 * 
 * @author CBONACETO
 *
 */
public class IcarusExamLoader_Phase2 extends IcarusExamLoader<IcarusExam_Phase2, Mission<?>, IcarusExamTutorial_Phase2> {
	
	/** The exam schema */
	public static final Schema ExamSchema;
	
	/** Load the exam schema */
	static {
		Schema examSchema = null;
		try {
			SchemaFactory s = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			ClassLoader classLoader = IcarusExamLoader_Phase2.class.getClassLoader();
			examSchema = s.newSchema(classLoader.getResource("schemas/IcarusCPD_2_Schema.xsd"));			
		} catch (Exception e) {
			e.printStackTrace();
		}
		ExamSchema = examSchema;
	}
	
	/** The feature vector loader used to load and cache feature vector data */
	protected static FeatureVectorLoader featureVectorLoader = FeatureVectorLoader.getInstance();
	
	/** Singleton instance of the exam loader */
	public static final IcarusExamLoader_Phase2 examLoaderInstance = new IcarusExamLoader_Phase2();
	
	private IcarusExamLoader_Phase2() {}		
	
	/**
	 * Get the singleton instance of the exam loader
	 * 
	 * @return
	 */
	public static IcarusExamLoader_Phase2 getInstance() {
		return examLoaderInstance;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.base.loader.IcarusExamLoaderBase#marshalExamToXml(org.mitre.icarus.cps.exam.base.IcarusExam)
	 */
	@Override
	public String marshalExamToXml(IcarusExam_Phase2 exam) throws JAXBException {
		return marshalExam(exam);
	}
	
	/**
	 * Marshalls an IcarusExam_Phase1 to XML.
	 * 
	 * @param exam the exam to marshall
	 * @return the exam XML string
	 * @throws JAXBException
	 */
	public static String marshalExam(IcarusExam_Phase2 exam) throws JAXBException {
		Marshaller marshaller = createMarshallerForClass(IcarusExam_Phase2.class);
		
		ByteArrayOutputStream output = new ByteArrayOutputStream (); 
		marshaller.marshal(exam, output);
		
		return output.toString();
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.base.loader.IcarusExamLoaderBase#unmarshalExamFromXml(java.net.URL, boolean)
	 */
	@Override
	public IcarusExam_Phase2 unmarshalExamFromXml(URL examXmlFile, boolean validate) throws JAXBException {
		return unmarshalExam(examXmlFile, validate);
	}
	
	/**
	 * Unmarshalls an IcarusExam from an XML file. Does not validate against the schema. 
	 *
	 * @param examFile the URL to the exam file
	 * @return an IcarusExam_Phase1 instance
	 * @throws JAXBException
	 */
	public static IcarusExam_Phase2 unmarshalExam(URL examFile) throws JAXBException {
		return unmarshalExam(examFile, false);
	}
	
	/**	  
	 * Unmarshalls an IcarusExam from an XML file and validates the file against the schema if validate is true.
	 * 
	 * @param examFile the URL to the exam file
	 * @param validate whether or not to validate the file against the schema
	 * @return an IcarusExam_Phase1 instance
	 * @throws JAXBException
	 */
	public static IcarusExam_Phase2 unmarshalExam(URL examFile, boolean validate) throws JAXBException {

		Unmarshaller unmarshaller = createUnmarshallerForClass(IcarusExam_Phase2.class, ExamSchema, validate);		
		
		IcarusExam_Phase2 exam = (IcarusExam_Phase2)(unmarshaller.unmarshal(examFile));
		if(exam != null) {
			exam.setOriginalPath(examFile);			
		}		
		
		if(validate && unmarshaller.getEventHandler() != null && 
				!unmarshaller.getEventHandler().toString().isEmpty()) {
			throw new JAXBException(unmarshaller.getEventHandler().toString());
		}
		
		return exam;
	}	

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.base.loader.IcarusExamLoaderBase#unmarshalExamFromXml(java.lang.String, boolean)
	 */
	@Override
	public IcarusExam_Phase2 unmarshalExamFromXml(String examXmlString, boolean validate) throws JAXBException {
		return unmarshalExam(examXmlString, validate);
	}
	
	/**	  
	 * Unmarshalls an IcarusExam from an XML string and validates the XML against the schema if validate is true.
	 * 
	 * @param examXml a string containing the exam XML
	 * @param validate whether or not to validate the XML against the schema
	 * @return an IcarusExam_Phase1 instance
	 * @throws JAXBException
	 */
	public static IcarusExam_Phase2 unmarshalExam(String examXml, boolean validate) throws JAXBException {
		Unmarshaller unmarshaller = createUnmarshallerForClass(IcarusExam_Phase2.class, ExamSchema, validate);
		
		StringReader examXmlStream = new StringReader(examXml);
		IcarusExam_Phase2 exam = (IcarusExam_Phase2)(unmarshaller.unmarshal(examXmlStream));		
		
		if(validate && unmarshaller.getEventHandler() != null && !unmarshaller.getEventHandler().toString().isEmpty()) {
			throw new JAXBException(unmarshaller.getEventHandler().toString());
		}
		
		return exam;
	}	

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.base.loader.IcarusExamLoaderBase#marshalExamPhase(org.mitre.icarus.cps.exam.base.IcarusExamPhase)
	 */
	@Override
	public String marshalExamPhase(Mission<?> examPhase) throws JAXBException {
		Marshaller marshaller = createMarshallerForClass(examPhase.getClass());
		
		ByteArrayOutputStream output = new ByteArrayOutputStream(); 
		marshaller.marshal(examPhase, output);
		
		return output.toString();
	}	

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.base.loader.IcarusExamLoaderBase#unmarshalExampPhase(java.net.URL, boolean)
	 */
	@Override
	public Mission<?> unmarshalExamPhase(URL examPhaseXmlFile, boolean validate) throws JAXBException {
		Unmarshaller unmarshaller = createUnmarshallerForClass(Mission.class, ExamSchema, validate);		
		
		Mission<?> examPhase = (Mission<?>)(unmarshaller.unmarshal(examPhaseXmlFile));
		
		if(validate && unmarshaller.getEventHandler() != null && !unmarshaller.getEventHandler().toString().isEmpty()) {
			throw new JAXBException(unmarshaller.getEventHandler().toString());
		}
		
		return examPhase;
	}	
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.base.loader.IcarusExamLoaderBase#unmarshalExamPhase(java.lang.String, boolean)
	 */
	@Override
	public Mission<?> unmarshalExamPhase(String examPhaseXml, boolean validate) throws JAXBException {
		Unmarshaller unmarshaller = createUnmarshallerForClass(Mission.class, ExamSchema, validate);		
		
		StringReader examPhaseXmlStream = new StringReader(examPhaseXml);		
		Mission<?> examPhase = (Mission<?>)(unmarshaller.unmarshal(examPhaseXmlStream));
		
		if(validate && unmarshaller.getEventHandler() != null && !unmarshaller.getEventHandler().toString().isEmpty()) {
			throw new JAXBException(unmarshaller.getEventHandler().toString());
		}
		
		return examPhase;
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.base.loader.IcarusExamLoader#marshalTutorialPhase(org.mitre.icarus.cps.exam.base.training.IcarusTutorialPhase)
	 */
	@Override
	public String marshalTutorialPhase(IcarusExamTutorial_Phase2 tutorialPhase)
			throws JAXBException {
		Marshaller marshaller = createMarshallerForClass(tutorialPhase.getClass());
		
		ByteArrayOutputStream output = new ByteArrayOutputStream (); 
		marshaller.marshal(tutorialPhase, output);
		
		return output.toString();
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.base.loader.IcarusExamLoader#unmarshalTutorialPhase(java.net.URL, boolean)
	 */
	@Override
	public IcarusExamTutorial_Phase2 unmarshalTutorialPhase(
			URL tutorialPhaseXmlFile, boolean validate) throws JAXBException {
		Unmarshaller unmarshaller = createUnmarshallerForClass(IcarusExamTutorial_Phase2.class, ExamSchema, validate);		
		
		IcarusExamTutorial_Phase2 tutorialPhase = (IcarusExamTutorial_Phase2)(unmarshaller.unmarshal(tutorialPhaseXmlFile));
		
		if(validate && unmarshaller.getEventHandler() != null && !unmarshaller.getEventHandler().toString().isEmpty()) {
			throw new JAXBException(unmarshaller.getEventHandler().toString());
		}
		
		return tutorialPhase;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.base.loader.IcarusExamLoader#unmarshalTutorialPhase(java.lang.String, boolean)
	 */
	@Override
	public IcarusExamTutorial_Phase2 unmarshalTutorialPhase(
			String tutorialPhaseXml, boolean validate) throws JAXBException {
		Unmarshaller unmarshaller = createUnmarshallerForClass(IcarusExamTutorial_Phase2.class, ExamSchema, validate);		
		
		StringReader tutorialPhaseXmlStream = new StringReader(tutorialPhaseXml);		
		IcarusExamTutorial_Phase2 tutorialPhase = (IcarusExamTutorial_Phase2)(unmarshaller.unmarshal(tutorialPhaseXmlStream));
		
		if(validate && unmarshaller.getEventHandler() != null && !unmarshaller.getEventHandler().toString().isEmpty()) {
			throw new JAXBException(unmarshaller.getEventHandler().toString());
		}
		
		return tutorialPhase;
	}	
	
	/**
	 * Marshals an IcarusTestTrial_Phase2 to XML.
	 * 
	 * @param trial the trial
	 * @return the trial XML string
	 * @throws JAXBException
	 */
	public static String marshalTrial(IcarusTestTrial_Phase2 trial) throws JAXBException {
		Marshaller marshaller = createMarshallerForClass(IcarusTestTrial_Phase2.class);
		
		ByteArrayOutputStream output = new ByteArrayOutputStream(); 
		marshaller.marshal(trial, output);
		
		return output.toString();
	}
	
	/**
	 * Unmarshalls an IcarusTestTrial_Phase2 from an XML file and validates the file against the schema if validate is true.
	 * 
	 * @param trialFile the URL to the trial file
	 * @param validate whether to validate the file against the schema
	 * @return an IcarusTestTrial_Phase1 instance
	 * @throws JAXBException
	 */
	public static IcarusTestTrial_Phase2 unmarshalTrial(URL trialFile, boolean validate) throws JAXBException {
		Unmarshaller unmarshaller = createUnmarshallerForClass(IcarusTestTrial_Phase2.class, ExamSchema, validate);		
		
		IcarusTestTrial_Phase2 trial = (IcarusTestTrial_Phase2)(unmarshaller.unmarshal(trialFile));
		
		if(validate && unmarshaller.getEventHandler() != null && !unmarshaller.getEventHandler().toString().isEmpty()) {
			throw new JAXBException(unmarshaller.getEventHandler().toString());
		}
		
		return trial;
	}
	
	/**
	 * Marshalls a TrialFeedback_Phase2 to XML.
	 * 
	 * @param trialFeedback the trial feedback
	 * @return the trial feedback XML string
	 * @throws JAXBException
	 */
	public static String marshalTrialFeedback(TrialFeedback_Phase2 trialFeedback) throws JAXBException {
		Marshaller marshaller = createMarshallerForClass(trialFeedback.getClass());
		
		ByteArrayOutputStream output = new ByteArrayOutputStream (); 
		marshaller.marshal(trialFeedback, output);
		
		return output.toString();
	}
	
	/**
	 * Unmarshalls a TrialFeedback_Phase2 from an XML file and validates the file against the schema if validate is true.
	 * 
	 * @param trialFeedbackFile the URL to the trial feedback file
	 * @param validate whether to validate the file against the scehma
	 * @return a TrialFeedback_Phase1 instance
	 * @throws JAXBException
	 */
	public static TrialFeedback_Phase2 unmarshalTrialFeedback(URL trialFeedbackFile, boolean validate) throws JAXBException {
		Unmarshaller unmarshaller = createUnmarshallerForClass(TrialFeedback_Phase2.class, ExamSchema, validate);		
		
		TrialFeedback_Phase2 trialFeedback = (TrialFeedback_Phase2)(unmarshaller.unmarshal(trialFeedbackFile));
		
		if(validate && unmarshaller.getEventHandler() != null && !unmarshaller.getEventHandler().toString().isEmpty()) {
			throw new JAXBException(unmarshaller.getEventHandler().toString());
		}
		
		return trialFeedback;
	}
	
	/**
	 * Unmarshalls a TrialFeedback_Phase1 from XML and validates the file against the schema if validate is true.
	 * 
	 * @param trialFeedbackXml a string containing the trial feedback XML
	 * @param validate whether to validate the file against the scehma
	 * @return a TrialFeedback_Phase1 instance
	 * @throws JAXBException
	 */
	public static TrialFeedback_Phase2 unmarshalTrialFeedback(String trialFeedbackXml, boolean validate) throws JAXBException {
		Unmarshaller unmarshaller = createUnmarshallerForClass(TrialFeedback_Phase2.class, ExamSchema, validate);		
		
		StringReader trialFeedbackXmlStream = new StringReader(trialFeedbackXml);		
		TrialFeedback_Phase2 trialFeedback = (TrialFeedback_Phase2)(unmarshaller.unmarshal(trialFeedbackXmlStream));
		
		if(validate && unmarshaller.getEventHandler() != null && !unmarshaller.getEventHandler().toString().isEmpty()) {
			throw new JAXBException(unmarshaller.getEventHandler().toString());
		}
		
		return trialFeedback;
	}
	
	/**
	 * 
	 * Populates the feature vector data (area of interest, blue locations) for all trials in all missions in an exam.
	 * 
	 * @param exam the exam
	 * @param baseUrl the URL to the location of the feature vector files if using relative URLs
	 * @param progressMonitor an optional progress monitor to track load progress
	 * @throws JAXBException
	 * @throws IOException
	 */
	public static void initializeExamFeatureVectorData(IcarusExam_Phase2 exam, URL baseUrl,
			IProgressMonitor progressMonitor) throws JAXBException, IOException {
		if(exam != null && exam.getMissions() != null) {
			for(Mission<?> mission : exam.getMissions()) {
				IcarusExamLoader_Phase2.initializeMissionFeatureVectorData(
						mission, baseUrl, true, true, progressMonitor);
			}
		}
	}
	
	/**
	 * Populates the feature vector data for all trials in a mission (blue locations) and the area of interest for the mission.
	 * 
	 * @param mission the mission
	 * @param baseUrl the URL to the location of the feature vector files if using relative URLs
	 * @param progressMonitor an optional progress monitor to track load progress
	 * @throws JAXBException
	 * @throws IOException
	 */
	public static void initializeMissionFeatureVectorData(Mission<? extends IcarusTestTrial_Phase2> mission, URL baseUrl,
			IProgressMonitor progressMonitor) throws JAXBException, IOException {
		IcarusExamLoader_Phase2.initializeMissionFeatureVectorData(
				mission, baseUrl, true, true, progressMonitor);
	}
	
	/**
	 * Populates the feature vector data for all trials in a mission (blue locations) and the area of interest for the mission.
	 * 
	 * @param mission the mission
	 * @param baseUrl the URL to the location of the feature vector files if using relative URLs
	 * @param loadAoi whether to load the area of interest feature vector for the mission 
	 * @param loadBlueLocations whether to load the blue locations for each trial in the mission
	 * @param progressMonitor an optional progress monitor to track load progress
	 * @throws JAXBException
	 * @throws IOException 
	 */	
	public static void initializeMissionFeatureVectorData(Mission<? extends IcarusTestTrial_Phase2> mission, URL baseUrl,
			boolean loadAoi, boolean loadBlueLocations,
			IProgressMonitor progressMonitor) throws JAXBException, IOException {
		if(progressMonitor != null) {
			progressMonitor.setProgress(progressMonitor.getMinimum());
		}	
		if(mission != null) {
			int progressTotal = progressMonitor != null ? progressMonitor.getMaximum() - progressMonitor.getMinimum() : 0;
			int progress = 0;
			//Load the area of interest for the mission
			if(loadAoi && mission.getAoiFile() != null && mission.getAoiFile().getFileUrl() != null) {				
				mission.setAoi(featureVectorLoader.unmarshalAreaOfInterest(
						createUrl(baseUrl, mission.getAoiFile().getFileUrl()), false));
				if(mission.getAoi() != null && mission.getAoi().getSceneImageFile() != null 
						&& mission.getAoi().getSceneImageFile().getFileUrl() != null) {
					//Load the scene image
					mission.getAoi().setSceneImage(
							ImageIO.read(createUrl(baseUrl, mission.getAoi().getSceneImageFile().getFileUrl())));
				}
			}
			if(progressMonitor != null) {
				progress = progressTotal/2;
				progressMonitor.setProgress(progress);
			}
			
			//Load the Blue locations for the mission
			if(loadBlueLocations && mission.getBlueLocationsFile() != null &&
					mission.getBlueLocationsFile().getFileUrl() != null &&
					mission.getTestTrials() != null && !mission.getTestTrials().isEmpty()) {				
				FeatureContainer<BlueLocation> locations = featureVectorLoader.unmarshalBlueLocations(
						createUrl(baseUrl, mission.getBlueLocationsFile().getFileUrl()), false,
						mission.getAoi() != null ? mission.getAoi().getImintRadius_miles() : null,
						mission.getAoi() != null ? mission.getAoi().getSigintRadius_miles() : null);
				if(locations != null && locations.getFeatureList() != null && !locations.getFeatureList().isEmpty()) {
					int locationsPerTrial = mission.getMissionType() != null ? mission.getMissionType().getLocationsPerTrial() : 1;
					Iterator<BlueLocation> locationIter = locations.getFeatureList().iterator();
					Iterator<? extends IcarusTestTrial_Phase2> trialIter = mission.getTestTrials().iterator();
					while(locationIter.hasNext() && trialIter.hasNext()) {
						BlueLocation location = locationIter.next();
						IcarusTestTrial_Phase2 trial = trialIter.next();
						if(locationsPerTrial > 1) {
							ArrayList<BlueLocation> locationList = new ArrayList<BlueLocation>(locationsPerTrial);
							trial.setBlueLocations(locationList);
							locationList.add(location);
							int i = 1;
							while(locationIter.hasNext() && i < locationsPerTrial) {
								locationList.add(locationIter.next());
								i++;
							}
						} else {
							trial.setBlueLocations(Arrays.asList(location));
						}
					}
				}
			}
		}
		if(progressMonitor != null) {
			progressMonitor.setProgress(progressMonitor.getMaximum());
		}
	}
	
	/**
	 * Create a new URL using the given base URL and path relative to the base URL.
	 * 
	 * @param baseUrl the base URL
	 * @param path a path relative to the base URL
	 * @return a new URL using the base URL and relative path
	 * @throws MalformedURLException
	 */
	public static URL createUrl(URL baseUrl, String path) throws MalformedURLException {
		return baseUrl != null ? new URL(baseUrl, path) : new URL(path);
	}
	
	public static void main(String[] args) {		
		try {
			//IcarusExam_Phase2 exam = createSampleExam();
			//System.out.println(marshalExam(exam));			
			//IcarusExam_Phase2 exam = unmarshalExam(new File(
			//		"data/Phase_2_CPD/exams/Sample_Exam_1/Sample-Exam-1.xml").toURI().toURL(), true);
			IcarusExam_Phase2 exam = unmarshalExam(new File(
					"data/Phase_2_CPD/exams/Sample_Exam_2/Sample-Exam-2.xml").toURI().toURL(), true);			
			if(exam.getMissions() != null && !exam.getMissions().isEmpty()) {
				for(Mission<?> mission : exam.getMissions()) {
					IcarusExamLoader_Phase2.initializeMissionFeatureVectorData(mission, exam.getOriginalPath(), null);
				}
			}
			System.out.println(marshalExam(exam));
			AreaOfInterest aoi = FeatureVectorLoader.getInstance().unmarshalAreaOfInterest(new File(
					"data/Phase_2_CPD/exams/Sample_Exam_1/Mission1_Area_Of_Interest.xml").toURI().toURL(), true);
			System.out.println(FeatureVectorLoader.getInstance().marshalAreaOfInterest(aoi));
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}	
	
	/*protected static IcarusExam_Phase2 createSampleExam() {
		IcarusExam_Phase2 exam = new IcarusExam_Phase2();
		exam.setProgramPhaseId("2");
		exam.setName("Sample Exam");
		exam.setId("Sample_Exam_1");
		exam.setNormalizationMode(NormalizationMode.NormalizeDuringManual);
		exam.setExamTimeStamp(Calendar.getInstance().getTime());
		
		//Create the tutorial
		IcarusExamTutorial_Phase2 tutorial = new IcarusExamTutorial_Phase2();
		exam.setTutorial(tutorial);
		tutorial.setName("Exam Tutorial");
		ArrayList<IcarusExamTutorialPage_Phase2> tutorialPages = new ArrayList<IcarusExamTutorialPage_Phase2>(); 
		tutorial.setTutorialPages(tutorialPages);
		for(int i=2; i<=11; i++) {
			tutorialPages.add(new IcarusExamTutorialPage_Phase2("tutorial/Slide" + i + ".PNG"));
		}
		for(int i=13; i<=23; i++) {
			tutorialPages.add(new IcarusExamTutorialPage_Phase2("tutorial/Slide" + i + ".PNG"));
		}
		
		//Create the BlueBook, SIGINT Reliability info, and Payoff matrix
		BlueBook blueBook = BlueBook.createDefaultBlueBook();
		exam.setBlueBook(blueBook);
		blueBook.setMission_1_TacticsInstructions(new InstructionsPage("tutorial/Mission_1_RedTactics.png"));
		blueBook.setMission_2_TacticsInstructions(new InstructionsPage("tutorial/Mission_2_RedTactics.png"));
		blueBook.setMission_3_TacticsInstructions(new InstructionsPage("tutorial/Mission_3_RedTactics.png"));		
		
		PayoffMatrix payoffMatrix = PayoffMatrix.createDefaultPayoffMatrix();
		exam.setPayoffMatrix(payoffMatrix);
		payoffMatrix.setPayoffMatrixInstructions(new InstructionsPage("tutorial/PayoffMatrix.png"));		
		
		SigintReliability sigintReliability = SigintReliability.createDefaultSigintReliability();
		exam.setSigintReliability(sigintReliability);
		sigintReliability.setSigintReliabilityInstructions(new InstructionsPage("tutorial/SigintReliability.png"));	
		
		//Create the missions and mission tutorials
		ArrayList<Mission<?>> missions = new ArrayList<Mission<?>>(3); 
		exam.setMissions(missions);
		int numTrials = 10;
		
		Random random = new Random(1);
		Mission<?> mission1 = MissionType.createSampleMission(MissionType.Mission_1, numTrials, random);
		missions.add(mission1);
		ArrayList<InstructionsPage> instructions = new ArrayList<InstructionsPage>();
		mission1.setInstructionPages(instructions);
		for(int i=25; i<=29; i++) {
			instructions.add(new InstructionsPage("tutorial/Slide" + i + ".PNG"));
		}		
		
		Mission<?> mission2 = MissionType.createSampleMission(MissionType.Mission_2, numTrials, random);
		missions.add(mission2);
		instructions = new ArrayList<InstructionsPage>();
		mission2.setInstructionPages(instructions);
		for(int i=30; i<=35; i++) {
			instructions.add(new InstructionsPage("tutorial/Slide" + i + ".PNG"));
		}		
		
		Mission<?> mission3 = MissionType.createSampleMission(MissionType.Mission_3, numTrials, random);
		missions.add(mission3);
		instructions = new ArrayList<InstructionsPage>();
		mission3.setInstructionPages(instructions);
		for(int i=36; i<=41; i++) {
			instructions.add(new InstructionsPage("tutorial/Slide" + i + ".PNG"));
		}		
		
		return exam;
	}*/
}