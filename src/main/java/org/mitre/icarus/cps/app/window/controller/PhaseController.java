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
package org.mitre.icarus.cps.app.window.controller;

import java.awt.Component;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JMenu;

import org.mitre.icarus.cps.app.experiment.IcarusExamController;
import org.mitre.icarus.cps.app.experiment.IcarusExperimentController;
import org.mitre.icarus.cps.app.experiment.IcarusSubjectData;
import org.mitre.icarus.cps.app.experiment.data_recorder.ISubjectDataRecorder;
import org.mitre.icarus.cps.app.widgets.progress_monitor.IProgressMonitor;
import org.mitre.icarus.cps.app.window.IApplicationWindow;
import org.mitre.icarus.cps.app.window.configuration.FileDescriptor;
import org.mitre.icarus.cps.app.window.configuration.FileLocation;
import org.mitre.icarus.cps.app.window.configuration.PhaseConfiguration;
import org.mitre.icarus.cps.exam.base.IcarusExam;
import org.mitre.icarus.cps.exam.base.IcarusExamPhase;
import org.mitre.icarus.cps.exam.base.loader.IcarusExamLoader;
import org.mitre.icarus.cps.exam.base.playback.ExamPlaybackDataSource;
import org.mitre.icarus.cps.exam.base.response.ResponseGeneratorData;
import org.mitre.icarus.cps.exam.base.training.IcarusTutorialPhase;
import org.mitre.icarus.cps.experiment_core.event.SubjectActionListener;
import org.mitre.icarus.cps.web.model.Site;

/**
 * 
 * 
 * @author CBONACETO
 *
 * @param <E>
 * @param <P>
 * @param <EPDS>
 * @param <EC>
 * @param <PC>
 */
public abstract class PhaseController<
	FVD extends JComponent,
	PC extends PhaseConfiguration<E, P, EPDS, EDC, EPC>,	
	E extends IcarusExam<?>, 
	P extends IcarusExamPhase,
	T extends IcarusTutorialPhase<?>,
	EPDS extends ExamPlaybackDataSource<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>,
	EDC extends IcarusExamController<E, P, T, ?, ?>,
	EPC extends IcarusExperimentController<EPDS, ?, ?, ?>> {
	
	/** The phase configuration */
	protected final PC phaseConfiguration;
	
	/** Reference to the application window */
	protected final IApplicationWindow applicationWindow;
	
	/** Component for showing feature vector data */
	private FVD mapPanel;
	
	/** The exam loader for the phase */	
	protected final IcarusExamLoader<E, P, T> examLoader;
	
	/** Data recorder to use for exam data collection */
	private final ISubjectDataRecorder<E, P, T> examDataRecorder;
	
	/** Whether to show the return to home button in the navigation panel */
	//TODO: We should probably discontinue this and put the return to home button in an application level GUI component or menu
	private final boolean showReturnHomeButton;
	
	/** The exam controller for the phase (for exam data collection) */	
	private EDC examController;	
	
	/** The exam playback controller for the phase (for exam playback) */
	private EPC examPlaybackController;
	
	/** The current active exam controller */
	private IcarusExperimentController<?, ?, ?, ?> activeExamController;
	
	private SubjectActionListener subjectActionListener;
	
	public PhaseController(PC phaseConfiguration, IApplicationWindow applicationWindow,
			ISubjectDataRecorder<E, P, T> examDataRecorder,
			boolean showReturnHomeButton) {
		this.phaseConfiguration = phaseConfiguration;
		this.applicationWindow = applicationWindow;
		this.examDataRecorder = examDataRecorder;
		this.showReturnHomeButton = showReturnHomeButton;
		this.examLoader = createExamLoader();
		phaseConfiguration.setCustomFeatureVectorMenuItems(createCustomFeatureVectorMenuItems(applicationWindow.getWindowComponent()));
		phaseConfiguration.setCustomHelpMenuItems(createCustomHelpMenuItems(applicationWindow.getWindowComponent()));		
	}
	
	public PC getPhaseConfiguration() {
		return phaseConfiguration;
	}	
	
	/**
	 * @return
	 */
	public EDC getExamController(boolean collectData) {
		if(examController == null) {
			examController = createControllerForExamDataCollection(
					applicationWindow.getWindowComponent(), showReturnHomeButton, examDataRecorder);
		}
		if(collectData) {
			configureExamControllerForDataCollection(examController);
		} else {
			configureExamControllerForDemonstration(examController);
		}
		return examController;
	}	
	
	/**
	 * @return
	 */
	public EPC getExamPlaybackController() {
		if(examPlaybackController == null) {
			examPlaybackController = createControllerForExamPlayback(applicationWindow.getWindowComponent(), showReturnHomeButton);
		}
		return examPlaybackController;
	}
	
	/**
	 * @return
	 */
	public IcarusExperimentController<?, ?, ?, ?> getActiveExamController() {
		return activeExamController;
	}
	
	/**
	 * @return
	 */
	public boolean isExamRunning() {
		return activeExamController != null && activeExamController.isExperimentRunning();
	}

	/**
	 * @param examFile
	 * @param collectData
	 * @param subjectData
	 * @param progressMonitor
	 * @return
	 * @throws Exception
	 */
	public JComponent showExamForDataCollectionOrDemonstration(FileLocation examFile, boolean collectData, 
			IcarusSubjectData subjectData, ApplicationController appController,
			IProgressMonitor progressMonitor) throws Exception {
		stopExam();
		E exam = loadExamForDataCollectionOrDemonstration(examFile, false, progressMonitor);
		EDC examController = getExamController(collectData);		
		examController.setApplicationController(appController);
		examController.setDataRecorder(collectData ? examDataRecorder : null);
		examController.initializeExperimentController(exam);
		if(subjectActionListener != null &&
				!examController.getExperimentPanel().isSubjectActionListenerPresent(subjectActionListener)) {
			examController.getExperimentPanel().addSubjectActionListener(subjectActionListener);
		}
		examController.startExperiment(subjectData);
		activeExamController = examController;
		return examController.getExperimentPanel().getExperimentPanelComponent();
	}
	
	/**
	 * @param examPlaybackFiles
	 * @param subjectData
	 * @param progressMonitor
	 * @return
	 * @throws Exception
	 */			
	public JComponent showExamForPlayback(List<FileLocation> examPlaybackFiles, ApplicationController appController,
			IProgressMonitor progressMonitor) throws Exception {
		stopExam();
		EPDS exam = loadExamForPlayback(examPlaybackFiles, progressMonitor);
		EPC examPlaybackController = getExamPlaybackController();
		examPlaybackController.setApplicationController(appController);
		examPlaybackController.initializeExperimentController(exam);
		String subjectId = "?";
		String siteId = "?";
		ResponseGeneratorData responseGenerator = exam.getResponseGeneratorData();
		if(responseGenerator != null) {
			subjectId = responseGenerator.getResponseGeneratorId() != null ? responseGenerator.getResponseGeneratorId() : subjectId;
			siteId = responseGenerator.getSiteId() != null ? responseGenerator.getSiteId() : siteId;
		}		
		if(subjectActionListener != null && 
			!examPlaybackController.getExperimentPanel().isSubjectActionListenerPresent(subjectActionListener)) {
			examPlaybackController.getExperimentPanel().addSubjectActionListener(subjectActionListener);
		}	
		examPlaybackController.startExperiment(new IcarusSubjectData(subjectId, new Site(siteId, siteId), 0, -1));	
		activeExamController = examPlaybackController;		
		return examPlaybackController.getExperimentPanel().getExperimentPanelComponent();
	}		
	
	/**
	 * @param subjectActionListener
	 */
	public void setSubjectActionListener(SubjectActionListener subjectActionListener) {
		if(this.subjectActionListener != subjectActionListener) {
			try {
				if(examController != null) {
					examController.getExperimentPanel().removeSubjectActionListener(this.subjectActionListener);
				}
				if(examPlaybackController != null) {
					examPlaybackController.getExperimentPanel().removeSubjectActionListener(this.subjectActionListener);
				}
			} catch(Exception ex) {}
			if(activeExamController != null && subjectActionListener != null) {
				try {
					if(!activeExamController.getExperimentPanel().isSubjectActionListenerPresent(subjectActionListener)) {
						activeExamController.getExperimentPanel().addSubjectActionListener(subjectActionListener);
					}
				} catch(Exception ex) {}
			}
			this.subjectActionListener = subjectActionListener;
		}
	}	
	
	public void advanceExamToPhase(int phaseIndex) {
		if(activeExamController != null) {
			activeExamController.skipToCondition(phaseIndex);
		}
	}	
	
	public void advanceExamToTrialInCurrentPhase(int trialNumber) {
		if(activeExamController != null) {
			advanceExamToTrial(activeExamController, trialNumber);
		}
	}
	
	protected abstract void advanceExamToTrial(IcarusExperimentController<?, ?, ?, ?> activeExamController, int trialNumber);	
	
	public void restartExam() {
		if(activeExamController != null) {
			activeExamController.restartExperiment();
		}
	}
	
	public void stopExam() {
		try {
			if(examController != null) {
				examController.stopExperiment();
				if(subjectActionListener != null) {
					examController.getExperimentPanel().removeSubjectActionListener(subjectActionListener);
				}
			}
			if(examPlaybackController != null) {
				examPlaybackController.stopExperiment();
				if(subjectActionListener != null) {
					examPlaybackController.getExperimentPanel().removeSubjectActionListener(subjectActionListener);
				}
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void changeExamParticipant(String subjectId, Site site) {
		if(examController != null) {
			examController.setSubjectData(subjectId, site);
		}
		if(examPlaybackController != null) {
			examPlaybackController.setSubjectData(subjectId, site);
		}
	}
	
	/**
	 * @return
	 */
	public FVD getMapPanel() {	
		if(mapPanel == null) {
			mapPanel = createMapPanel(applicationWindow.getWindowComponent());
		}
		return mapPanel;
	}

	protected abstract IcarusExamLoader<E, P, T> createExamLoader();
	
	protected abstract JMenu createCustomFeatureVectorMenuItems(Component parent);	
	
	protected abstract JMenu createCustomHelpMenuItems(Component parent);
	
	protected abstract FVD createMapPanel(Component parent);
	
	protected abstract EDC createControllerForExamDataCollection(Component parent, boolean showReturnHomeButton, 
			ISubjectDataRecorder<E, P, T> dataRecorder);
	
	protected abstract void configureExamControllerForDataCollection(EDC examController);	
	protected abstract void configureExamControllerForDemonstration(EDC examController);
	
	protected abstract EPC createControllerForExamPlayback(Component parent, boolean showReturnHomeButton);	
	
	public abstract E loadExamForDataCollectionOrDemonstration(FileLocation examFile, boolean validate, 
			IProgressMonitor progressMonitor) throws Exception;
	
	public abstract EPDS loadExamForPlayback(List<FileLocation> examPlaybackFiles, 
			IProgressMonitor progressMonitor) throws Exception;
	
	public abstract Object openFeatureVectorFile(FileLocation featureVectorFile, boolean validate, 
			IProgressMonitor progressMonitor) throws Exception;
	
	public List<String> showFeatureVectorData(FileLocation featureVectorFile, IProgressMonitor progressMonitor) throws Exception {
		if(mapPanel == null) {
			mapPanel = createMapPanel(applicationWindow.getWindowComponent());
		}
		return showFeatureVectorData(mapPanel, featureVectorFile, progressMonitor);
	}
	
	protected abstract List<String> showFeatureVectorData(FVD mapPanel, FileLocation featureVectorFile, IProgressMonitor progressMonitor) throws Exception;
	
	public List<String> clearFeatureVectorData(FileDescriptor featureVectorType) {
		if(mapPanel != null) {
			return clearFeatureVectorData(mapPanel, featureVectorType);
		} else {
			return null;
		}
	}
	
	protected abstract List<String> clearFeatureVectorData(FVD mapPanel, FileDescriptor featureVectorType);
	
	public void clearAllFeatureVectorData() {
		if(mapPanel != null) {
			clearAllFeatureVectorData(mapPanel);
		}
	}
	
	protected abstract void clearAllFeatureVectorData(FVD mapPanel);	
}