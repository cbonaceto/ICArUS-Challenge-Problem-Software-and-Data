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
package org.mitre.icarus.cps.app.widgets.phase_2.experiment;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;

import org.mitre.icarus.cps.app.widgets.WidgetConstants;
import org.mitre.icarus.cps.app.widgets.basic.BasicConditionPanel;
import org.mitre.icarus.cps.app.widgets.basic.BreakPanel;
import org.mitre.icarus.cps.app.widgets.map.phase_2.MapConstants_Phase2;
import org.mitre.icarus.cps.app.widgets.map.phase_2.MapPanelContainer;
import org.mitre.icarus.cps.app.widgets.phase_2.WidgetConstants_Phase2;
import org.mitre.icarus.cps.app.widgets.phase_2.experiment.AssessmentReportPanelManager.CommandButtonOrientation;
import org.mitre.icarus.cps.app.widgets.phase_2.experiment.CommandButton.CommandButtonType;
import org.mitre.icarus.cps.app.widgets.phase_2.experiment.datum_list.DatumListItem;
import org.mitre.icarus.cps.app.widgets.phase_2.experiment.datum_list.DatumListItemType;
import org.mitre.icarus.cps.app.widgets.phase_2.experiment.datum_list.MultiLocationDatumPanel;
import org.mitre.icarus.cps.app.widgets.phase_2.experiment.report_panel.AssessmentReportPanel;
import org.mitre.icarus.cps.app.widgets.phase_2.experiment.report_panel.ReportComponentContainer.Alignment;
import org.mitre.icarus.cps.app.widgets.probability_entry.ProbabilityContainerLayout;
import org.mitre.icarus.cps.app.widgets.probability_entry.ProbabilityContainerLayout.ProbabilityContainerLayoutType;
import org.mitre.icarus.cps.app.window.IcarusLookAndFeel;
import org.mitre.icarus.cps.exam.phase_2.testing.DatumType;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.NormalizationConstraint;
import org.mitre.icarus.cps.experiment_core.gui.IConditionComponent;
import org.mitre.icarus.cps.experiment_core.gui.JPanelConditionComponent;

/**
 * Condition panel for Phase 2 Challenge Problem.
 * 
 * @author CBONACETO
 *
 */
//TODO: Add method to resize map panel if dimensions exceed screen size
public class ConditionPanel_Phase2 extends BasicConditionPanel {	
	private static final long serialVersionUID = -5493812669309206947L;
	
	/** The left content panel (contains location datum panels or other content) */
	protected JPanel leftPanel;
	
	/** Location datum panel */
	protected MultiLocationDatumPanel locationDatumPanel;
	protected JPanel locationDatumPanelContainer;
	
	/** The map panel (contains layers panel, legend panel, and map) */
	protected MapPanelContainer mapPanel;
	
	/** The report panel (area on right, contains reporting widgets) */
	protected AssessmentReportPanel reportPanel;
	
	/** The report panel manager */
	protected AssessmentReportPanelManager reportPanelManager;
	
	/** The score banner panel */
	protected JSeparator scorePanelSeparator;
	protected ScorePanel scorePanel;

	public ConditionPanel_Phase2(Component parent) {
		this(parent, true, WidgetConstants.BANNER_ORIENTATION, 4, true);
	}
	
	public ConditionPanel_Phase2(Component parent, boolean showInstructionBanner, BannerOrientation bannerOrientation, 
			boolean showScorePanel) {		
		super(parent, showInstructionBanner, bannerOrientation);
		createPanel(new MultiLocationDatumPanel(), createMapPanel(parent), 
				new AssessmentReportPanelManager(WidgetConstants_Phase2.NUM_REPORT_PANELS), 
				showScorePanel ? new ScorePanel() : null, showScorePanel, new BreakPanel());
	}	
	
	public ConditionPanel_Phase2(Component parent, boolean showInstructionBanner, BannerOrientation bannerOrientation,
			int instructionBannerTextHeight, boolean showScorePanel) {		
		super(parent, showInstructionBanner, bannerOrientation, instructionBannerTextHeight);
		createPanel(new MultiLocationDatumPanel(), createMapPanel(parent), 
			new AssessmentReportPanelManager(WidgetConstants_Phase2.NUM_REPORT_PANELS), 
			showScorePanel ? new ScorePanel() : null, showScorePanel, new BreakPanel());
	}
	
	protected ConditionPanel_Phase2(Component parent, boolean showInstructionBanner, BannerOrientation bannerOrientation, 
			int instructionBannerTextHeight, MultiLocationDatumPanel locationDatumPanel, MapPanelContainer mapPanel, 
			AssessmentReportPanelManager reportPanelManager, ScorePanel scorePanel, BreakPanel breakPanel) {
		super(parent, showInstructionBanner, bannerOrientation, instructionBannerTextHeight);
		createPanel(new MultiLocationDatumPanel(), mapPanel, reportPanelManager, scorePanel, scorePanel != null, breakPanel);
	}
	
	protected MapPanelContainer createMapPanel(Component parent) {		
		MapPanelContainer mapPanel = new MapPanelContainer(parent, false, false);
		mapPanel.setToolTipsEnabled(false);
		mapPanel.setShowScaleBar(false);
		mapPanel.setPanEnabled(false);
		mapPanel.setZoomEnabled(false);
		mapPanel.setMapPreferredSize(new Dimension(MapConstants_Phase2.PREFERRED_MAP_WIDTH, 
				MapConstants_Phase2.PREFERRED_MAP_HEIGHT), MapConstants_Phase2.MAP_BORDER_NARROW);
		mapPanel.setMinimumSize(mapPanel.getPreferredSize());		
		return mapPanel;
	}
	
	/**
	 * Creates panel with the multi-location datum panel.
	 */
	protected JPanel createLocationDatumPanelContainer(MultiLocationDatumPanel locationDatumPanel) {
		JPanel locationDatumPanelContainer = new JPanel(new GridBagLayout());
		locationDatumPanelContainer.setBackground(Color.WHITE);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = 0;
		gbc.weightx = 1;		
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		JLabel label = new JLabel(" Locations");
		label.setOpaque(true);
		label.setFont(WidgetConstants.FONT_PANEL_TITLE);
		label.setHorizontalAlignment(JLabel.LEFT);
		label.setPreferredSize(new Dimension(label.getPreferredSize().width, 
				label.getPreferredSize().height + 3));
		gbc.weighty = 0;
		locationDatumPanelContainer.add(label, gbc);		

		gbc.gridy++;
		gbc.weighty = 0;
		locationDatumPanelContainer.add(WidgetConstants.createDefaultSeparator(), gbc);
		
		if(locationDatumPanel == null) {
			locationDatumPanel = new MultiLocationDatumPanel();
		}
		locationDatumPanel.setBackground(Color.WHITE);
		JScrollPane sp = new JScrollPane(locationDatumPanel);
		sp.setBackground(Color.WHITE);
		sp.setBorder(null);
		gbc.gridy++;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.NORTH;
		locationDatumPanelContainer.add(sp, gbc);
		return locationDatumPanelContainer;
	}
	
	protected void createPanel(MultiLocationDatumPanel locationDatumPanel, MapPanelContainer mapPanel, 
			AssessmentReportPanelManager reportPanelManager, ScorePanel scorePanel, boolean showScorePanel, 
			BreakPanel breakPanel) {
		this.breakPanel = breakPanel;
		contentPanel = new JPanelConditionComponent("content");
		contentPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0; 
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		
		//Add the left content panel
		leftPanel = new JPanel(new BorderLayout());
		gbc.weightx = 0;
		gbc.anchor = GridBagConstraints.WEST;
		contentPanel.add(leftPanel, gbc);
		leftPanel.setVisible(false);
		
		//Add the location datum panel
		this.locationDatumPanel = locationDatumPanel;
		if(locationDatumPanel != null) {
			locationDatumPanelContainer = createLocationDatumPanelContainer(locationDatumPanel);
			locationDatumPanelContainer.setBorder(WidgetConstants.DEFAULT_BORDER);
			gbc.gridx++;
			gbc.weightx = 0;
			gbc.anchor = GridBagConstraints.WEST;
			contentPanel.add(locationDatumPanelContainer, gbc);
			locationDatumPanelContainer.setVisible(false);
		}
		
		//Add the map panel
		this.mapPanel = mapPanel;
		if(mapPanel != null) {			
			gbc.gridx++;
			gbc.weightx = 1;
			gbc.anchor = GridBagConstraints.WEST;
			contentPanel.add(mapPanel, gbc);
		}
		
		//Add the report panel and create a report panel manager
		this.reportPanelManager = reportPanelManager;
		this.reportPanel = null;
		if(reportPanelManager != null) {
			reportPanel = reportPanelManager.getAssessmentReportPanel();			
			reportPanel.setBorder(WidgetConstants.DEFAULT_BORDER);
			gbc.gridx++;
			gbc.weightx = 0;
			gbc.fill = GridBagConstraints.VERTICAL;
			gbc.anchor = GridBagConstraints.EAST;
			contentPanel.add(reportPanel, gbc);
			gbc.fill = GridBagConstraints.BOTH;
		}
		
		//Add the score banner at the bottom
		this.scorePanel = scorePanel;
		if(scorePanel != null && showScorePanel) {
			scorePanelSeparator = WidgetConstants.createDefaultSeparator();
			gbc.gridx = 0;
			gbc.gridy++;
			gbc.gridwidth = contentPanel.getComponentCount();
			gbc.weightx = 1;
			gbc.weighty = 0;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			contentPanel.add(scorePanelSeparator, gbc);			
			gbc.gridy++;
			contentPanel.add(scorePanel, gbc);
		}
		
		contentPanel.setPreferredSize(computeMaxSize());		
		setConditionComponents(Collections.singleton((IConditionComponent) contentPanel));	
		
		showBlankPage();
	}

	public void updatePreferredSize() {
		contentPanel.setPreferredSize(null);
		contentPanel.setPreferredSize(computeMaxSize());
		setConditionComponents(Collections.singleton((IConditionComponent) contentPanel));		
	}

	/** Compute the maximum preferred size of the panel */	
	protected Dimension computeMaxSize() {
		boolean locationDatumPanelVisible = locationDatumPanelContainer.isVisible();
		boolean layerPanelVisible = mapPanel.isLayerPanelVisible();
		boolean legendPanelVisible = mapPanel.isLegendPanelVisible();
		boolean mapPanelVisible = mapPanel.isVisible();		
		boolean reportPanelVisible = reportPanel.isVisible();				
		locationDatumPanelContainer.setVisible(true);
		locationDatumPanel.setLocations(
				Arrays.asList("Blue Location 1", "Blue Location 2"), 
				Arrays.asList("1", "2"), 
				Arrays.asList(DatumListItemType.OSINT.getDatumListItem(), 
						DatumListItemType.IMINT.getDatumListItem(), 
						DatumListItemType.P_PROPENSITY.getDatumListItem(), 
						DatumListItemType.HUMINT.getDatumListItem(),
						DatumListItemType.P_CAPABILITY_PROPENSITY.getDatumListItem(), 
						DatumListItemType.SIGINT.getDatumListItem(), 
						DatumListItemType.P_ACTIVITY.getDatumListItem(),
						DatumListItemType.P_ACTIVITY_CAPABILITY_PROPENSITY.getDatumListItem()));
		locationDatumPanel.setDatumValue("1", 
				DatumType.AttackProbabilityReport_Activity_Capability_Propensity, "100%");
		mapPanel.setLayerPanelVisibile(false);
		mapPanel.setLegendPanelVisible(false);
		mapPanel.setVisible(true);		
		reportPanel.setVisible(true);
		
		//Configure the report panel with a two-location attack report probability panel
		//TODO: Be sure this is the largest (widest) report component		
		reportPanelManager.configureAttackProbabilityReportPanel(
				Arrays.asList("Red Attacks Blue Location 1", "Red Attacks Blue Location 2"), 
				"P(Attack | SIGINT, HUMINT, IMINT, OSINT)", true, true,
				true, "<html>The sum must be &lt= 100% since<br>Red can only attack one location.</html>", 
				NormalizationConstraint.createDefaultNormalizationConstraint());
		List<DatumListItem> datumItems = Arrays.asList(
				DatumListItemType.OSINT.getDatumListItem(), 
				DatumListItemType.IMINT.getDatumListItem(),
				DatumListItemType.HUMINT.getDatumListItem(), 
				DatumListItemType.P_ACTIVITY.getDatumListItem(), 
				DatumListItemType.P_ACTIVITY_CAPABILITY_PROPENSITY.getDatumListItem());
		List<String> datumValues = Arrays.asList("100%", "10", "100%", "100%", "100%");		
		reportPanelManager.setDatumListAtRow(0, datumItems, datumValues);
		reportPanelManager.setCommandButtonAtRow(0, CommandButtonType.ReviewBlueBook, 
						CommandButtonOrientation.AboveInteractionComponent);
		reportPanelManager.setInteractionComponentAtRow(reportPanelManager.getAttackProbabilityReportPanel(), 
				0, "P (Activity, Capability, Propensity) Report", true);
		reportPanelManager.setReportComponentVisible(0, true);
		
		int maxWidth = contentPanel.getPreferredSize().width;
		Dimension maxSize = new Dimension(maxWidth, contentPanel.getPreferredSize().height);
		locationDatumPanelContainer.setMinimumSize(
				new Dimension(locationDatumPanelContainer.getPreferredSize().width, maxSize.height));
		locationDatumPanelContainer.setPreferredSize(locationDatumPanelContainer.getMinimumSize());
		mapPanel.setLayerAndLegendPreferredWidth(locationDatumPanelContainer.getPreferredSize().width);
		reportPanel.setMinimumSize(
				new Dimension(reportPanel.getPreferredSize().width, maxSize.height));
		reportPanel.setPreferredSize(reportPanel.getMinimumSize());
		//System.out.println(reportPanel.getMinimumSize());
		reportPanelManager.removeInteractionComponentAtRow(0);
		locationDatumPanelContainer.setVisible(locationDatumPanelVisible);
		
		locationDatumPanelContainer.setVisible(locationDatumPanelVisible);
		locationDatumPanel.setLocations(null, null, null);
		mapPanel.setLayerPanelVisibile(layerPanelVisible);
		mapPanel.setLegendPanelVisible(legendPanelVisible);
		mapPanel.setVisible(mapPanelVisible);		
		reportPanel.setVisible(reportPanelVisible);
		
		return new Dimension(maxSize.width + 1, maxSize.height + 1);
	}
	
	public void showTaskScreen(boolean showLocationDatumPanel, boolean showLayerPanel, boolean showLegendPanel) {
		configureTaskScreen(showLocationDatumPanel, showLayerPanel, showLegendPanel);
		setConditionComponent(contentPanel);
	}
	
	public void configureTaskScreen(boolean showLocationDatumPanel, boolean showLayerPanel, boolean showLegendPanel) {
		if(locationDatumPanelContainer.isVisible() != showLocationDatumPanel) {
			setLocationDatumPanelVisible(showLocationDatumPanel);
		}
		if(mapPanel.isLayerPanelVisible() != showLayerPanel) {
			mapPanel.setLayerPanelVisibile(showLayerPanel);
		}
		if(mapPanel.isLegendPanelVisible() != showLegendPanel) {		
			mapPanel.setLegendPanelVisible(showLegendPanel);
		}
		contentPanel.revalidate();
	}
	
	public void setLeftPanelVisible(boolean visible) {
		if(leftPanel != null && visible != leftPanel.isVisible()) {
			leftPanel.setVisible(visible);
			contentPanel.revalidate();
		}
	}
	
	public void setLeftPanelContent(JComponent content) {
		if(leftPanel != null) {
			leftPanel.removeAll();
			leftPanel.add(content);
			if(leftPanel.isVisible()) {
				contentPanel.revalidate();
			}
		}
	}
	
	public MultiLocationDatumPanel getLocationDatumPanel() {
		return locationDatumPanel;
	}

	public void setLocationDatumPanelVisible(boolean visible) {
		if(locationDatumPanelContainer != null && visible != locationDatumPanelContainer.isVisible()) {
			locationDatumPanelContainer.setVisible(visible);
			contentPanel.revalidate();
		}
	}
	
	public MapPanelContainer getMapPanel() {
		return mapPanel;
	}	
	
	public void setMapPanelVisible(boolean visible) {
		if(mapPanel != null && visible != mapPanel.isVisible()) {
			mapPanel.setVisible(visible);
			contentPanel.revalidate();
		}
	}

	public AssessmentReportPanel getReportPanel() {
		return reportPanel;
	}
	
	public AssessmentReportPanelManager getReportPanelManager() {
		return reportPanelManager;
	}
	
	public void setReportPanelVisible(boolean visible) {
		if(reportPanel != null && visible != reportPanel.isVisible()) {
			reportPanel.setVisible(visible);
			contentPanel.revalidate();
		}
	}

	public ScorePanel getScorePanel() {
		return scorePanel;
	}
	
	/**
	 * @param redScore
	 * @param blueScore
	 */
	public void setScores(Double redTrialScore, Double blueTrialScore, 
			Double redMissionScore, Double blueMissionScore) {
		scorePanel.setRedTrialScore(redTrialScore);
		scorePanel.setBlueTrialScore(blueTrialScore);
		scorePanel.setRedMissionScore(redMissionScore);
		scorePanel.setBlueMissionScore(blueMissionScore);
	}
	
	public void setScorePanelVisible(boolean visible) {
		if(scorePanel != null && visible != scorePanel.isVisible()) {
			scorePanelSeparator.setVisible(visible);
			scorePanel.setVisible(visible);
			contentPanel.revalidate();
		}
	}
	
	/**
	 * @param score
	 * @return
	 */
	public String formatScore(Double score) {
		return ScorePanel.formatScore(score);
	}

	public static void main(String[] args) {
		IcarusLookAndFeel.initializeICArUSLookAndFeel();
		JFrame frame = new JFrame("Test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		ConditionPanel_Phase2 comp = new ConditionPanel_Phase2(frame);
		comp.getLocationDatumPanel().setLocations(
				Arrays.asList("Blue Location 1", "Blue Location 2"), 
				Arrays.asList("1", "2"), 
				Arrays.asList(DatumListItemType.OSINT.getDatumListItem(), 
						DatumListItemType.IMINT.getDatumListItem(), 
						DatumListItemType.P_PROPENSITY.getDatumListItem(), 
						DatumListItemType.HUMINT.getDatumListItem(),
						DatumListItemType.P_CAPABILITY_PROPENSITY.getDatumListItem(), 
						DatumListItemType.SIGINT.getDatumListItem(), 
						DatumListItemType.P_ACTIVITY.getDatumListItem(),
						DatumListItemType.P_ACTIVITY_CAPABILITY_PROPENSITY.getDatumListItem()));
		//comp.getLocationDatumPanel().setDatumValue("1", DatumType.P_ACTIVITY_CAPABILITY_PROPENSITY, "100%");
		comp.showTaskScreen(true, false, false);
		ProbabilityReportPanel probPanel = new ProbabilityReportPanel("pr_1", 
				new ProbabilityContainerLayout(ProbabilityContainerLayoutType.HORIZONTAL, 1));
		probPanel.configureProbabilityEntryContainer(Collections.singleton("Red Attacks Location"), 
				"P (Capability, Propensity):", true, false,
				true, "Sorry, Error", NormalizationConstraint.createDefaultNormalizationConstraint());
		comp.getReportPanel().addComponentAtTop(probPanel, Alignment.Stretch, 
				"P (Capability, Propensity) Report", true, true, false, null);
		frame.getContentPane().add(comp);
		frame.pack();
		frame.setMinimumSize(frame.getSize());
		System.out.println("Window Size: " + frame.getSize());
		frame.setVisible(true);
	}
}