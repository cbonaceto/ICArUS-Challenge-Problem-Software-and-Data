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
package org.mitre.icarus.cps.app.window.phase_1;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.mitre.icarus.cps.app.experiment.IcarusExperimentController;
import org.mitre.icarus.cps.app.experiment.data_recorder.ISubjectDataRecorder;
import org.mitre.icarus.cps.app.experiment.phase_1.IcarusExamController_Phase1;
import org.mitre.icarus.cps.app.experiment.phase_1.player.PlayerController;
import org.mitre.icarus.cps.app.widgets.map.phase_1.MapPanelContainer;
import org.mitre.icarus.cps.app.widgets.map.phase_1.PlacemarkFactory.AttackLocationType;
import org.mitre.icarus.cps.app.widgets.phase_1.dialog.GridSizeDlg;
import org.mitre.icarus.cps.app.widgets.phase_1.experiment.ConditionPanel_Phase1;
import org.mitre.icarus.cps.app.widgets.phase_1.experiment.ExperimentPanel_Phase1;
import org.mitre.icarus.cps.app.widgets.phase_1.player.ConditionPanel_Player;
import org.mitre.icarus.cps.app.widgets.phase_1.player.ExperimentPanel_Player;
import org.mitre.icarus.cps.app.widgets.progress_monitor.IProgressMonitor;
import org.mitre.icarus.cps.app.window.IApplicationWindow;
import org.mitre.icarus.cps.app.window.configuration.FileDescriptor;
import org.mitre.icarus.cps.app.window.configuration.FileLocation;
import org.mitre.icarus.cps.app.window.controller.PhaseController;
import org.mitre.icarus.cps.assessment.data_model.phase_1.data_sets.AverageHumanDataSet_Phase1;
import org.mitre.icarus.cps.assessment.persistence.phase_1.xml.XMLCPADataPersister;
import org.mitre.icarus.cps.assessment.score_computer.phase_1.ScoreComputer;
import org.mitre.icarus.cps.exam.base.loader.IcarusExamLoader;
import org.mitre.icarus.cps.exam.phase_1.IcarusExam_Phase1;
import org.mitre.icarus.cps.exam.phase_1.loader.IcarusExamLoader_Phase1;
import org.mitre.icarus.cps.exam.phase_1.playback.ExamPlaybackDataSource_Phase1;
import org.mitre.icarus.cps.exam.phase_1.testing.TaskTestPhase;
import org.mitre.icarus.cps.exam.phase_1.training.TutorialPhase;
import org.mitre.icarus.cps.experiment_core.gui.ConditionPanel.BannerOrientation;
import org.mitre.icarus.cps.feature_vector.phase_1.FeatureVectorManager;
import org.mitre.icarus.cps.feature_vector.phase_1.GridSize;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupAttack;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupCenter;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupType;
import org.mitre.icarus.cps.feature_vector.phase_1.Road;
import org.mitre.icarus.cps.feature_vector.phase_1.SocintOverlay;
import org.mitre.icarus.cps.feature_vector.phase_1.TaskData;

/**
 * Controller for the Phase 1 Challenge Problem.
 * 
 * @author CBONACETO
 *
 */
public class Phase_1_Controller extends 
	PhaseController<MapPanelContainer, Phase_1_Configuration, IcarusExam_Phase1, TaskTestPhase<?>, 
	TutorialPhase, ExamPlaybackDataSource_Phase1, IcarusExamController_Phase1, PlayerController> {
	
	/** The grid size to use when viewing feature vector data */
	protected GridSize gridSize;	
	
	/** The name of the current task feature vector file */
	protected String taskFileName = "";
	
	/** The name of the current roads feature vector file */
	protected String roadFileName = "";	
	
	/** The name of the current regions feature vector file */
	protected String regionFileName = "";
	
	public Phase_1_Controller(Phase_1_Configuration phaseConfiguration, IApplicationWindow applicationWindow,
			ISubjectDataRecorder<IcarusExam_Phase1, TaskTestPhase<?>, TutorialPhase> examDataRecorder, 
			boolean showReturnHomeButton) {
		super(phaseConfiguration, applicationWindow, examDataRecorder, showReturnHomeButton);
		gridSize = new GridSize();
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.app.window.configuration.PhaseController#advanceExamToTrial(org.mitre.icarus.cps.app.experiment.IcarusExperimentController, int, int)
	 */
	@Override
	protected void advanceExamToTrial(IcarusExperimentController<?, ?, ?, ?> activeExamController, int trialNumber) 
			throws IllegalArgumentException {
		if(activeExamController == null || !(activeExamController instanceof PlayerController)) {
			throw new IllegalArgumentException("Error, advancing to a trial in the exam is not supported.");
		}		
		((PlayerController)activeExamController).advanceToTrial(trialNumber);
	}

	@Override
	protected IcarusExamLoader<IcarusExam_Phase1, TaskTestPhase<?>, TutorialPhase> createExamLoader() {
		return IcarusExamLoader_Phase1.examLoaderInstance;
	}

	@Override
	protected JMenu createCustomFeatureVectorMenuItems(final Component parent) {
		JMenu customFeatureVectorMenuItems = new JMenu("");
		JMenuItem gridSizeItem = new JMenuItem("Set Grid Size");
		gridSizeItem.setMnemonic(KeyEvent.VK_G);
		customFeatureVectorMenuItems.add(gridSizeItem);
		gridSizeItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				//Show grid size dialog
				GridSize newGridSize = GridSizeDlg.showDialog(parent, "Set Grid Size", gridSize); 
				if(newGridSize != null) {
					gridSize = newGridSize;
					if(getMapPanel() != null) {
						getMapPanel().setGridSize(gridSize);
					}
				}
			}
		});		
		return customFeatureVectorMenuItems;
	}

	@Override
	protected JMenu createCustomHelpMenuItems(Component parent) {
		//Does nothing, no custom help menu items
		return null;
	}

	@Override
	protected MapPanelContainer createMapPanel(Component parent) {
		return new MapPanelContainer(parent, gridSize, true, true);
	}

	@Override
	protected IcarusExamController_Phase1 createControllerForExamDataCollection(Component parent,
			boolean showReturnHomeButton, 
			ISubjectDataRecorder<IcarusExam_Phase1, TaskTestPhase<?>, TutorialPhase> dataRecorder) {
		ConditionPanel_Phase1 conditionPanel = new ConditionPanel_Phase1(parent, true, BannerOrientation.Top);		
		IcarusExamController_Phase1 examController = new IcarusExamController_Phase1(new ScoreComputer(), dataRecorder);
		//ExperimentPanel_Phase1 experimentPanel = new ExperimentPanel_Phase1(parent, conditionPanel, applicationOptions.isClientMode());
		ExperimentPanel_Phase1 experimentPanel = new ExperimentPanel_Phase1(parent, conditionPanel, showReturnHomeButton);
		examController.setExperimentPanel(experimentPanel);		
		//experimentPanel.addSubjectActionListener(this);
		return examController;
	}

	@Override
	protected void configureExamControllerForDataCollection(IcarusExamController_Phase1 examController) {
		//Does nothing		
	}

	@Override
	protected void configureExamControllerForDemonstration(IcarusExamController_Phase1 examController) {
		//Does nothing
	}

	@Override
	protected PlayerController createControllerForExamPlayback(Component parent, boolean showReturnHomeButton) {
		ConditionPanel_Player conditionPanel = new ConditionPanel_Player(parent);
		PlayerController examPlayerController = new PlayerController();			
		ExperimentPanel_Player experimentPanel = new ExperimentPanel_Player(parent, conditionPanel);
		examPlayerController.setExperimentPanel(experimentPanel);
		return examPlayerController;
	}

	@Override
	public IcarusExam_Phase1 loadExamForDataCollectionOrDemonstration(FileLocation examFile,
			boolean validate, IProgressMonitor progressMonitor) throws Exception {
		IcarusExam_Phase1 exam = examLoader.unmarshalExamFromXml(examFile.getFileUrl(), validate);
		if(exam != null) {
			exam.setOriginalPath(examFile.getFileUrl());
		}
		return exam;
	}

	@Override
	public ExamPlaybackDataSource_Phase1 loadExamForPlayback(List<FileLocation> examPlaybackFiles, 
			IProgressMonitor progressMonitor) throws Exception {
		if(examPlaybackFiles != null && !examPlaybackFiles.isEmpty()) {
			URL examFileUrl = null;
			URL examResponseFileUrl = null;
			URL avgHumanDataSetFileUrl = null;
			for(FileLocation file : examPlaybackFiles) {
				if(file != null && file.getFileType() != null) {
					switch(file.getFileType().getFileTypeId()) {
					case Phase_1_Configuration.EXAM_FILE_TYPE:
						examFileUrl = file.getFileUrl();
						break;
					case Phase_1_Configuration.EXAM_RESPONSE_FILE_TYPE:
						examResponseFileUrl = file.getFileUrl();
						break;
					case Phase_1_Configuration.AVG_HUMAN_FILE_TYPE:
						avgHumanDataSetFileUrl = file.getFileUrl();
						break;
					}
				}
			}
			if(examFileUrl == null) {
				throw new Exception("No exam file was specified");
			}
			if(examResponseFileUrl == null) {
				throw new Exception("No exam response file was specified");
			}			
			
			//Load the exam
			IcarusExam_Phase1 exam = examLoader.unmarshalExamFromXml(examFileUrl, false);
			if(exam != null) {		
				exam.setOriginalPath(examFileUrl);
				if(exam.getTasks() != null && !exam.getTasks().isEmpty()) {
					for(TaskTestPhase<?> task : exam.getTasks()) {
						IcarusExamLoader_Phase1.initializeTaskFeatureVectorData(task, 
							exam.getOriginalPath(), exam.getGridSize(), false, progressMonitor);
					}
				}
			}
			
			//Load the exam response
			IcarusExam_Phase1 participantExamResponse = examLoader.unmarshalExamFromXml(examResponseFileUrl, false);
			
			//Load the average human data set if present
			AverageHumanDataSet_Phase1 avgHumanResponse = null;
			if(avgHumanDataSetFileUrl != null) {
				avgHumanResponse = XMLCPADataPersister.loadAverageHumanDataSet(avgHumanDataSetFileUrl);
			}
			
			ExamPlaybackDataSource_Phase1 examResponse = new ExamPlaybackDataSource_Phase1();
			
			//Initialize the exam response object and compute metrics
			examResponse.initializeExamResponseData(exam, participantExamResponse, avgHumanResponse, 
					avgHumanResponse != null ? avgHumanResponse.getMetricsInfo() : null);
			//this.examFileURL = examFileUrl;
			//this.examResponseFileURL = examResponseFileUrl;
			//this.avgHumanDataSetFileURL = avgHumanDataSetUrl;
			return examResponse;
		}
		return null;
	}

	@Override
	public Object openFeatureVectorFile(FileLocation featureVectorFile,
			boolean validate, IProgressMonitor progressMonitor) throws Exception {
		URL featureVectorFileUrl = featureVectorFile.getFileUrl();
		switch(featureVectorFile.getFileType().getFileTypeId()) {
		case Phase_1_Configuration.TASK_FEATURE_VECTOR_FILE_TYPE:
			return FeatureVectorManager.getInstance().getTaskData(featureVectorFileUrl, gridSize);
		case Phase_1_Configuration.ROADS_FEATURE_VECTOR_FILE_TYPE:	
			return FeatureVectorManager.getInstance().getRoads(featureVectorFileUrl, gridSize);
		case Phase_1_Configuration.REGIONS_FEATURE_VECTOR_FILE_TYPE :
			return FeatureVectorManager.getInstance().getRegionsOverlay(featureVectorFileUrl, gridSize);
		}
		return null;
	}

	@Override
	protected List<String> showFeatureVectorData(MapPanelContainer mapPanel, FileLocation featureVectorFile, 
			IProgressMonitor progressMonitor) throws Exception {
		//Exception e = null;
		//Only show scale bar in Tasks 3-7
		boolean showScaleBar = true;
		TaskData taskData = null;
		ArrayList<Road> roads = null;
		SocintOverlay regions = null;

		try {
			URL featureVectorFileUrl = featureVectorFile.getFileUrl();
			String fileName = new File(featureVectorFile.getFileUrl().toURI()).getName();
			switch(featureVectorFile.getFileType().getFileTypeId()) {
			case Phase_1_Configuration.TASK_FEATURE_VECTOR_FILE_TYPE:
				taskData = FeatureVectorManager.getInstance().getTaskData(featureVectorFileUrl, gridSize);
				taskFileName = fileName;
				showScaleBar = (taskFileName.contains("3_") || taskFileName.contains("4_") || taskFileName.contains("5_") ||
						taskFileName.contains("6_") || taskFileName.contains("7_") ||
						taskData.getAttacks() == null || taskData.getAttacks().size() <= 4);				
				break;
			case Phase_1_Configuration.ROADS_FEATURE_VECTOR_FILE_TYPE:	
				roads = FeatureVectorManager.getInstance().getRoads(featureVectorFileUrl, gridSize);				
				roadFileName = fileName;
				break;
			case Phase_1_Configuration.REGIONS_FEATURE_VECTOR_FILE_TYPE :
				regions = FeatureVectorManager.getInstance().getRegionsOverlay(featureVectorFileUrl, gridSize);
				regionFileName = fileName;
				break;
			}			
			//updateFrameTitleForFeatureVectorData();
		} catch(Exception e1) {
			throw new Exception("The feature vector file contained errors", e1);
			//e = e1;
		}    				

		/*if(e != null) {
					ValidationErrorsDlg.showErrorDialog(frame, 
							"Feature Vector Not Valid", 
							"The feature vector file contained errors: ",
							e.getMessage());							
					return false;
				} else {*/
		mapPanel.setShowScale(showScaleBar);
		if(taskData != null) {
			boolean imintFound = false;
			boolean movintFound = false;
			boolean sigintFound = false;
			Set<GroupAttack> locationsFound = new TreeSet<GroupAttack>(new GroupAttackComparator());
			Set<GroupType> groupsFound = new TreeSet<GroupType>();

			if(taskData.getAttacks() != null && !taskData.getAttacks().isEmpty()) {
				mapPanel.setSigactLayerEnabled(true);
				mapPanel.setSigactLocations(taskData.getAttacks(), AttackLocationType.GROUP_ATTACK, true);
				for(GroupAttack attack : taskData.getAttacks()) {
					locationsFound.add(attack);
					if(attack.getIntelReport() != null) {
						if(attack.getIntelReport().getImintInfo() != null) {
							imintFound = true;
						}
						if(attack.getIntelReport().getMovintInfo() != null) {
							movintFound = true;
						}
						if(attack.getIntelReport().getSigintInfo() != null) {
							sigintFound = true;
						}
					}
				}
				mapPanel.setSigactLocationsInLegend(locationsFound);
				mapPanel.setSigactsLegendItemVisible(true);
			} else {
				mapPanel.setSigactLayerEnabled(false);
				mapPanel.setSigactsLegendItemVisible(false);
			}

			if(taskData.getCenters() != null && !taskData.getCenters().isEmpty()) {
				mapPanel.setGroupCentersLayerEnabled(true);
				mapPanel.setGroupCenters(taskData.getCenters(), true);
				for(GroupCenter groupCenter : taskData.getCenters()) {
					groupsFound.add(groupCenter.getGroup());
					if(groupCenter.getIntelReport() != null) {
						if(groupCenter.getIntelReport().getImintInfo() != null) {
							imintFound = true;
						}
						if(groupCenter.getIntelReport().getMovintInfo() != null) {
							movintFound = true;
						}
						if(groupCenter.getIntelReport().getSigintInfo() != null) {
							sigintFound = true;
						}
					}
				}
				mapPanel.setGroupCenterGroupsForLegend(groupsFound);
				mapPanel.setGroupCentersLegendItemVisible(true);									
			} else {
				mapPanel.setGroupCentersLayerEnabled(false);
				mapPanel.setGroupCentersLegendItemVisible(false);
			}								

			mapPanel.setImintLayerEnabled(imintFound);
			mapPanel.setImintLegendItemVisible(imintFound);

			mapPanel.setMovintLayerEnabled(movintFound);
			mapPanel.setMovintLegendItemVisible(movintFound);

			mapPanel.setSigintLayerEnabled(sigintFound);
			mapPanel.setSigintLegendItemVisible(sigintFound);
		}

		if(roads != null) {
			mapPanel.setRoadLayerEnabled(true);								
			mapPanel.setRoads(roads);
			mapPanel.setRoadsLegendItemVisible(true);
		}

		if(regions != null) {
			mapPanel.setSocintRegionsLayerEnabled(true);
			mapPanel.setSocintRegionsOverlay(regions);
			mapPanel.setSocintGroupsForLegend(regions.getGroups());								
			mapPanel.setSocintLegendItemVisible(true);
		}

		mapPanel.redrawMap();		

		//contentPanel.removeAll();
		//contentPanel.add(mapPanel);
		//contentPanel.validate();
		//contentPanel.repaint();			
		//return true;
		//}
		return createOpenFeatureVectorFilesList();
	}

	@Override
	protected List<String> clearFeatureVectorData(MapPanelContainer mapPanel, FileDescriptor featureVectorType) {
		if(featureVectorType != null) {
			switch(featureVectorType.getFileTypeId()) {
			case Phase_1_Configuration.TASK_FEATURE_VECTOR_FILE_TYPE:
				clearTaskData(mapPanel);		
				break;
			case Phase_1_Configuration.ROADS_FEATURE_VECTOR_FILE_TYPE:	
				clearRoadData(mapPanel);
				break;
			case Phase_1_Configuration.REGIONS_FEATURE_VECTOR_FILE_TYPE :
				clearRegionData(mapPanel);
				break;
			}
		}
		return createOpenFeatureVectorFilesList();
	}

	@Override
	protected void clearAllFeatureVectorData(MapPanelContainer mapPanel) {
		taskFileName = "";
		roadFileName = "";
		regionFileName = "";
		//frame.setTitle(APPLICATION_NAME);
		mapPanel.resetMap();
	}
	
	private List<String> createOpenFeatureVectorFilesList() {
		List<String> files = new LinkedList<String>();
		if(!taskFileName.isEmpty()) {
			files.add(taskFileName);
		}
		if(!roadFileName.isEmpty()) {
			files.add(roadFileName);
		}
		if(!regionFileName.isEmpty()) {
			files.add(regionFileName);
		}
		return files;
		/*StringBuilder title = new StringBuilder(APPLICATION_NAME);
		if(!taskFileName.isEmpty() || !roadFileName.isEmpty() || !regionFileName.isEmpty()) {
			title.append(" - ");
			boolean commaNeeded = false;
			if(!taskFileName.isEmpty()) {
				title.append(taskFileName);
				commaNeeded = true;
			}
			if(!roadFileName.isEmpty()) {
				if(commaNeeded) {
					title.append(", ");
				}
				title.append(roadFileName);
				commaNeeded = true;
			}
			if(!regionFileName.isEmpty()) {
				if(commaNeeded) {
					title.append(", ");
				}
				title.append(regionFileName);
			}
		}
		frame.setTitle(title.toString());*/
	}
	
	private void clearTaskData(MapPanelContainer mapPanel) {
		taskFileName = "";
		//updateFrameTitleForFeatureVectorData();
		mapPanel.removeAllSigactLocations();
		mapPanel.setSigactLayerEnabled(false);
		mapPanel.setSigactLocationsInLegend(null);
		mapPanel.setSigactsLegendItemVisible(false);
		
		mapPanel.removeAllGroupCenters();					
		mapPanel.setGroupCentersLayerEnabled(false);
		mapPanel.setGroupCenterGroupsForLegend(null);
		mapPanel.setGroupCentersLegendItemVisible(false);
		
		mapPanel.setImintLayerEnabled(false);
		mapPanel.setImintLegendItemVisible(false);
		
		mapPanel.setMovintLayerEnabled(false);
		mapPanel.setMovintLegendItemVisible(false);
		
		mapPanel.setSigintLayerEnabled(false);
		mapPanel.setSigintLegendItemVisible(false);		
		mapPanel.redrawMap();
	}
	
	private void clearRoadData(MapPanelContainer mapPanel) {
		roadFileName = "";
		mapPanel.removeAllRoads();
		mapPanel.setRoadLayerEnabled(false);
		mapPanel.setRoadsLegendItemVisible(false);
		mapPanel.redrawMap();
	}
	
	private void clearRegionData(MapPanelContainer mapPanel) {
		regionFileName = "";
		mapPanel.removeAllSocintRegions();
		mapPanel.setSocintRegionsLayerEnabled(false);
		mapPanel.setSocintGroupsForLegend(null);
		mapPanel.setSocintLegendItemVisible(false);
		mapPanel.redrawMap();
	}
	
	/**
	 * @author CBONACETO
	 *
	 */
	protected static class GroupAttackComparator implements Comparator<GroupAttack> {
		@Override
		public int compare(GroupAttack ga0, GroupAttack ga1) {
			int groupCompare = 0;
			if(ga0.getGroup() == null)  {
				if(ga1.getGroup() == null) {
					groupCompare = 0;
				}
				else {
					groupCompare = -1;
				}
			}
			else if(ga1.getGroup() == null) {
				groupCompare = 1;
			}
			else {			
				groupCompare = ga0.getGroup().compareTo(ga1.getGroup());
			}			
			
			int locationCompare = 0;
			if(ga0.getLocation() == null || ga0.getLocation().getLocationId() == null)  {
				if(ga1.getLocation() == null || ga1.getLocation().getLocationId() == null) {
					locationCompare = 0;
				}
				else {
					locationCompare = -1;
				}
			}
			else if(ga1.getLocation() == null || ga1.getLocation().getLocationId() == null) {
				locationCompare = 1;
			}
			else {			
				locationCompare = ga0.getLocation().getLocationId().compareTo(ga1.getLocation().getLocationId());
			}		
			
			if(groupCompare == 0) {
				return locationCompare;
			}
			return groupCompare;
		}
	}
}