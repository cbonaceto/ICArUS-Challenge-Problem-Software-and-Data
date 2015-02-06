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
package org.mitre.icarus.cps.app.widgets.phase_05.experiment;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;

import org.mitre.icarus.cps.app.experiment.phase_05.LayerSelectTrialState;
import org.mitre.icarus.cps.app.experiment.phase_05.TrialState;
import org.mitre.icarus.cps.app.experiment.phase_05.TrialState.TrialType;
import org.mitre.icarus.cps.app.widgets.WidgetConstants;
import org.mitre.icarus.cps.app.widgets.basic.BreakPanel;
import org.mitre.icarus.cps.app.widgets.dialog.ErrorDlg;
import org.mitre.icarus.cps.app.widgets.map.phase_05.FeatureVector;
import org.mitre.icarus.cps.app.widgets.map.phase_05.FeatureVectorPanel;
import org.mitre.icarus.cps.app.widgets.map.phase_05.Layer;
import org.mitre.icarus.cps.app.widgets.map.phase_05.LegendPanel;
import org.mitre.icarus.cps.app.widgets.map.phase_05.Sector;
import org.mitre.icarus.cps.app.widgets.map.phase_05.Layer.LayerType;
import org.mitre.icarus.cps.app.widgets.phase_05.ImageManager;
import org.mitre.icarus.cps.app.widgets.probability_entry.ProbabilityEntryConstants;
import org.mitre.icarus.cps.app.widgets.probability_entry.distinct_bars.ProbabilityBoxContainer;
import org.mitre.icarus.cps.app.widgets.probability_entry.distinct_bars.BoxContainer.BoxOrientation;
import org.mitre.icarus.cps.app.widgets.probability_entry.distinct_bars.SettingsBoxWithDecorators.EditControlType;
import org.mitre.icarus.cps.exam.base.IcarusExam.NormalizationMode;
import org.mitre.icarus.cps.exam.base.testing.IcarusTestTrial;
import org.mitre.icarus.cps.exam.base.testing.IcarusTestTrial.TestTrialType;
import org.mitre.icarus.cps.exam.phase_05.IcarusExam_Phase05;
import org.mitre.icarus.cps.exam.phase_05.response.LayerData;
import org.mitre.icarus.cps.exam.phase_05.testing.assessment.AssessmentTrial;
import org.mitre.icarus.cps.exam.phase_05.testing.assessment.EvidenceElement;
import org.mitre.icarus.cps.exam.phase_05.testing.scene_presentation.SceneItem;
import org.mitre.icarus.cps.exam.phase_05.testing.scene_presentation.ScenePresentationTrial;
import org.mitre.icarus.cps.exam.phase_05.testing.scene_presentation.question_types.IdentifyItemQuestion;
import org.mitre.icarus.cps.exam.phase_05.testing.scene_presentation.question_types.LocateItemQuestion;
import org.mitre.icarus.cps.exam.phase_05.testing.scene_presentation.question_types.Question;
import org.mitre.icarus.cps.exam.phase_05.training.Annotation;
import org.mitre.icarus.cps.exam.phase_05.training.AnnotationGridTrainingTrial;
import org.mitre.icarus.cps.exam.phase_05.training.AnnotationTrainingTrial;
import org.mitre.icarus.cps.exam.phase_05.training.IcarusTrainingTrial;
import org.mitre.icarus.cps.exam.phase_05.training.RuleTrainingTrial;
import org.mitre.icarus.cps.exam.phase_05.training.IcarusTrainingTrial.TrainingTrialType;
import org.mitre.icarus.cps.feature_vector.phase_05.FeatureVectorManager;
import org.mitre.icarus.cps.feature_vector.phase_05.ParsedPaletteObject;
import org.mitre.icarus.cps.experiment_core.gui.ConditionPanel;
import org.mitre.icarus.cps.experiment_core.gui.IConditionComponent;
import org.mitre.icarus.cps.experiment_core.gui.JPanelConditionComponent;

/**
 * Condition panel for test and training phases in an exam.
 * 
 * @author Jing Hu and Craig Bonaceto
 */
@SuppressWarnings("deprecation")
public class ConditionPanel_Phase05 extends ConditionPanel {
	private static final long serialVersionUID = 1L;
	
	private Window parentWindow;
	
	/** The current exam */
	private IcarusExam_Phase05 exam;
	
	/** The current identify item question */
	private IdentifyItemQuestion identify;
	
	/** The current locate item question */
	private LocateItemQuestion locate;
	
	/** The current assessment trial */
	private AssessmentTrial assessment;
	
	/** Content panel contains all sub-panels */
	private JPanelConditionComponent contentPanel;	
	
	/** Contains the layers tree and legend */
	private JPanel leftPanel;
	
	/** Contains the probability entry components */
	private JPanel rightPanel;
	
	/** Panel with the scene */
	private FeatureVectorPanel imagePanel;
	
	/** Legend panel */
	private LegendPanel legendPanel;
	
	/** Probability entry boxes for scene presentation trials */
	private ProbabilityBoxContainer boxes;
	
	/** Break screen panel */
	private BreakPanel breakPanel;
	
	private ArrayList<String> boxNames = new ArrayList<String>();
	
	private final GridBagConstraints rightPanelConstraints;

	public ConditionPanel_Phase05(Window parentWindow) {
		this(parentWindow, false, WidgetConstants.BANNER_ORIENTATION);
	}
	
	public ConditionPanel_Phase05(Window parentWindow, boolean showInstructionBanner,
			BannerOrientation bannerOrientation) {
		
		super(showInstructionBanner, bannerOrientation);
		this.parentWindow = parentWindow;
		breakPanel = new BreakPanel();

		contentPanel = new JPanelConditionComponent("content");
		contentPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0; 
		gbc.gridy = 0;
		gbc.weightx = 0;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		
		//Left panel contains the layers and legend
		leftPanel = new JPanel(new GridBagLayout());
		leftPanel.setBackground(Color.WHITE);
		leftPanel.setBorder(WidgetConstants.DEFAULT_BORDER);
		leftPanel.setPreferredSize(new Dimension(140, 500));
		leftPanel.setMinimumSize(leftPanel.getPreferredSize());		
		gbc.weightx = 0;
		contentPanel.add(leftPanel, gbc);
		
		//Image panel contains the scene
		final JPanel imagePanelContainer  = new JPanel(new GridBagLayout());
		imagePanelContainer.setBackground(Color.WHITE);
		imagePanelContainer.setPreferredSize(new Dimension(580, 520));
		imagePanelContainer.setMinimumSize(imagePanelContainer.getPreferredSize());	
		imagePanelContainer.setBorder(WidgetConstants.DEFAULT_BORDER);
		imagePanel = new FeatureVectorPanel();
		imagePanel.setPreferredSize(new Dimension(510, 510));
		imagePanel.setMinimumSize(imagePanel.getPreferredSize());
		imagePanelContainer.add(imagePanel);
		gbc.weightx = 1;
		gbc.gridx++;
		contentPanel.add(imagePanelContainer, gbc);
		
		//Add resize listener to keep image panel square
		imagePanelContainer.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent arg0) {
				int width = imagePanelContainer.getWidth();
				int height = imagePanelContainer.getHeight();
				if(width > height) {
					width = height;
				}
				imagePanel.setPreferredSize(new Dimension(width-10, width-10));
				imagePanelContainer.revalidate();
			}
		});
		
		//Right panel contains the questions
		rightPanel = new JPanel(new GridBagLayout());
		rightPanel.setBorder(WidgetConstants.DEFAULT_BORDER);
		rightPanelConstraints = new GridBagConstraints();
		rightPanelConstraints.weighty = 1;
		rightPanelConstraints.anchor = GridBagConstraints.NORTH;
		rightPanelConstraints.insets = WidgetConstants.INSETS_DEFAULT;
		gbc.weightx = 0;
		gbc.gridx++;
		contentPanel.add(rightPanel, gbc);
		
		contentPanel.setPreferredSize(computeMaxSize());
		
		setConditionComponents(Collections.singleton((IConditionComponent) contentPanel));
		if(instructionBanner != null) {
			instructionBanner.setPreferredSize(new Dimension(contentPanel.getPreferredSize().width, 
					getFontMetrics(instructionBanner.getFont()).getHeight() * 3 + 6));
		}
		
		showBlankPage();
	}

	/** Compute the maximum preferred size of the panel */
	private Dimension computeMaxSize() {
		ArrayList<Integer> temp = new ArrayList<Integer>();
		temp.addAll(Arrays.asList(0, 0, 0, 0));
		boxNames.clear();
		for(int i=0; i<4; i++) {
			boxNames.add("Title");
		}
		TrialState state = new TrialState(0, 0, TrialType.ScenePresentation);
		state.setNumBoxes(4);
		state.setBoxOrientation(BoxOrientation.HORIZONTAL_LINE);		
		showBoxes(state, temp, null);
		int maxWidth = contentPanel.getPreferredSize().width;
		int rightPanelWidth = rightPanel.getPreferredSize().width;
		//state.boxOrientation = BoxOrientation.GRID;
		showBoxes(state, temp, null);
		Dimension maxSize = new Dimension(maxWidth, contentPanel.getPreferredSize().height);
		rightPanel.setPreferredSize(new Dimension(rightPanelWidth, 
				maxSize.height));
		return maxSize;
	}
	
	/** 
	 * Show the break screen 
	 */
	public void showBreakScreen(boolean showCountdown) {
		setInstructionBannerText("Please take a short break.  You may click next to continue when the break is over.");
		breakPanel.setCountdownVisible(showCountdown);
		setConditionComponent(breakPanel);
	}	
	
	/** 
	 * Update the break screen
	 */
	public void setBreakTimeRemaining(long remainingTime_ms) {
		breakPanel.setRemainingTime(remainingTime_ms);
	}	
	public void setBreakOver(boolean breakOver) {
		breakPanel.setClickNext(breakOver);
	}
	public void setBreakText(String text) {
		breakPanel.setInstructionText(text);
	}
	
	/** 
	 * Show a training trial 
	 */
	public void showTrainingTrial(IcarusTrainingTrial trainingTrial, 
			IcarusExam_Phase05 exam, final int trialNum) {
		if(trainingTrial.getTrainingTrialType() == TrainingTrialType.Annotation) {
			showAnnotationTrainingTrial((AnnotationTrainingTrial)trainingTrial, exam, trialNum);
		}
		else if(trainingTrial.getTrainingTrialType() == TrainingTrialType.AnnotationGrid) {
			showAnnotationGridTrainingTrial((AnnotationGridTrainingTrial)trainingTrial, exam, trialNum);
		}
		else if(trainingTrial.getTrainingTrialType() == TrainingTrialType.Rule) {
			showRuleTrainingTrial((RuleTrainingTrial)trainingTrial, exam, trialNum);
		}		
		else {
			System.err.println("Unknown training type: " + trainingTrial.getTrainingTrialType());
		}
		
	}
	
	/** 
	 * Show an annotation training trial
	 */
	public void showAnnotationTrainingTrial(final AnnotationTrainingTrial trainingTrial, 
			final IcarusExam_Phase05 exam, final int trialNum) {

		this.exam = exam;		
		showBlankPage();

		// Load the feature vector for the scene presentation trial
		//FIXME: Do this in a background thread
		final FeatureVector world = loadFeatureVector(trainingTrial.getFeatureVectorUrl(), 
				trainingTrial.getObjectPaletteUrl(), exam.getOriginalPath());

		if(world != null) {	
			//Create sector annotations
			if(trainingTrial.getAnnotations() != null && !trainingTrial.getAnnotations().isEmpty()) {
				HashMap<Integer, StringBuilder> annotations = new HashMap<Integer, StringBuilder>();
				for(Annotation annotation : trainingTrial.getAnnotations()) {
					StringBuilder sb = annotations.get(annotation.getSectorId());
					if(sb == null) {
						sb = new StringBuilder();
						annotations.put(annotation.getSectorId(), sb);
					}
					else {
						sb.append(", ");
					}
					SceneItem sceneItem = exam.getSceneItem(annotation.getItemId());
					if(sceneItem != null) sb.append(sceneItem.getItemName()); else sb.append("N/A");					
				}
				if(world.getSectorLayer() != null && world.getSectorLayer().getNumSectors() > 0) {
					for(Sector sector : world.getSectorLayer()) {
						sector.setName("");
						sector.setFaded(true);
					}
					for(Map.Entry<Integer, StringBuilder> annotation: annotations.entrySet()) {
						Sector sector = world.getSectorLayer().getSector(annotation.getKey());
						if(sector != null) {
							sector.setName(annotation.getValue().toString());
							sector.setFaded(false);
						}
					}
				}
			}
			
			imagePanel.setWorld(world);

			//Hide probability entry area
			if(rightPanel.isVisible()) {
				rightPanel.setVisible(false);
			}

			//Set instruction banner text
			setInstructionBannerText("Training " + Integer.toString(trialNum) + 
			". Study the annotations in the scene and click Next to continue when you are ready.");

			//Update layers (only layers specified in training trial are shown)
			ArrayList<LayerData> layers = null;
			if(trainingTrial.getBaseLayers() != null && !trainingTrial.getBaseLayers().isEmpty()) {
				layers = new ArrayList<LayerData>(trainingTrial.getBaseLayers().size());
				for(Integer layer : trainingTrial.getBaseLayers()) {
					layers.add(new LayerData(layer));
				}				
			}
			updateAvailableLayers(layers);

			setConditionComponent(contentPanel);
		}
	}
	
	/** 
	 * Show an annotation grid training trial
	 */
	public void showAnnotationGridTrainingTrial(final AnnotationGridTrainingTrial trainingTrial, 
			final IcarusExam_Phase05 exam, final int trialNum) {

		this.exam = exam;		
		showBlankPage();

		AnnotationGridPanel gridPanel = new AnnotationGridPanel(trainingTrial, exam, true, 4, 4);

		//Hide probability entry area
		if(rightPanel.isVisible()) {
			rightPanel.setVisible(false);
		}

		//Set instruction banner text
		setInstructionBannerText("Training " + Integer.toString(trialNum) + 
		". Study the examples of facility types and click Next to continue when you are ready.");

		//Update layers (only layers specified in training trial are shown)
		ArrayList<LayerData> layers = null;
		if(trainingTrial.getBaseLayers() != null && !trainingTrial.getBaseLayers().isEmpty()) {
			layers = new ArrayList<LayerData>(trainingTrial.getBaseLayers().size());
			for(Integer layer : trainingTrial.getBaseLayers()) {
				layers.add(new LayerData(layer));
			}				
		}
		updateAvailableLayers(layers);

		//setConditionComponent(contentPanel);
		setConditionComponent(gridPanel);
	}
	
	/** 
	 * Show a rules training trial
	 */
	public void showRuleTrainingTrial(final RuleTrainingTrial trainingTrial,
			final IcarusExam_Phase05 exam, final int trialNum) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				//Set instruction banner text
				setInstructionBannerText("Training " + Integer.toString(trialNum) + 
						". Study the rules below and click Next to continue when you are ready.");
				
				instructionPanel.setInstructionText(ExperimentPanel_Phase05.createRulesPage(null, trainingTrial.getRules()));
				
				showInstructionsPage();
			}
		});
	}
	
	/**
	 * Show a test trial 	 * 
	 */
	public void showTestTrial(IcarusTestTrial testTrial, IcarusExam_Phase05 exam, TrialState initialState) {
		if(testTrial.getTestTrialType() == TestTrialType.Assessment) {
			showAssessmentTrial((AssessmentTrial)testTrial, exam, initialState);
		}
		else if(testTrial.getTestTrialType() == TestTrialType.ScenePresentation) {
			showScenePresentationTrial((ScenePresentationTrial)testTrial, exam, initialState);
		}
		else {
			System.err.println("Unknown trial type: " + testTrial.getTestTrialType());
		}
	}
	
	
	/**
	 * Show an assessment question trial 
	 */
	public void showAssessmentTrial(AssessmentTrial assessmentTrial, IcarusExam_Phase05 exam, TrialState state) {
		//TODO: Finish this by adding scene item images to the panel
		assessment = assessmentTrial;
		updateInstructionsText(state);		
		
		int numBoxes = 1;
		boxNames.clear();
		if(assessmentTrial.getSceneItemsToProbe() != null &&
				!assessmentTrial.getSceneItemsToProbe().isEmpty()) {
			numBoxes = assessmentTrial.getSceneItemsToProbe().size();			
			for (Integer i : assessmentTrial.getSceneItemsToProbe()) {
				SceneItem item = exam.getSceneItem(i);
				if(item != null) {
					boxNames.add(item.getItemName());
				}
				else {
					boxNames.add("N/A");
				}
			}
		}
		
		//Get images for evidence items
		ArrayList<ImageIcon> images = new ArrayList<ImageIcon>();
		if(assessment.getEvidence() != null && !assessment.getEvidence().isEmpty()) {
			for(EvidenceElement evidence : assessment.getEvidence()) {
				ParsedPaletteObject object = null;
				if(evidence.getItemId() != null && assessment.getObjectPaletteUrl() != null) {					
					try {
						//System.out.println("fetching object: " + evidence.getItemId());
						object = FeatureVectorManager.getInstance().getObject(
								assessment.getObjectPaletteUrl(),
								exam.getOriginalPath(),
								evidence.getItemId(), 1);
					} catch (Exception e) {
						ErrorDlg.showErrorDialog(parentWindow, e, true);
					}
				}
				ImageIcon image = ImageManager.getEvidenceImage(evidence.getItemType(), 
						evidence.getItemId(), object);
				//System.out.println("fetched image: " + image.getIconWidth() + ", " + image.getIconHeight());
				images.add(image);
			}
		}		
		
		//Create an assessment question panel
		AssessmentQuestionPanel questionPanel = new AssessmentQuestionPanel("assessmentComponent",
				assessmentTrial.getQuestionText(), 
				images,
				state.getBoxOrientation(),
				boxNames, numBoxes,
				exam.getNormalizationMode() == NormalizationMode.NormalizeDuringInstaneous,
				assessmentTrial.isProbabilitiesNormalized());		
		boxes = questionPanel.getProbabilityBoxes();
		if(!state.getRawData().isEmpty()) {
			//Show initial probabilities
			boxes.setCurrentSettings(state.getRawData());
			boxes.setPreviousSettings(state.getRawData());
		}
		
		setConditionComponent(questionPanel);
	}
	
	/**
	 * Show a scene presentation trial
	 */
	public void showScenePresentationTrial(final ScenePresentationTrial scene, 
			final IcarusExam_Phase05 exam, final TrialState initialState) {		
		this.exam = exam;
		
		showBlankPage();
		
		// Load the feature vector for the scene presentation trial
		//FIXME: Do this in a background thread
		final FeatureVector world = loadFeatureVector(scene.getFeatureVectorUrl(), scene.getObjectPaletteUrl(), exam.getOriginalPath());		

		if(world != null) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					identify = null;
					locate = null;

					imagePanel.setWorld(world);

					Question question = scene.getQuestion();		

					switch (question.getQuestionType()) {
					case IdentifyItem:

						identify = (IdentifyItemQuestion) question;
						createIdentifyItemQuestion(identify, initialState.getTrialNumber(), 
								initialState.getTrialPhase(), exam);		
						break;
					case LocateItem:

						locate = (LocateItemQuestion) question;				
						createLocateItemQuestion(locate, initialState.getTrialNumber(), 
								initialState.getTrialPhase(), exam);
						break;
					default:
						System.err.println("Unknown Question Type");
					break;
					}

					showBoxes(initialState, null, exam.getNormalizationMode());	
					setConditionComponent(contentPanel);
				}
			});
		}
	}
	
	private FeatureVector loadFeatureVector(String featureVectorUrl, String objectPaletteUrl, URL baseUrl) {
		try {
			return FeatureVectorManager.getInstance().getFeatureVector(
					featureVectorUrl, objectPaletteUrl, baseUrl);
		} catch(Exception ex) {
			ErrorDlg.showErrorDialog(parentWindow, ex, true);
		}
		return null;
	}
	
	/**
	 * Creates standard selection boxes. Optionally displays the previous
	 * state to the user
	 */
	private JComponent createStandardBoxes(TrialState state, List<Integer> prev, NormalizationMode normalizationMode) {
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();		
		c.gridwidth = GridBagConstraints.REMAINDER;
		
		if (prev == null) {
			panel.add(new JLabel("your judgement"), c);
		} else {
			panel.add(new JLabel("your updated judgement"), c);
		}		
		
		boxes = new ProbabilityBoxContainer(
				state.getBoxOrientation(),				
				ProbabilityEntryConstants.BOX_SIZE,
				state.getNumBoxes(),
				EditControlType.Spinner, true, true, true, boxNames);		
		boxes.setAutoNormalize(normalizationMode == NormalizationMode.NormalizeDuringInstaneous);		
		if(state.getTrialType() == TrialType.ConfirmNormalized) {
			//Show normalized probabilities
			boxes.setCurrentSettings(state.getNormalizedData());
			boxes.setPreviousSettings(state.getNormalizedData());
			boxes.showConfirmedProbabilities();
		}
		else if(!state.getRawData().isEmpty()) {
			//Show raw probabilities
			boxes.setCurrentSettings(state.getRawData());
			boxes.setPreviousSettings(state.getRawData());
		}
	
		panel.add(boxes, c);
		
		if (prev != null) {
			
			c.insets.top = 20;			
			panel.add(new JLabel("your previous judgement"), c);
			c.insets.top = 0;
			
			ProbabilityBoxContainer prevBoxes = new ProbabilityBoxContainer(
					state.getBoxOrientation(),
					ProbabilityEntryConstants.BOX_SIZE,
					state.getNumBoxes(),
					EditControlType.Spinner, true, false, false, boxNames);
			prevBoxes.showConfirmedProbabilities();
			prevBoxes.setCurrentSettings(prev);
			prevBoxes.setPreviousSettings(prev);			
			
			panel.add(prevBoxes, c);
		}
		
		return panel;
	}
	
	/**
	 * Creates the layer selection dialog to the user and displays
	 * the current settings boxes 
	 */
	private JComponent createLayerSelect(final LayerSelectTrialState state,	NormalizationMode normalizationMode) {
		
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridwidth = GridBagConstraints.REMAINDER;
		
		panel.add(new JLabel("your judgement"), c);
		
		boxes = new ProbabilityBoxContainer(
				state.getBoxOrientation(),
				ProbabilityEntryConstants.BOX_SIZE,
				state.getNumBoxes(),
				EditControlType.Spinner, true, false, false, boxNames);
		boxes.setAutoNormalize(normalizationMode == NormalizationMode.NormalizeDuringInstaneous);
		if(state.getTrialType() == TrialType.ConfirmNormalized) {
			//Show normalized probabilities
			boxes.setCurrentSettings(state.getNormalizedData());
			boxes.setPreviousSettings(state.getNormalizedData());
			boxes.showConfirmedProbabilities();
		}
		else if(!state.getRawData().isEmpty()) {
			//Show raw probabilities
			boxes.setCurrentSettings(state.getRawData());
			boxes.setPreviousSettings(state.getRawData());
		}
		
		c.insets.bottom = 64;
		panel.add(boxes, c);
		c.insets.bottom = 0;
		
		LayerData selected = state.getSelectedLayer();
		
		ButtonGroup radioGroup = new ButtonGroup();
		
		c.anchor = GridBagConstraints.LINE_START;
		//System.out.println("Showing user choice layers for trial:");
		for (final LayerData layer : state.getLayerChoices()) {
			
			//Don't add layer selection option if the layer has already been selected
			if(!state.isLayerSelected(layer.getLayerID())) {
				Layer sceneLayer  = imagePanel.getLayer(layer.getLayerID());
				String layerName = "Unknown Layer";
				if(sceneLayer != null) {
					layerName = sceneLayer.getLayerType().toString();
				}
				else {
					///Attempt to use a standard layer name
					try {
						layerName = LayerType.values()[layer.getLayerID()-1].toString();
					} catch(Exception ex) {}
				}				
				//String layerName = sceneLayer.getLayerType().toString();
				JRadioButton button = new JRadioButton(layerName);

				if (selected != null && layer.getLayerID().equals(selected.getLayerID())) {
					button.setSelected(true);
				}

				//if(sceneLayer != null) {
				button.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						state.setSelectedLayer(layer);
						layer.setSelected(true);
					}
				});
				//}

				radioGroup.add(button);
				panel.add(button, c);
				//}
				//else {
				//System.err.println("Warning, layer not present: " + layer.getLayerID() 
				//		+ ", Presentation type: " + layer.getPresentationType());
				//}
			}
			/*else {
				System.out.println("option for layer " + layer.getLayerID() + " not added because it is already shown");
			}*/
		}
		return panel;
	}
	
	private void createIdentifyItemQuestion(IdentifyItemQuestion question, int trialNum,
			int trialPhase, IcarusExam_Phase05 exam) {
		
		//Fade sectors that are not being probed
		Collection<Sector> sectors = imagePanel.getSectors();
		if(sectors != null) {
			for(Sector sector : sectors) {
				sector.setFaded(true);
			}
			Sector probedSector = imagePanel.getSector(question.getSectorToProbe());
			if(probedSector != null) {
				probedSector.setFaded(false);
			}
		}
		
		setInstructionBannerText(createIdentifyItemInstructions(question, trialNum, trialPhase, exam));

		boxNames.clear();
		
		for (Integer i : question.getSceneItemsToProbe()) {
			SceneItem item = exam.getSceneItem(i);
			if(item != null) {
				boxNames.add(item.getItemName());
			}
			else {
				boxNames.add("N/A");
			}
		}
	}
	
	private String createIdentifyItemInstructions(IdentifyItemQuestion question, int trialNum,
			int trialPhase, IcarusExam_Phase05 exam) {
		
		//StringBuilder sb = new StringBuilder("Trial " + Integer.toString(trialNum) + " (Identify Item). ");
		StringBuilder sb = new StringBuilder();
		if(trialPhase == 0) {
			sb.append("Trial " + Integer.toString(trialNum) + " (Identify Item). ");
			sb.append("Enter the probability that <b>Sector ");
		}
		else {
			sb.append("In light of all layers that have been presented, enter the probability that <b>Sector ");
		}
		sb.append(question.getSectorToProbe() + "</b> contains the ");
		
		int index = 0;
		for (Integer sceneItemID : question.getSceneItemsToProbe()) {
			if(index == question.getSceneItemsToProbe().size() - 1) {
				sb.append("or ");
			}
			sb.append(exam.getFacilities().get(sceneItemID-1).getItemName());
			if(index < question.getSceneItemsToProbe().size() - 1) {
				sb.append(", ");
			}			
			index++;
		}
		sb.append(" and click Next.");
		return sb.toString();
	}

	private void createLocateItemQuestion(LocateItemQuestion question, int trialNum,
			int trialPhase, IcarusExam_Phase05 exam) {
		
		setInstructionBannerText(createLocateItemInstructions(question, trialNum, trialPhase, exam));
		
		boxNames.clear();
		for (Integer i : question.getSectorsToProbe()) {
			boxNames.add("Sector " + i.toString());
		}
	}
	
	private String createLocateItemInstructions(LocateItemQuestion question, int trialNum,
			int trialPhase, IcarusExam_Phase05 exam) {
		
		//StringBuilder sb = new StringBuilder("Trial " + Integer.toString(trialNum) + " (Locate Item). ");
		StringBuilder sb = new StringBuilder();
		if(trialPhase == 0) {
			sb.append("Trial " + Integer.toString(trialNum) + " (Locate Item). ");
			sb.append("Enter the probability that each sector contains the <b>");
		}
		else {
			sb.append("In light of all layers that have been presented, enter the probability that each sector contains the <b>");;
		}
		sb.append(exam.getFacilities().get(
				question.getSceneItemToProbe()-1).getItemName());
		sb.append("</b> and click Next.");	
		
		return sb.toString();
	}
	
	/**
	 * Shows the layer select panel
	 */
	public void showLayerSelect(LayerSelectTrialState state, NormalizationMode normalizationMode) {
		//boxes.setCurrentSettings(state.getData());
		//System.out.println("Select a layer from " + state.getSelectedLayers());
		updateContent(state, createLayerSelect(state, normalizationMode));
	}
	
	/**
	 * Shows a new set probability selection boxes 
	 */
	public void showBoxes(TrialState state, List<Integer> prev, NormalizationMode normalizationMode) {
		
		if(state.getTrialType() == TrialType.Assessment) {
			//Show raw probabilities for an assessment trial
			updateInstructionsText(state);
			if(!state.getRawData().isEmpty()) {
				//Show raw probabilities
				boxes.showEditableProbabilities();
				boxes.setCurrentSettings(state.getRawData());
				boxes.setPreviousSettings(state.getRawData());
			}
			contentPanel.repaint();
		}
		else if(state.getTrialType() == TrialType.ConfirmNormalized) {
			//Show normalized probabilities for an assessment or scene presentation trial
			updateInstructionsText(state);	
			boxes.setCurrentSettings(state.getNormalizedData());
			boxes.setPreviousSettings(state.getNormalizedData());
			boxes.showConfirmedProbabilities();
			contentPanel.repaint();
		}
		else {
			//Show probabilities entry boxes for a scene presentation trial
			if(!rightPanel.isVisible()) {
				rightPanel.setVisible(true);
			}
			updateContent(state, createStandardBoxes(state, prev, normalizationMode));
		}
	}

	private void updateContent(TrialState state, JComponent component) {
		updateInstructionsText(state);		
		updateAvailableLayers(state.getSelectedLayers());
		rightPanel.removeAll();
		rightPanel.add(component, rightPanelConstraints);

		contentPanel.revalidate();
		contentPanel.repaint();
	}
	
	/** 
	 * Updates instruction banner text based on the trial state
	 */
	private void updateInstructionsText(TrialState state) {
		if(state.getTrialType() == TrialType.Assessment) {
			setInstructionBannerText(Integer.toString(state.getTrialNumber()) + " (Assessment): " +
					assessment.getQuestionText());
		}
		else if(state.getTrialType() == TrialType.ConfirmNormalized) {
			setInstructionBannerText("Probabilities sum to 100%. Click Back to adjust them or Next to continue.");
			//setInstructionBannerText("Trial " + Integer.toString(state.trialNumber) + ". " +  
				//	"Probabilities sum to 100%. Click Back to adjust them or Next to continue.");
		}
		else {
			if(state instanceof LayerSelectTrialState) {
				setInstructionBannerText("Select the next layer you would like to see and click Next.");
				//setInstructionBannerText("Trial " + Integer.toString(state.trialNumber) + ". " +  
				//		"Select the next layer you would like to see and click Next.");
			}
			else {
				if(identify != null) {
					setInstructionBannerText(createIdentifyItemInstructions(identify, state.getTrialNumber(), 
							state.getTrialPhase(), exam));
				}
				else if(locate != null) {
					setInstructionBannerText(createLocateItemInstructions(locate, state.getTrialNumber(), 
							state.getTrialPhase(), exam));
				}
			}
		}
	}
	
	/**
	 * Updates the available layers layers given list of availableLayers passed in.
	 * If available layers is null, all layers are enabled.
	 */
	private void updateAvailableLayers(Collection<LayerData> availableLayers) {
		if(imagePanel.getWorld() != null && !imagePanel.getWorld().getLayers().isEmpty()) {
			//Enable any layer that is "always" enabled
			for (Layer layer : imagePanel.getLayers()) {
				layer.setEnabled(layer.isAlwaysEnabled() || availableLayers == null);
			}

			//Now enable layers present in availableLayers
			if(availableLayers != null && !availableLayers.isEmpty()) {
				for (LayerData layerData : availableLayers) {
					Layer featureLayer = imagePanel.getLayer(layerData.getLayerID());
					if(featureLayer != null) {
						featureLayer.setEnabled(true);
					}
				}		
			}
		}

		leftPanel.removeAll();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = 0;
		gbc.weightx = 1;		
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.fill = GridBagConstraints.BOTH;		
		
		JLabel label = new JLabel(" Layers");
		label.setOpaque(true);
		label.setFont(WidgetConstants.FONT_PANEL_TITLE);
		label.setHorizontalAlignment(JLabel.LEFT);
		gbc.weighty = 0;
		leftPanel.add(label, gbc);
		
		gbc.gridy++;
		gbc.weighty = 0;
		leftPanel.add(WidgetConstants.createDefaultSeparator(), gbc);		
		
		JPanel layerPanel = imagePanel.createLayerPanel();
		layerPanel.setBackground(Color.WHITE);
		layerPanel.setBorder(BorderFactory.createEmptyBorder(8, 2, 5, 2));
		gbc.gridy++;
		gbc.weighty = .7;
		leftPanel.add(layerPanel, gbc);
		 
		gbc.gridy++;
		gbc.weighty = 0;
		leftPanel.add(WidgetConstants.createDefaultSeparator(), gbc);		
		
		label = new JLabel(" Legend");
		label.setOpaque(true);
		label.setFont(WidgetConstants.FONT_PANEL_TITLE);
		label.setHorizontalAlignment(JLabel.LEFT);
		gbc.gridy++;
		gbc.weighty = 0;
		leftPanel.add(label, gbc);
		
		gbc.gridy++;
		gbc.weighty = 0;
		leftPanel.add(WidgetConstants.createDefaultSeparator(), gbc);
		
		if(legendPanel == null) {
			legendPanel = LegendPanel.createDefaultLegendPanel();
			legendPanel.setBackground(Color.WHITE);
			legendPanel.setBorder(BorderFactory.createEmptyBorder(8, 2, 5, 2));
		}
		gbc.gridy++;
		gbc.weighty = .3;
		leftPanel.add(legendPanel, gbc);
	}
	
	public ProbabilityBoxContainer getBoxes() {
		return boxes;
	}
}
