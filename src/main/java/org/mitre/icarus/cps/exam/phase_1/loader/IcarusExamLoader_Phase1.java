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
package org.mitre.icarus.cps.exam.phase_1.loader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.mitre.icarus.cps.app.widgets.progress_monitor.IProgressMonitor;
import org.mitre.icarus.cps.exam.base.loader.IcarusExamLoader;
import org.mitre.icarus.cps.exam.phase_1.IcarusExam_Phase1;
import org.mitre.icarus.cps.exam.phase_1.feedback.TaskFeedback;
import org.mitre.icarus.cps.exam.phase_1.feedback.TrialFeedback_Phase1;
import org.mitre.icarus.cps.exam.phase_1.testing.IcarusTestTrial_Phase1;
import org.mitre.icarus.cps.exam.phase_1.testing.TaskTestPhase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_1_2_3_PhaseBase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_1_2_3_TrialBlockBase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_3_Phase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_4_5_6_PhaseBase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_4_5_6_TrialBase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_7_Phase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_7_Trial;
import org.mitre.icarus.cps.exam.phase_1.training.TutorialPhase;
import org.mitre.icarus.cps.feature_vector.phase_1.FeatureVectorFileDescriptor;
import org.mitre.icarus.cps.feature_vector.phase_1.FeatureVectorManager;
import org.mitre.icarus.cps.feature_vector.phase_1.GridSize;
import org.mitre.icarus.cps.feature_vector.phase_1.Road;
import org.mitre.icarus.cps.feature_vector.phase_1.SocintOverlay;
import org.mitre.icarus.cps.feature_vector.phase_1.SocintPolygon;
import org.mitre.icarus.cps.feature_vector.phase_1.TaskData;
import org.mitre.icarus.cps.feature_vector.phase_1.parser.RoadCsvParser;
import org.mitre.icarus.cps.feature_vector.phase_1.parser.RoadKmlParser;
import org.mitre.icarus.cps.feature_vector.phase_1.parser.TaskCsvParser;
import org.mitre.icarus.cps.feature_vector.phase_1.parser.TaskKmlParser;

/**
 * Binds exam and response classes to XML using JAXB for the Phase 1 CPD format.
 * 
 * @author CBONACETO
 *
 */
public class IcarusExamLoader_Phase1 extends IcarusExamLoader<IcarusExam_Phase1, TaskTestPhase<?>, TutorialPhase> {
	
	/** The exam schema */
	protected static Schema ExamSchema;
	
	/** The feature vector manager used to load and cache feature vector data */
	protected static FeatureVectorManager featureVectorManager = FeatureVectorManager.getInstance();
	
	/** Singleton instance of the exam loader */
	public static final IcarusExamLoader_Phase1 examLoaderInstance = new IcarusExamLoader_Phase1();
	
	private IcarusExamLoader_Phase1() {}
	
	/** Load the exam schema */
	static {
		try {
			SchemaFactory s = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			ClassLoader classLoader = IcarusExamLoader_Phase1.class.getClassLoader();
			ExamSchema = s.newSchema(classLoader.getResource("schemas/IcarusCPD_1_Schema.xsd"));				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.base.loader.IcarusExamLoaderBase#marshalExamToXml(org.mitre.icarus.cps.exam.base.IcarusExam)
	 */
	@Override
	public String marshalExamToXml(IcarusExam_Phase1 exam) throws JAXBException {
		return marshalExam(exam);
	}
	
	/**
	 * Marshalls an IcarusExam_Phase1 to XML.
	 * 
	 * @param exam the exam to marshall
	 * @return the exam XML string
	 * @throws JAXBException
	 */
	public static String marshalExam(IcarusExam_Phase1 exam) throws JAXBException {
		Marshaller marshaller = createMarshallerForClass(IcarusExam_Phase1.class);
		
		ByteArrayOutputStream output = new ByteArrayOutputStream (); 
		marshaller.marshal(exam, output);
		
		return output.toString();
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.base.loader.IcarusExamLoaderBase#unmarshalExamFromXml(java.net.URL, boolean)
	 */
	@Override
	public IcarusExam_Phase1 unmarshalExamFromXml(URL examXmlFile, boolean validate) throws JAXBException {
		return unmarshalExam(examXmlFile, validate);
	}
	
	/**
	 * Unmarshalls an IcarusExam from an XML file. Does not validate against the schema. 
	 *
	 * @param examFile the URL to the exam file
	 * @return an IcarusExam_Phase1 instance
	 * @throws JAXBException
	 */
	public static IcarusExam_Phase1 unmarshalExam(URL examFile) throws JAXBException {
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
	public static IcarusExam_Phase1 unmarshalExam(URL examFile, boolean validate) throws JAXBException {

		Unmarshaller unmarshaller = createUnmarshallerForClass(IcarusExam_Phase1.class, ExamSchema, validate);		
		
		IcarusExam_Phase1 exam = (IcarusExam_Phase1)(unmarshaller.unmarshal(examFile));
		if(exam != null) {
			exam.setOriginalPath(examFile);
			if(exam.getProbabilityRules() != null) {
				exam.getProbabilityRules().updateRulesMaps();
			}
		}		
		
		if(validate && unmarshaller.getEventHandler() != null && !unmarshaller.getEventHandler().toString().isEmpty()) {
			throw new JAXBException(unmarshaller.getEventHandler().toString());
		}
		
		return exam;
	}
	

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.base.loader.IcarusExamLoaderBase#unmarshalExamFromXml(java.lang.String, boolean)
	 */
	@Override
	public IcarusExam_Phase1 unmarshalExamFromXml(String examXmlString, boolean validate) throws JAXBException {
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
	public static IcarusExam_Phase1 unmarshalExam(String examXml, boolean validate) throws JAXBException {
		Unmarshaller unmarshaller = createUnmarshallerForClass(IcarusExam_Phase1.class, ExamSchema, validate);
		
		StringReader examXmlStream = new StringReader(examXml);
		IcarusExam_Phase1 exam = (IcarusExam_Phase1)(unmarshaller.unmarshal(examXmlStream));
		
		if(exam != null) {
			if(exam.getProbabilityRules() != null) {
				exam.getProbabilityRules().updateRulesMaps();
			}
		}		
		
		if(validate && unmarshaller.getEventHandler() != null && !unmarshaller.getEventHandler().toString().isEmpty()) {
			throw new JAXBException(unmarshaller.getEventHandler().toString());
		}
		
		return exam;
	}	

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.base.loader.IcarusExamLoaderBase#marshalExamPhase(org.mitre.icarus.cps.exam.base.IcarusExamPhase)
	 */
	@Override
	public String marshalExamPhase(TaskTestPhase<?> examPhase) throws JAXBException {
		return marshalTask(examPhase);
	}
	
	/**
	 * Marshalls a TaskTestPhase to XML.
	 * 
	 * @param task the task
	 * @return the task XML string
	 * @throws JAXBException
	 */
	public static String marshalTask(TaskTestPhase<?> task) throws JAXBException {
		Marshaller marshaller = createMarshallerForClass(task.getClass());
		
		ByteArrayOutputStream output = new ByteArrayOutputStream (); 
		marshaller.marshal(task, output);
		
		return output.toString();
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.base.loader.IcarusExamLoaderBase#unmarshalExampPhase(java.net.URL, boolean)
	 */
	@Override
	public TaskTestPhase<?> unmarshalExamPhase(URL examPhaseXmlFile, boolean validate) throws JAXBException {
		return unmarshalTask(examPhaseXmlFile, validate);
	}

	/**
	 * Unmarshalls a TaskTestPhase from an XML file and validates the file against the schema if validate is true.
	 * 
	 * @param taskFile the URL to the task file
	 * @param validate whether to validate the file against the schema
	 * @return a TaskTestPhase instance
	 * @throws JAXBException
	 */
	public static TaskTestPhase<?> unmarshalTask(URL taskFile, boolean validate) throws JAXBException {
		Unmarshaller unmarshaller = createUnmarshallerForClass(TaskTestPhase.class, ExamSchema, validate);		
		
		TaskTestPhase<?> task = (TaskTestPhase<?>)(unmarshaller.unmarshal(taskFile));
		
		if(validate && unmarshaller.getEventHandler() != null && !unmarshaller.getEventHandler().toString().isEmpty()) {
			throw new JAXBException(unmarshaller.getEventHandler().toString());
		}
		
		return task;
	}	
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.base.loader.IcarusExamLoaderBase#unmarshalExamPhase(java.lang.String, boolean)
	 */
	@Override
	public TaskTestPhase<?> unmarshalExamPhase(String examPhaseXml, boolean validate) throws JAXBException {
		return unmarshalTask(examPhaseXml, validate);
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.base.loader.IcarusExamLoader#marshalTutorialPhase(org.mitre.icarus.cps.exam.base.training.IcarusTutorialPhase)
	 */
	@Override
	public String marshalTutorialPhase(TutorialPhase tutorialPhase)
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
	public TutorialPhase unmarshalTutorialPhase(URL tutorialPhaseXmlFile,
			boolean validate) throws JAXBException {
		Unmarshaller unmarshaller = createUnmarshallerForClass(TutorialPhase.class, ExamSchema, validate);		
		
		TutorialPhase tutorial = (TutorialPhase)(unmarshaller.unmarshal(tutorialPhaseXmlFile));
		
		if(validate && unmarshaller.getEventHandler() != null && !unmarshaller.getEventHandler().toString().isEmpty()) {
			throw new JAXBException(unmarshaller.getEventHandler().toString());
		}
		
		return tutorial;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.base.loader.IcarusExamLoader#unmarshalTutorialPhase(java.lang.String, boolean)
	 */
	@Override
	public TutorialPhase unmarshalTutorialPhase(String tutorialPhaseXml,
			boolean validate) throws JAXBException {
		Unmarshaller unmarshaller = createUnmarshallerForClass(TutorialPhase.class, ExamSchema, validate);		
		
		StringReader tutorialXmlStream = new StringReader(tutorialPhaseXml);		
		TutorialPhase tutorial = (TutorialPhase)(unmarshaller.unmarshal(tutorialXmlStream));
		
		if(validate && unmarshaller.getEventHandler() != null && !unmarshaller.getEventHandler().toString().isEmpty()) {
			throw new JAXBException(unmarshaller.getEventHandler().toString());
		}
		
		return tutorial;
	}	

	/**
	 * Unmarshalls a TaskTestPhase from XML and validates the XML against the schema if validate is true.
	 * 
	 * @param taskXml a string containing the task XML
	 * @param validate whether to validate the XML against the schema
	 * @return a TaskTestPhase instance
	 * @throws JAXBException
	 */
	public static TaskTestPhase<?> unmarshalTask(String taskXml, boolean validate) throws JAXBException {
		Unmarshaller unmarshaller = createUnmarshallerForClass(TaskTestPhase.class, ExamSchema, validate);		
		
		StringReader taskXmlStream = new StringReader(taskXml);		
		TaskTestPhase<?> task = (TaskTestPhase<?>)(unmarshaller.unmarshal(taskXmlStream));
		
		if(validate && unmarshaller.getEventHandler() != null && !unmarshaller.getEventHandler().toString().isEmpty()) {
			throw new JAXBException(unmarshaller.getEventHandler().toString());
		}
		
		return task;
	}
	
	/**
	 * Marshals an IcarusTestTrial_Phase1 to XML.
	 * 
	 * @param trial the trial
	 * @return the trial XML string
	 * @throws JAXBException
	 */
	public static String marshalTrial(IcarusTestTrial_Phase1 trial) throws JAXBException {
		Marshaller marshaller = createMarshallerForClass(trial.getClass());
		
		ByteArrayOutputStream output = new ByteArrayOutputStream (); 
		marshaller.marshal(trial, output);
		
		return output.toString();
	}
	
	/**
	 * Unmarshalls an IcarusTestTrial_Phase1 from an XML file and validates the file against the schema if validate is true.
	 * 
	 * @param trialFile the URL to the trial file
	 * @param validate whether to validate the file against the schema
	 * @return an IcarusTestTrial_Phase1 instance
	 * @throws JAXBException
	 */
	public static IcarusTestTrial_Phase1 unmarshalTrial(URL trialFile, boolean validate) throws JAXBException {
		Unmarshaller unmarshaller = createUnmarshallerForClass(IcarusTestTrial_Phase1.class, ExamSchema, validate);		
		
		IcarusTestTrial_Phase1 trial = (IcarusTestTrial_Phase1)(unmarshaller.unmarshal(trialFile));
		
		if(validate && unmarshaller.getEventHandler() != null && !unmarshaller.getEventHandler().toString().isEmpty()) {
			throw new JAXBException(unmarshaller.getEventHandler().toString());
		}
		
		return trial;
	}
	
	/**
	 * Marshalls a TaskFeedback to XML.
	 * 
	 * @param taskFeedback the task feedback
	 * @return a task feedback XML string
	 * @throws JAXBException
	 */
	public static String marshalTaskFeedback(TaskFeedback taskFeedback) throws JAXBException {
		Marshaller marshaller = createMarshallerForClass(taskFeedback.getClass());
		
		ByteArrayOutputStream output = new ByteArrayOutputStream (); 
		marshaller.marshal(taskFeedback, output);
		
		return output.toString();
	}
	
	/**
	 * Unmarshalls a TaskFeedback from  an XML file and validates the file against the schema if validate is true.
	 * 
	 * @param taskFeedbackFile the URL to the task feedback file
	 * @param validate whether to validate the file against the schema
	 * @return a TaskFeedback instance
	 * @throws JAXBException
	 */
	public static TaskFeedback unmarshalTaskFeedback(URL taskFeedbackFile, boolean validate) throws JAXBException {
		Unmarshaller unmarshaller = createUnmarshallerForClass(TaskFeedback.class, ExamSchema, validate);		
		
		TaskFeedback taskFeedback = (TaskFeedback)(unmarshaller.unmarshal(taskFeedbackFile));
		
		if(validate && unmarshaller.getEventHandler() != null && !unmarshaller.getEventHandler().toString().isEmpty()) {
			throw new JAXBException(unmarshaller.getEventHandler().toString());
		}
		
		return taskFeedback;
	}
	
	/**
	 * Marshalls a TrialFeedback_Phase1 to XML.
	 * 
	 * @param trialFeedback the trial feedback
	 * @return the trial feedback XML string
	 * @throws JAXBException
	 */
	public static String marshalTrialFeedback(TrialFeedback_Phase1 trialFeedback) throws JAXBException {
		Marshaller marshaller = createMarshallerForClass(trialFeedback.getClass());
		
		ByteArrayOutputStream output = new ByteArrayOutputStream (); 
		marshaller.marshal(trialFeedback, output);
		
		return output.toString();
	}
	
	/**
	 * Unmarshalls a TrialFeedback_Phase1 from an XML file and validates the file against the schema if validate is true.
	 * 
	 * @param trialFeedbackFile the URL to the trial feedback file
	 * @param validate whether to validate the file against the scehma
	 * @return a TrialFeedback_Phase1 instance
	 * @throws JAXBException
	 */
	public static TrialFeedback_Phase1 unmarshalTrialFeedback(URL trialFeedbackFile, boolean validate) throws JAXBException {
		Unmarshaller unmarshaller = createUnmarshallerForClass(TrialFeedback_Phase1.class, ExamSchema, validate);		
		
		TrialFeedback_Phase1 trialFeedback = (TrialFeedback_Phase1)(unmarshaller.unmarshal(trialFeedbackFile));
		
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
	public static TrialFeedback_Phase1 unmarshalTrialFeedback(String trialFeedbackXml, boolean validate) throws JAXBException {
		Unmarshaller unmarshaller = createUnmarshallerForClass(TrialFeedback_Phase1.class, ExamSchema, validate);		
		
		StringReader trialFeedbackXmlStream = new StringReader(trialFeedbackXml);		
		TrialFeedback_Phase1 trialFeedback = (TrialFeedback_Phase1)(unmarshaller.unmarshal(trialFeedbackXmlStream));
		
		if(validate && unmarshaller.getEventHandler() != null && !unmarshaller.getEventHandler().toString().isEmpty()) {
			throw new JAXBException(unmarshaller.getEventHandler().toString());
		}
		
		return trialFeedback;
	}
	
	/**
	 * Populates the feature vector data for all trials in a task.
	 * 
	 * @param task the task
	 * @param baseUrl the URL to the location of the feature vector files if using relative URLs
	 * @param gridSize the grid size
	 * @param useKmlIfPresent whether to use the KML version of the feature vector files
	 * @param progressMonitor an optional progress monitor to track load progress
	 * @throws IOException
	 * @throws ParseException
	 * @throws URISyntaxException
	 */
	public static void initializeTaskFeatureVectorData(TaskTestPhase<?> task, URL baseUrl, 
			GridSize gridSize, boolean useKmlIfPresent,
			IProgressMonitor progressMonitor) throws IOException, ParseException, URISyntaxException {
		if(task != null) {
			if(task instanceof Task_1_2_3_PhaseBase) {
				initializeTaskFeatureVectorData(
						(Task_1_2_3_PhaseBase<?>)task, baseUrl, gridSize, useKmlIfPresent, progressMonitor);
			}
			else if(task instanceof Task_4_5_6_PhaseBase) {
				initializeTaskFeatureVectorData(
						(Task_4_5_6_PhaseBase<?>)task, baseUrl, gridSize, useKmlIfPresent, progressMonitor);
			}
			else if(task instanceof Task_7_Phase) {
				initializeTaskFeatureVectorData(
						(Task_7_Phase)task, baseUrl, gridSize, useKmlIfPresent, progressMonitor);
			}
			else {
				throw new IllegalArgumentException("Error loading feature vector data: unrecognized task type");
			}
		}
	}
	
	/**
	 * Populates the feature vector data for all trials in a Task 1, 2, or 3 phase.
	 * 
	 * @param task the task
	 * @param baseUrl the URL to the location of the feature vector files if using relative URLs
	 * @param gridSize the grid size
	 * @param useKmlIfPresent whether to use the KML version of the feature vector files
	 * @param progressMonitor an optional progress monitor to track load progress
	 * @throws IOException
	 * @throws ParseException
	 * @throws URISyntaxException
	 */
	public static void initializeTaskFeatureVectorData(Task_1_2_3_PhaseBase<?> task, URL baseUrl, 
			GridSize gridSize, boolean useKmlIfPresent, 
			IProgressMonitor progressMonitor) throws IOException, ParseException, URISyntaxException {
		
		if(progressMonitor != null) {
			progressMonitor.setMinimum(0);
			progressMonitor.setProgress(0);
		}
		
		if(task.getTrialBlocks() != null && !task.getTrialBlocks().isEmpty()) {
			if(progressMonitor != null) {
				progressMonitor.setMaximum(task.getTrialBlocks().size());
			}
			
			//Load roads for Task 3
			if(task instanceof Task_3_Phase) {
				FeatureVectorFileDescriptor roadsFile = ((Task_3_Phase)task).getRoadsFile();
				if(roadsFile != null) {
					ArrayList<Road> roads = loadRoads(roadsFile, baseUrl, gridSize, useKmlIfPresent);
					if(roads != null) {
						((Task_3_Phase)task).setRoads(roads);
					}
				}					
			}
			
			int blockNum = 1;
			//T extends Task_1_2_3_TrialBlockBase
			for(Task_1_2_3_TrialBlockBase block : task.getTrialBlocks()) {
				if(progressMonitor != null) {
					progressMonitor.setNote("Loading data for trial block: " + blockNum);
				}
				
				//Load attack presentation trial data
				if(block.getFeatureVectorFile() != null) {					
					TaskData taskData = loadTaskData(block.getFeatureVectorFile(), baseUrl, gridSize, useKmlIfPresent);
					if(taskData != null) {
						block.setTaskData(taskData);
					}
				}
				
				if(progressMonitor != null) {
					progressMonitor.setProgress(blockNum);
				}
				blockNum++;
			}
		}
		else if(progressMonitor != null) {
			progressMonitor.setMaximum(0);
		}
	}	
	
	/**
	 * Populates the feature vector data for all trials in a Task 4, 5, or 6 phase.
	 * 
	 * @param task the task
	 * @param baseUrl the URL to the location of the feature vector files if using relative URLs
	 * @param gridSize the grid size
	 * @param useKmlIfPresent whether to use the KML version of the feature vector files
	 * @param progressMonitor an optional progress monitor to track load progress
	 * @throws IOException
	 * @throws ParseException
	 * @throws URISyntaxException
	 */
	public static void initializeTaskFeatureVectorData(Task_4_5_6_PhaseBase<?> task, URL baseUrl, 
			GridSize gridSize, boolean useKmlIfPresent,
			IProgressMonitor progressMonitor) throws IOException, ParseException, URISyntaxException {
		
		if(progressMonitor != null) {
			progressMonitor.setMinimum(0);
			progressMonitor.setProgress(0);
		}
		
		if(task.getTestTrials() != null && !task.getTestTrials().isEmpty()) {
			if(progressMonitor != null) {
				progressMonitor.setMaximum(task.getTestTrials().size());
			}
			int trialNum = 1;
			for(Task_4_5_6_TrialBase trial : task.getTestTrials()) {
				if(progressMonitor != null) {
					progressMonitor.setNote("Loading data for trial: " + trialNum);
				}
				
				//Load attack location and group center trial data
				if(trial.getFeatureVectorFile() != null) {
					TaskData taskData = loadTaskData(
							trial.getFeatureVectorFile(), trial.getDistanceVectorFile(), baseUrl, gridSize, useKmlIfPresent);
					if(taskData != null) {
						trial.setTaskData(taskData);
					}
				}
				
				//Load roads
				if(trial.getRoadsFile() != null) {
					ArrayList<Road> roads = loadRoads(
							trial.getRoadsFile(), baseUrl, gridSize, useKmlIfPresent);
					if(roads != null) {
						trial.setRoads(roads);
					}
				}
				
				//Load regions (Now use the overlay format exclusively)
				if(trial.getRegionsFile() != null) {
					SocintOverlay regionsOverlay = loadRegionsOverlay(
							trial.getRegionsFile(), baseUrl, gridSize, useKmlIfPresent);
					if(regionsOverlay != null) {
						trial.setRegionsOverlay(regionsOverlay);
					}
					/*ArrayList<SocintPolygon> regions = loadRegions(
							trial.getRegionsFile(), baseUrl, gridSize, useKmlIfPresent);
					if(regions != null) {
						trial.setRegionBounds(regions);
					}*/
				}						
				
				if(progressMonitor != null) {
					progressMonitor.setProgress(trialNum);
				}
				
				trialNum++;
			}
		}
		else if(progressMonitor != null) {
			progressMonitor.setMaximum(0);
		}
	}
	
	/**
	 * Populates the feature vector data for all trials in a Task 7 phase.
	 * 
	 * @param task the task
	 * @param baseUrl the URL to the location of the feature vector files if using relative URLs
	 * @param gridSize the grid size
	 * @param useKmlIfPresent whether to use the KML version of the feature vector files
	 * @param progressMonitor an optional progress monitor to track load progress
	 * @throws IOException
	 * @throws ParseException
	 * @throws URISyntaxException
	 */
	public static void initializeTaskFeatureVectorData(Task_7_Phase task, URL baseUrl, 
			GridSize gridSize, boolean useKmlIfPresent, 
			IProgressMonitor progressMonitor) throws IOException, ParseException, URISyntaxException {
		
		if(progressMonitor != null) {
			progressMonitor.setMinimum(0);
			progressMonitor.setProgress(0);
		}
		
		if(task.getTestTrials() != null && !task.getTestTrials().isEmpty()) {
			if(progressMonitor != null) {
				progressMonitor.setNote("Loading data for Mission");
				progressMonitor.setMaximum(task.getTestTrials().size() + 1);
			}
			
			//Load group centers for task
			if(task.getFeatureVectorFile() != null) {					
				TaskData taskData = loadTaskData(
						task.getFeatureVectorFile(), baseUrl, gridSize, useKmlIfPresent);
				if(taskData != null) {
					task.setTaskData(taskData);
				}
			}					
			
			//Load roads for task
			if(task.getRoadsFile() != null) {
				ArrayList<Road> roads = loadRoads(
						task.getRoadsFile(), baseUrl, gridSize, useKmlIfPresent);
				if(roads != null) {
					task.setRoads(roads);
				}
			}
			
			//Load the regions for the task
			if(task.getRegionsFile() != null) {
				SocintOverlay regionsOverlay = loadRegionsOverlay(
						task.getRegionsFile(), baseUrl, gridSize, useKmlIfPresent);
				if(regionsOverlay != null) {
					task.setRegionsOverlay(regionsOverlay);
				}
			}			
			
			if(progressMonitor != null) {
				progressMonitor.setProgress(1);
			}
			
			int trialNum = 1;
			for(Task_7_Trial trial : task.getTestTrials()) {
				if(progressMonitor != null) {
					progressMonitor.setNote("Loading data for trial: " + trialNum);
				}
				
				//Load group center and attack location trial data
				if(trial.getFeatureVectorFile() != null) {					
					TaskData taskData = loadTaskData(
							trial.getFeatureVectorFile(), baseUrl, gridSize, useKmlIfPresent);
					if(taskData != null) {
						trial.setTaskData(taskData);
					}
				}						
				
				if(progressMonitor != null) {
					progressMonitor.setProgress(trialNum+1);
				}
				trialNum++;
			}
		}
		else if(progressMonitor != null) {
			progressMonitor.setMaximum(0);
		}
	}
	
	/**
	 * Loads a task feature vector file.
	 * 
	 * @param taskFile
	 * @param baseUrl
	 * @param gridSize
	 * @param useKmlIfPresent
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	protected static TaskData loadTaskData(FeatureVectorFileDescriptor taskFile, URL baseUrl, 
			GridSize gridSize, boolean useKmlIfPresent) throws IOException, ParseException  {
		return loadTaskData(taskFile, null, baseUrl, gridSize, useKmlIfPresent);
	}
	
	/**
	 * Loads a task feature vector file.
	 * 
	 * @param taskFile
	 * @param baseUrl
	 * @param gridSize
	 * @param useKmlIfPresent
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	protected static TaskData loadTaskData(FeatureVectorFileDescriptor taskFile, FeatureVectorFileDescriptor distanceFile, URL baseUrl, 
			GridSize gridSize, boolean useKmlIfPresent) throws IOException, ParseException  {
		
		if(taskFile.getFeatureVectorURL_KML() != null && useKmlIfPresent) {
			//DEBUG CODE
			//System.out.println("loading KML file: " + taskFile.getFeatureVectorURL_KML());
			if(baseUrl == null) {
				return featureVectorManager.getTaskData(
						new URL(taskFile.getFeatureVectorURL_KML()), gridSize);
			}
			else {
				return featureVectorManager.getTaskData(
						taskFile.getFeatureVectorURL_KML(),
						baseUrl, gridSize);
			}
		}
		else if(taskFile.getFeatureVectorURL_CSV() != null) {
			if(baseUrl == null) {
				return featureVectorManager.getTaskData(
						new URL(taskFile.getFeatureVectorURL_CSV()), 
						distanceFile != null ? new URL(distanceFile.getFeatureVectorURL_CSV()) : null,
						gridSize);
			}
			else {			
				return featureVectorManager.getTaskData(taskFile.getFeatureVectorURL_CSV(), 
						distanceFile != null ? distanceFile.getFeatureVectorURL_CSV() : null, 
						baseUrl, gridSize);
			}
		}
		else {
			throw new IOException("Error loading feature vector data: No CSV or KML URL was found in the file descriptor.");
		}
	}
	
	/**
	 * Loads a roads feature vector file.
	 * 
	 * @param roadsFile
	 * @param baseUrl
	 * @param gridSize
	 * @param useKmlIfPresent
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	protected static ArrayList<Road> loadRoads(FeatureVectorFileDescriptor roadsFile, URL baseUrl, 
			GridSize gridSize, boolean useKmlIfPresent) throws IOException, ParseException {
		
		if(roadsFile.getFeatureVectorURL_KML() != null && useKmlIfPresent) {
			if(baseUrl == null) {
				return featureVectorManager.getRoads(
						new URL(roadsFile.getFeatureVectorURL_KML()), gridSize);
			}
			else {
				return featureVectorManager.getRoads(
						roadsFile.getFeatureVectorURL_KML(),
						baseUrl, gridSize);
			}
		}
		else if(roadsFile.getFeatureVectorURL_CSV() != null) {
			if(baseUrl == null) {
				return featureVectorManager.getRoads(
						new URL(roadsFile.getFeatureVectorURL_CSV()), gridSize);
			}
			else {
				return featureVectorManager.getRoads(roadsFile.getFeatureVectorURL_CSV(), baseUrl, gridSize);
			}
		}		
		else {
			throw new IOException("Error loading feature vector data: No CSV or KML URL was found in the file descriptor.");
		}
	}
	
	/**
	 * Loads a regions overlay feature vector file.
	 * 
	 * @param regionsFile
	 * @param baseUrl
	 * @param gridSize
	 * @param useKmlIfPresent
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 * @throws URISyntaxException
	 */
	protected static SocintOverlay loadRegionsOverlay(FeatureVectorFileDescriptor regionsFile, URL baseUrl,
			GridSize gridSize, boolean useKmlIfPresent) throws IOException, ParseException, URISyntaxException {
		
		if(regionsFile.getFeatureVectorURL_KML() != null && useKmlIfPresent) {
			if(baseUrl == null) {
				return featureVectorManager.getRegionsOverlay(
						new URL(regionsFile.getFeatureVectorURL_KML()), gridSize);
			}
			else {
				return featureVectorManager.getRegionsOverlay(
						regionsFile.getFeatureVectorURL_KML(),
						baseUrl, gridSize);
			}
		}
		else if(regionsFile.getFeatureVectorURL_CSV() != null) {
			if(baseUrl == null) {
				return featureVectorManager.getRegionsOverlay(
						new URL(regionsFile.getFeatureVectorURL_CSV()), gridSize);
			}
			else {
				return featureVectorManager.getRegionsOverlay(
						regionsFile.getFeatureVectorURL_CSV(), baseUrl, gridSize);
			}
		}		
		else {
			throw new IOException("Error loading feature vector data: No CSV or KML URL was found in the file descriptor.");
		}
	}
	
	/**
	 * Loads a region feature vector file.
	 * 
	 * @param regionsFile
	 * @param baseUrl
	 * @param gridSize
	 * @param useKmlIfPresent
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 * @throws URISyntaxException
	 */
	protected static ArrayList<SocintPolygon> loadRegions(FeatureVectorFileDescriptor regionsFile, URL baseUrl,
			GridSize gridSize, boolean useKmlIfPresent) throws IOException, ParseException, URISyntaxException {
		
		if(regionsFile.getFeatureVectorURL_KML() != null && useKmlIfPresent) {
			if(baseUrl == null) {
				return featureVectorManager.getRegions(
						new URL(regionsFile.getFeatureVectorURL_KML()), gridSize);
			}
			else {
				return featureVectorManager.getRegions(
						regionsFile.getFeatureVectorURL_KML(),
						baseUrl, gridSize);
			}
		}
		else if(regionsFile.getFeatureVectorURL_CSV() != null) {
			if(baseUrl == null) {
				return featureVectorManager.getRegions(
						new URL(regionsFile.getFeatureVectorURL_CSV()), gridSize);
			}
			else {
				return featureVectorManager.getRegions(regionsFile.getFeatureVectorURL_CSV(), baseUrl, gridSize);
			}
		}		
		else {
			throw new IOException("Error loading feature vector data: No CSV or KML URL was found in the file descriptor.");
		}
	}
	
	/** Test main */
	public static void main(String[] args) {		
		try {
			//TaskTestPhase task = unmarshalTask(new File("data/Phase_1_CPD/SampleTask.xml").toURI().toURL(), false);
			//System.out.println((Task_1_Phase)task);			
			
			IcarusExam_Phase1 exam = unmarshalExam(new File("data/Phase_1_CPD/Examples/SampleExam_withResponses.xml").toURI().toURL(), true);
			System.out.println(marshalExam(exam));
			
			TaskCsvParser task_csv = new TaskCsvParser("data/Phase_1_CPD/Examples/task7_phase.csv", exam.getGridSize());
			TaskKmlParser task_kml = new TaskKmlParser(task_csv);
			System.out.println(task_kml.getKmlString());
			
			RoadCsvParser road_csv = new RoadCsvParser("data/Phase_1_CPD/Examples/roads.csv", exam.getGridSize());
			RoadKmlParser road_kml = new RoadKmlParser(road_csv);
			System.out.println(road_kml.getKmlString());
			
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}	
}