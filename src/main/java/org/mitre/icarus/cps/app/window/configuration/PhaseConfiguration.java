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
package org.mitre.icarus.cps.app.window.configuration;

import java.util.EnumSet;
import java.util.List;

import javax.swing.JMenu;

import org.mitre.icarus.cps.app.experiment.IcarusExamController;
import org.mitre.icarus.cps.app.experiment.IcarusExperimentController;
import org.mitre.icarus.cps.app.window.IApplicationWindow.WindowAlignment;
import org.mitre.icarus.cps.app.window.configuration.ApplicationConfiguration.ExamControlOption;
import org.mitre.icarus.cps.app.window.configuration.ApplicationConfiguration.ExamOpenOption;
import org.mitre.icarus.cps.app.window.configuration.ApplicationConfiguration.FeatureVectorOpenOption;
import org.mitre.icarus.cps.exam.base.IcarusExam;
import org.mitre.icarus.cps.exam.base.IcarusExamPhase;
import org.mitre.icarus.cps.exam.base.playback.ExamPlaybackDataSource;

/**
 * Contains configuration settings for a Program phase (e.g., Phase 1, Phase 2).
 * 
 * 
 * @author CBONACETO
 *
 */
public abstract class PhaseConfiguration<
	E extends IcarusExam<?>, 
	P extends IcarusExamPhase,
	EPDS extends ExamPlaybackDataSource<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>,
	EC extends IcarusExamController<E, ?, ?, ?, ?>,
	PC extends IcarusExperimentController<EPDS, ?, ?, ?>> {	
	
	/** The phase ID (1-3) */
	protected String phaseId;
	
	/** The phase name */
	protected String phaseName;
	
	/** The name for tasks in this phase (e.g., Mission) */
	protected String taskTypeName = "Mission";
	
	/** The exam open options enabled for the phase */
	protected EnumSet<ExamOpenOption> examOpenOptions;
	
	/** The feature vector open options enabled for the phase */
	protected EnumSet<FeatureVectorOpenOption> featureVectorOpenOptions;
	
	/** The exam file type for the phase */
	protected FileDescriptor examFileType;
	
	/** The exam playback file types for the phase */
	protected List<FileDescriptor> examPlaybackFileTypes;
	
	/** The feature vector file types for the phase */
	protected List<FileDescriptor> featureVectorFileTypes;
	
	/** The exam control options enabled for the phase for exams open for data collection or demonstration */
	protected EnumSet<ExamControlOption> examDataCollectionControlOptions;
	
	/** The exam control options enabled for the phase for exams open for demonstration */
	protected EnumSet<ExamControlOption> examDemonstrationControlOptions;
	
	/** The exam control options enabled for the phase for exams open for play-back */
	protected EnumSet<ExamControlOption> examPlaybackControlOptions;	
	
	/** The current default exam file (if any) */
	protected FileLocation defaultExamFile;
	
	/** The last exam file that was opened for data collection */
	protected FileLocation examFile;
	
	/** The last set of exam files that were opened for playback */
	protected List<FileLocation> examPlaybackFiles;
	
	/** The last feature vector file that was opened */	
	protected FileLocation featureVectorFile;
	
	/** The default location of exam files */
	protected FileLocation defaultExamFileLocation;
	
	/** The default location of playback files */
	protected List<FileLocation> defaultPlaybackFileLocations;
	
	/** Contains any additional feature vector menu items for the phase (non-standard control items). 
	 * These menu items will be displayed above the standard feature vector control items with a separator 
	 * at the bottom when the feature vector menu is enabled */
	protected JMenu customFeatureVectorMenuItems;
	
	/** Contains any additional help menu items for the phase. These menu items will be displayed above the standard
	 * help menu items with a separator at the bottom */
	protected JMenu customHelpMenuItems;
	
	/** The default window alignment when showing an exam for data collection or demonstration */
	protected WindowAlignment examDataCollectionWindowAlignment = WindowAlignment.CENTER;
	
	/** The default window alignment when showing an exam for playback */
	protected WindowAlignment examPlaybackWindowAlignment = WindowAlignment.CENTER;
	
	/** The default window alignment when showing a feature vector file */
	protected WindowAlignment featureVectorWindowAlignment = WindowAlignment.CENTER;
	
	protected PhaseConfiguration() {}

	public String getPhaseId() {
		return phaseId;
	}

	protected void setPhaseId(String phaseId) {
		this.phaseId = phaseId;
	}
	
	public String getPhaseName() {
		return phaseName;
	}

	protected void setPhaseName(String phaseName) {
		this.phaseName = phaseName;
	}

	public String getTaskTypeName() {
		return taskTypeName;
	}

	protected void setTaskTypeName(String taskTypeName) {
		this.taskTypeName = taskTypeName;
	}

	public EnumSet<ExamOpenOption> getExamOpenOptions() {
		return examOpenOptions;
	}

	protected void setExamOpenOptions(EnumSet<ExamOpenOption> examOpenOptions) {
		this.examOpenOptions = examOpenOptions;
	}

	public EnumSet<FeatureVectorOpenOption> getFeatureVectorOpenOptions() {
		return featureVectorOpenOptions;
	}

	protected void setFeatureVectorOpenOptions(EnumSet<FeatureVectorOpenOption> featureVectorOpenOptions) {
		this.featureVectorOpenOptions = featureVectorOpenOptions;
	}

	public FileDescriptor getExamFileType() {
		return examFileType;
	}

	protected void setExamFileType(FileDescriptor examFileType) {
		this.examFileType = examFileType;
	}

	public List<FileDescriptor> getExamPlaybackFileTypes() {
		return examPlaybackFileTypes;
	}

	protected void setExamPlaybackFileTypes(List<FileDescriptor> examPlaybackFileTypes) {
		this.examPlaybackFileTypes = examPlaybackFileTypes;
	}

	public List<FileDescriptor> getFeatureVectorFileTypes() {
		return featureVectorFileTypes;
	}

	protected void setFeatureVectorFileTypes(List<FileDescriptor> featureVectorFileTypes) {
		this.featureVectorFileTypes = featureVectorFileTypes;
	}

	public EnumSet<ExamControlOption> getExamDataCollectionControlOptions() {
		return examDataCollectionControlOptions;
	}

	protected void setExamDataCollectionControlOptions(EnumSet<ExamControlOption> examDataCollectionControlOptions) {
		this.examDataCollectionControlOptions = examDataCollectionControlOptions;
	}

	public EnumSet<ExamControlOption> getExamDemonstrationControlOptions() {
		return examDemonstrationControlOptions;
	}

	protected void setExamDemonstrationControlOptions(EnumSet<ExamControlOption> examDemonstrationControlOptions) {
		this.examDemonstrationControlOptions = examDemonstrationControlOptions;
	}

	public EnumSet<ExamControlOption> getExamPlaybackControlOptions() {
		return examPlaybackControlOptions;
	}

	protected void setExamPlaybackControlOptions(EnumSet<ExamControlOption> examPlaybackControlOptions) {
		this.examPlaybackControlOptions = examPlaybackControlOptions;
	}	

	public FileLocation getDefaultExamFile() {
		return defaultExamFile;
	}

	public void setDefaultExamFile(FileLocation defaultExamFile) {
		this.defaultExamFile = defaultExamFile;
	}
	
	public FileLocation getExamFile() {
		return examFile;
	}

	public void setExamFile(FileLocation examFile) {
		this.examFile = examFile;
	}

	public List<FileLocation> getExamPlaybackFiles() {
		return examPlaybackFiles;
	}

	public void setExamPlaybackFiles(List<FileLocation> examPlaybackFiles) {
		this.examPlaybackFiles = examPlaybackFiles;
	}

	public FileLocation getFeatureVectorFile() {
		return featureVectorFile;
	}

	public void setFeatureVectorFile(FileLocation featureVectorFile) {
		this.featureVectorFile = featureVectorFile;
	}

	public FileLocation getDefaultExamFileLocation() {
		return defaultExamFileLocation;
	}

	protected void setDefaultExamFileLocation(FileLocation defaultExamFileLocation) {
		this.defaultExamFileLocation = defaultExamFileLocation;
	}

	public List<FileLocation> getDefaultPlaybackFileLocations() {
		return defaultPlaybackFileLocations;
	}

	protected void setDefaultPlaybackFileLocations(List<FileLocation> defaultPlaybackFileLocations) {
		this.defaultPlaybackFileLocations = defaultPlaybackFileLocations;
	}

	public JMenu getCustomFeatureVectorMenuItems() {
		return customFeatureVectorMenuItems;
	}

	public void setCustomFeatureVectorMenuItems(JMenu customFeatureVectorMenuItems) {
		this.customFeatureVectorMenuItems = customFeatureVectorMenuItems;
	}

	public JMenu getCustomHelpMenuItems() {
		return customHelpMenuItems;
	}

	public void setCustomHelpMenuItems(JMenu customHelpMenuItems) {
		this.customHelpMenuItems = customHelpMenuItems;
	}

	public WindowAlignment getExamDataCollectionWindowAlignment() {
		return examDataCollectionWindowAlignment;
	}

	protected void setExamDataCollectionWindowAlignment(WindowAlignment examDataCollectionWindowAlignment) {
		this.examDataCollectionWindowAlignment = examDataCollectionWindowAlignment;
	}

	public WindowAlignment getExamPlaybackWindowAlignment() {
		return examPlaybackWindowAlignment;
	}

	protected void setExamPlaybackWindowAlignment(WindowAlignment examPlaybackWindowAlignment) {
		this.examPlaybackWindowAlignment = examPlaybackWindowAlignment;
	}

	public WindowAlignment getFeatureVectorWindowAlignment() {
		return featureVectorWindowAlignment;
	}

	protected void setFeatureVectorWindowAlignment(WindowAlignment featureVectorWindowAlignment) {
		this.featureVectorWindowAlignment = featureVectorWindowAlignment;
	}
}