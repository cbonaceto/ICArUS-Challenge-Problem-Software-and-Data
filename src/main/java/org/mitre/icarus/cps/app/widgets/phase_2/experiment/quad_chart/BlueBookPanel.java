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
package org.mitre.icarus.cps.app.widgets.phase_2.experiment.quad_chart;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.mitre.icarus.cps.app.widgets.phase_2.ColorManager_Phase2;
import org.mitre.icarus.cps.exam.phase_2.testing.bluebook.BlueBook;
import org.mitre.icarus.cps.exam.phase_2.testing.bluebook.RedTactic;
import org.mitre.icarus.cps.exam.phase_2.testing.bluebook.RedTacticParameters;
import org.mitre.icarus.cps.experiment_core.gui.IConditionComponent;
import org.mitre.icarus.cps.feature_vector.phase_2.BlueLocation;
import org.mitre.icarus.cps.feature_vector.phase_2.int_datum.ImintDatum;

/**
 * @author CBONACETO
 *
 */
public class BlueBookPanel extends JPanel implements IConditionComponent {
	
	private static final long serialVersionUID = -3163075389759185306L;
	
	/** The Red tactic parameters panels */
	private List<RedTacticParametersPanel> redTacticPanels;
	
	/** The Red tactics */
	private List<RedTactic> redTactics;
	
	/** The component ID */
	protected String componentId;
	
	public BlueBookPanel(Collection<RedTactic> redTactics, boolean attackProbabilitiesUnknown) {
		super(new GridBagLayout());
		setBackground(Color.WHITE);
		redTacticPanels = new ArrayList<RedTacticParametersPanel>();
		this.redTactics = new ArrayList<RedTactic>();
		if(redTactics != null && !redTactics.isEmpty()) {
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.anchor = GridBagConstraints.NORTH;
			Font titleFont = redTactics.size() > 1 ? QuadChartPanelFactory.TITLE_FONT.deriveFont(Font.PLAIN) : 
				QuadChartPanelFactory.TITLE_FONT;
			for(RedTactic redTactic : redTactics) {
				this.redTactics.add(redTactic);
				RedTacticParametersPanel redTacticPanel = 
						QuadChartPanelFactory.createRedTacticParametersPanel(
								createRedTacticsName(redTactic.getName(), redTactics.size()), 
								redTactic.getTacticParameters(), ImintDatum.MIN_U, ImintDatum.MAX_U, 
								true, attackProbabilitiesUnknown, false);
				redTacticPanel.setTitleFont(titleFont);
				redTacticPanel.setBackground(Color.WHITE);
				redTacticPanel.setColumnHeadingsBackground(Color.WHITE);
				redTacticPanel.setRowHeadingsBackground(Color.WHITE);
				redTacticPanels.add(redTacticPanel);
				if(gbc.gridy > 0) {
					gbc.insets.top = 16;
				}
				if(gbc.gridy == redTactics.size() - 1) {
					gbc.weighty = 1;
				}
				add(redTacticPanel, gbc);
				gbc.gridy++;
			}
		}
	}
	
	protected String createRedTacticsName(String tacticName, int numTactics) {
		if(numTactics > 1) {
			StringBuilder sb = new StringBuilder("<html><center><b>");
			sb.append(tacticName);
			sb.append(":</b><br>");
			sb.append("Probability of Red Attack");
			sb.append("</center></html>");
			return sb.toString();
		} else {
			return "Probability of Red Attack";
		}
	}
	
	@Override
	public String getComponentId() {
		return componentId;
	}

	@Override
	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}	
	
	/**
	 * @param currentLocation
	 */
	public void highlightCells(BlueLocation currentLocation) {
		if(!redTacticPanels.isEmpty() && currentLocation != null) {
			Double currentP = currentLocation.getOsint() != null ? currentLocation.getOsint().getRedVulnerability_P() : null;
			Integer currentU = currentLocation.getImint() != null ? currentLocation.getImint().getRedOpportunity_U() : null;
			if(currentP != null && currentU != null) {
				for(int panelIndex = 0; panelIndex < redTacticPanels.size(); panelIndex++) {
					highlightCell(panelIndex, currentP, currentU);
				}
			}
		}
	}	
	
	/**
	 * @param currentLocations
	 */
	public void highlightCells(Collection<BlueLocation> currentLocations) {
		if(!redTacticPanels.isEmpty() && currentLocations != null && !currentLocations.isEmpty()) {
			int panelIndex = 0;
			for(BlueLocation currentLocation : currentLocations) {				
				if(panelIndex < redTacticPanels.size()) {
					Double currentP = currentLocation.getOsint() != null ? currentLocation.getOsint().getRedVulnerability_P() : null;
					Integer currentU = currentLocation.getImint() != null ? currentLocation.getImint().getRedOpportunity_U() : null;
					if(currentP != null && currentU != null) {
						highlightCell(panelIndex, currentP, currentU);
					}
				} else {
					break;
				}
				panelIndex++;
			}
		}
	}	

	/**
	 * @param currentP
	 * @param currentU
	 */
	/**
	 * @param currentP
	 * @param currentU
	 */
	public void highlightCells(Double currentP, Integer currentU) {
		if(!redTacticPanels.isEmpty()) {
			for(int panelIndex = 0; panelIndex < redTacticPanels.size(); panelIndex++) {
				highlightCell(panelIndex, currentP, currentU);
			}
		}		
	}
	
	/**
	 * @param panelIndex
	 * @param currentP
	 * @param currentU
	 */
	public void highlightCell(int panelIndex, Double currentP, Integer currentU) {		
		if(!redTacticPanels.isEmpty() && panelIndex >=0 && panelIndex < redTacticPanels.size() &&
				currentP != null && currentU != null) {
			RedTacticParametersPanel redTacticPanel = redTacticPanels.get(panelIndex);
			RedTactic redTactic = panelIndex < redTactics.size() ? redTactics.get(panelIndex) : null;
			
			//Set the background color for all cells to light blue
			redTacticPanel.setCellBackground(ColorManager_Phase2.getColor(
					ColorManager_Phase2.BLUEBOOK_UNHIGHLIGHTED));

			//Make the background color of the highlighted cells dark blue
			try {
				int cellIndex = 0;
				if(redTactic == null || redTactic.getTacticParameters() == null) {
					cellIndex = RedTacticParameters.getRedTacticQuadrant(currentP, currentU,
							RedTacticParameters.DEFAULT_HIGH_P_THRESHOLD, 
							RedTacticParameters.DEFAULT_LARGE_U_THRESHOLD).ordinal();
				} else {				
					cellIndex = redTactic.getTacticParameters().getRedTacticQuadrant(currentP, currentU).ordinal();
				} 
				redTacticPanel.setCellBackground(cellIndex, ColorManager_Phase2.getColor(
						ColorManager_Phase2.BLUEBOOK));
			} catch(Exception ex) {}
		}
	}
	
	/**
	 * 
	 */
	public void unhighlightAllCells() {
		if(!redTacticPanels.isEmpty()) {
			for(RedTacticParametersPanel redTacticPanel : redTacticPanels) {
				//Set the background color for all cells to the standard Blue
				redTacticPanel.setCellBackground(ColorManager_Phase2.getColor(
						ColorManager_Phase2.BLUEBOOK_UNHIGHLIGHTED));//ColorManager_Phase2.BLUEBOOK));
			}
		}
	}
	
	@Override
	public JComponent getComponent() {
		return this;
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("Test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		BlueBookPanel panel = new BlueBookPanel(BlueBook.createDefaultBlueBook().getMission_2_Tactics(), true);
		panel.highlightCells(.25d, 3);
		frame.getContentPane().add(panel);		
		frame.pack();
		frame.setVisible(true);
	}
}