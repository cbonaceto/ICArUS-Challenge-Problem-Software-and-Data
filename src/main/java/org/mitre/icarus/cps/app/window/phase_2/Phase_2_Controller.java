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
package org.mitre.icarus.cps.app.window.phase_2;

import java.awt.Component;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JMenu;

import org.mitre.icarus.cps.app.experiment.IcarusExperimentController;
import org.mitre.icarus.cps.app.experiment.data_recorder.ISubjectDataRecorder;
import org.mitre.icarus.cps.app.experiment.phase_1.player.PlayerController;
import org.mitre.icarus.cps.app.experiment.phase_2.IcarusExamController_Phase2;
import org.mitre.icarus.cps.app.widgets.map.phase_2.MapConstants_Phase2;
import org.mitre.icarus.cps.app.widgets.map.phase_2.MapPanelContainer;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.BlueLocationPlacemark;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.BlueLocationPlacemark.DisplayMode;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.BlueLocationPlacemark.DisplaySize;
import org.mitre.icarus.cps.app.widgets.phase_2.experiment.ConditionPanel_Phase2;
import org.mitre.icarus.cps.app.widgets.phase_2.experiment.ExperimentPanel_Phase2;
import org.mitre.icarus.cps.app.widgets.progress_monitor.IProgressMonitor;
import org.mitre.icarus.cps.app.window.IApplicationWindow;
import org.mitre.icarus.cps.app.window.configuration.FileDescriptor;
import org.mitre.icarus.cps.app.window.configuration.FileLocation;
import org.mitre.icarus.cps.app.window.controller.PhaseController;
import org.mitre.icarus.cps.exam.base.loader.IcarusExamLoader;
import org.mitre.icarus.cps.exam.phase_1.playback.ExamPlaybackDataSource_Phase1;
import org.mitre.icarus.cps.exam.phase_2.IcarusExam_Phase2;
import org.mitre.icarus.cps.exam.phase_2.loader.IcarusExamLoader_Phase2;
import org.mitre.icarus.cps.exam.phase_2.testing.DatumType;
import org.mitre.icarus.cps.exam.phase_2.testing.Mission;
import org.mitre.icarus.cps.exam.phase_2.training.IcarusExamTutorial_Phase2;
import org.mitre.icarus.cps.experiment_core.gui.ConditionPanel.BannerOrientation;
import org.mitre.icarus.cps.feature_vector.phase_2.AreaOfInterest;
import org.mitre.icarus.cps.feature_vector.phase_2.BlueLocation;
import org.mitre.icarus.cps.feature_vector.phase_2.FeatureContainer;
import org.mitre.icarus.cps.feature_vector.phase_2.loader.FeatureVectorLoader;


/**
 * Controller for the Phase 2 Challenge Problem.
 * 
 * @author CBONACETO
 *
 */
public class Phase_2_Controller extends 
	PhaseController<MapPanelContainer, Phase_2_Configuration, IcarusExam_Phase2, Mission<?>, 
	IcarusExamTutorial_Phase2, ExamPlaybackDataSource_Phase1, IcarusExamController_Phase2, PlayerController> {		

	/** The name of the current AOI feature vector file */
	protected String aoiFileName = "";

	/** The name of the current Blue locations feature vector file */
	protected String blueLocationsFileName = "";
	
	public Phase_2_Controller(Phase_2_Configuration phaseConfiguration, IApplicationWindow applicationWindow,
			ISubjectDataRecorder<IcarusExam_Phase2, Mission<?>, IcarusExamTutorial_Phase2> examDataRecorder, 
			boolean showReturnHomeButton) {
		super(phaseConfiguration, applicationWindow, examDataRecorder, showReturnHomeButton);
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.app.window.configuration.PhaseController#advanceExamToTrial(org.mitre.icarus.cps.app.experiment.IcarusExperimentController, int, int)
	 */
	@Override
	protected void advanceExamToTrial(IcarusExperimentController<?, ?, ?, ?> activeExamController, int trialNumber) 
			throws IllegalArgumentException {
		/*if(activeExamController == null || !(activeExamController instanceof PlayerController)) {
			throw new IllegalArgumentException("Error, advancing to a trial in the exam is not supported.");
		}*/
		if(activeExamController != null) {
			if(activeExamController instanceof IcarusExamController_Phase2) {
				((IcarusExamController_Phase2)activeExamController).advanceToTrial(trialNumber);
			} else if(activeExamController instanceof PlayerController) {
				((PlayerController)activeExamController).advanceToTrial(trialNumber);
			}
		}
	}

	@Override
	protected IcarusExamLoader<IcarusExam_Phase2, Mission<?>, IcarusExamTutorial_Phase2> createExamLoader() {
		return IcarusExamLoader_Phase2.examLoaderInstance;
	}

	@Override
	protected JMenu createCustomFeatureVectorMenuItems(final Component parent) {
		//Does nothing, no custom feature vector menu items
		return null;
	}

	@Override
	protected JMenu createCustomHelpMenuItems(Component parent) {
		//Does nothing, no custom help menu items
		return null;
	}

	@Override
	protected MapPanelContainer createMapPanel(Component parent) {		
		MapPanelContainer map = new MapPanelContainer(parent, true, false);
		map.setShowScaleBar(false);
		map.setPanEnabled(false);
		map.setZoomEnabled(false);
		map.setBlueLocationsLayerSelectable(true);
		map.setMultipleToolTipsEnabled(true);
		return map;
	}

	@Override
	protected IcarusExamController_Phase2 createControllerForExamDataCollection(
			Component parent, boolean showReturnHomeButton, 
			ISubjectDataRecorder<IcarusExam_Phase2, Mission<?>, IcarusExamTutorial_Phase2> dataRecorder) {
		ConditionPanel_Phase2 conditionPanel = new ConditionPanel_Phase2(parent, true, BannerOrientation.Top, true);
		IcarusExamController_Phase2 examController = new IcarusExamController_Phase2(dataRecorder);
		ExperimentPanel_Phase2 experimentPanel = new ExperimentPanel_Phase2(parent, conditionPanel, 
				showReturnHomeButton, BannerOrientation.Top, BannerOrientation.Top);
		examController.setExperimentPanel(experimentPanel);
		return examController;
	}

	@Override
	protected void configureExamControllerForDataCollection(IcarusExamController_Phase2 examController) {
		if(examController != null) {
			examController.setClearResponseData(true);
		}
	}

	@Override
	protected void configureExamControllerForDemonstration(IcarusExamController_Phase2 examController) {
		if(examController != null) {
			examController.setClearResponseData(false);
		}
	}

	@Override
	protected PlayerController createControllerForExamPlayback(Component parent, boolean showReturnHomeButton) {
		//TODO: Implement this
		return null;
		/*ConditionPanel_Player conditionPanel = new ConditionPanel_Player(parent);
		PlayerController examPlayerController = new PlayerController();			
		ExperimentPanel_Player experimentPanel = new ExperimentPanel_Player(parent, conditionPanel);
		examPlayerController.setExperimentPanel(experimentPanel);
		return examPlayerController;*/
	}

	@Override
	public IcarusExam_Phase2 loadExamForDataCollectionOrDemonstration(FileLocation examFile,
			boolean validate, IProgressMonitor progressMonitor) throws Exception {
		IcarusExam_Phase2 exam = examLoader.unmarshalExamFromXml(examFile.getFileUrl(), validate);
		if(exam != null) {
			exam.setOriginalPath(examFile.getFileUrl());
		}
		return exam;
	}

	@Override
	public ExamPlaybackDataSource_Phase1 loadExamForPlayback(List<FileLocation> examPlaybackFiles, 
			IProgressMonitor progressMonitor) throws Exception {
		//TODO: Implement this
		return null;
		/*
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
		return null;*/
	}
	
	@Override
	public Object openFeatureVectorFile(FileLocation featureVectorFile,
			boolean validate, IProgressMonitor progressMonitor) throws Exception {
		URL featureVectorFileUrl = featureVectorFile.getFileUrl();
		switch(featureVectorFile.getFileType().getFileTypeId()) {
		case Phase_2_Configuration.AREA_OF_INTEREST_VECTOR_FILE_TYPE:
			return FeatureVectorLoader.getInstance().unmarshalAreaOfInterest(featureVectorFileUrl, validate);
		case Phase_2_Configuration.BLUE_LOCATIONS_FEATURE_VECTOR_FILE_TYPE:	
			return FeatureVectorLoader.getInstance().unmarshalBlueLocations(featureVectorFileUrl, validate);
		}
		return null;
	}

	//@SuppressWarnings("unused")
	@Override
	protected List<String> showFeatureVectorData(MapPanelContainer mapPanel, FileLocation featureVectorFile, 
			IProgressMonitor progressMonitor) throws Exception {
		AreaOfInterest aoi = null;
		FeatureContainer<BlueLocation> blueLocations = null;
		try {
			URL featureVectorFileUrl = featureVectorFile.getFileUrl();
			String fileName = new File(featureVectorFile.getFileUrl().toURI()).getName();
			switch(featureVectorFile.getFileType().getFileTypeId()) {
			case Phase_2_Configuration.AREA_OF_INTEREST_VECTOR_FILE_TYPE:
				aoi = FeatureVectorLoader.getInstance().unmarshalAreaOfInterest(featureVectorFileUrl, false);
				aoiFileName = fileName;
				if(aoi != null && aoi.getSceneImageFile() != null 
						&& aoi.getSceneImageFile().getFileUrl() != null) {
					//Load the scene image
					aoi.setSceneImage(ImageIO.read(IcarusExamLoader_Phase2.createUrl(
							featureVectorFileUrl, aoi.getSceneImageFile().getFileUrl())));
				}
				//DEBUG CODE
				/*double width_degrees = Math.abs(aoi.getBottomRightLon() - aoi.getTopLeftLon());
				double height_degrees = Math.abs(aoi.getBottomRightLat() - aoi.getTopLeftLat());				
				double width_miles = GeoUtils.computeDistance_miles(new GeoCoordinate(aoi.getTopLeftLon(), aoi.getTopLeftLat()),
						new GeoCoordinate(aoi.getBottomRightLon(), aoi.getTopLeftLat()));
				double height_miles = GeoUtils.computeDistance_miles(new GeoCoordinate(aoi.getTopLeftLon(), aoi.getTopLeftLat()),
						new GeoCoordinate(aoi.getTopLeftLon(), aoi.getBottomRightLat()));
				System.out.println("Width: " + width_degrees + " degrees, " + width_miles + " miles");
				System.out.println("Height: " + height_degrees + " degrees, " + height_miles + " miles");*/
				//END DEBUG CODE
				break;
			case Phase_2_Configuration.BLUE_LOCATIONS_FEATURE_VECTOR_FILE_TYPE:
				blueLocations = FeatureVectorLoader.getInstance().unmarshalBlueLocations(featureVectorFileUrl, false);
				blueLocationsFileName = fileName;
				break;
			}
		} catch(Exception e1) {
			throw new Exception("The feature vector file contained errors", e1);
		}		

		if(aoi != null) {
			mapPanel.setAOILayerEnabled(true);
			mapPanel.setBlueRegionLayerEnabled(true);
			mapPanel.setAOI(aoi);
			mapPanel.setBlueRegion(aoi != null ? aoi.getBlueRegion() : null);
		}

		if(blueLocations != null) {
			if(mapPanel.getAOI() == null) {
				throw new Exception("Please open an area of interest before adding Blue locations.");
			} else {
				mapPanel.setBlueLocationsLayerEnabled(true);
				mapPanel.setIntAnnotationTextLayerEnabled(MapConstants_Phase2.SHOW_OSINT_TEXT || 
						MapConstants_Phase2.SHOW_IMINT_TEXT);
				mapPanel.setOsintLineLayerEnabled(MapConstants_Phase2.SHOW_OSINT_LINE);
				mapPanel.setImintCircleLayerEnabled(MapConstants_Phase2.SHOW_IMINT_CIRCLE);
				mapPanel.setSigintLayerEnabled(true);			
				Map<String, BlueLocationPlacemark> placemarks = mapPanel.addBlueLocations(blueLocations.getFeatureList(), 
						createBlueLocationNames(blueLocations), true, 
						DisplayMode.StandardMode, DisplaySize.StandardSize, false);
				if(placemarks != null) {
					for(BlueLocationPlacemark placemark : placemarks.values()) {
						mapPanel.addIntToBlueLocation(placemark, DatumType.OSINT, false, true);					
						mapPanel.addIntToBlueLocation(placemark, DatumType.IMINT, false, true);
						mapPanel.addIntToBlueLocation(placemark, DatumType.SIGINT, false, true);
					}
				}
			}
		}
		mapPanel.redrawMap();
		return createOpenFeatureVectorFilesList();
	}
	
	protected List<String> createBlueLocationNames(FeatureContainer<BlueLocation> blueLocations) {
		if(blueLocations != null && !blueLocations.isEmpty()) {
			ArrayList<String> locationNames = new ArrayList<String>(blueLocations.size());
			for(BlueLocation location : blueLocations) {
				locationNames.add(location.getId());
			}
			return locationNames;
		}
		return null;
	}

	@Override
	protected List<String> clearFeatureVectorData(MapPanelContainer mapPanel, FileDescriptor featureVectorType) {
		if(featureVectorType != null) {
			switch(featureVectorType.getFileTypeId()) {
			case Phase_2_Configuration.BLUE_LOCATIONS_FEATURE_VECTOR_FILE_TYPE:
				mapPanel.removeAllBlueLocations();
				mapPanel.setBlueLocationsLayerEnabled(false);
				mapPanel.setIntAnnotationTextLayerEnabled(false);
				mapPanel.setOsintLineLayerEnabled(false);
				mapPanel.setImintCircleLayerEnabled(false);
				mapPanel.setSigintLayerEnabled(false);	
				mapPanel.redrawMap();
				break;
			case Phase_2_Configuration.AREA_OF_INTEREST_VECTOR_FILE_TYPE:
				mapPanel.setAOI(null);
				mapPanel.setAOILayerEnabled(false);
				mapPanel.setBlueRegionLayerEnabled(false);
				mapPanel.redrawMap();
				break;
			}
		}
		return createOpenFeatureVectorFilesList();
	}

	@Override
	protected void clearAllFeatureVectorData(MapPanelContainer mapPanel) {
		aoiFileName = "";
		blueLocationsFileName = "";
		mapPanel.resetMap();
	}

	private List<String> createOpenFeatureVectorFilesList() {
		List<String> files = new LinkedList<String>();
		if(!aoiFileName.isEmpty()) {
			files.add(aoiFileName);
		}
		if(!blueLocationsFileName.isEmpty()) {
			files.add(blueLocationsFileName);
		}
		return files;
	}	
}