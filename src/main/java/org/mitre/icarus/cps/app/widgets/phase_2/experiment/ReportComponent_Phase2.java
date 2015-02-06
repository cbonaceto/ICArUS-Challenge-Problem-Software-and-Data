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

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.mitre.icarus.cps.app.widgets.phase_2.WidgetConstants_Phase2;
import org.mitre.icarus.cps.app.widgets.phase_2.experiment.datum_list.DatumLabel;
import org.mitre.icarus.cps.app.widgets.phase_2.experiment.datum_list.DatumLabelFactory;
import org.mitre.icarus.cps.app.widgets.phase_2.experiment.datum_list.DatumListItem;
import org.mitre.icarus.cps.app.widgets.phase_2.experiment.datum_list.DatumListPanel;
import org.mitre.icarus.cps.app.widgets.phase_2.experiment.datum_list.DatumListItemType;
import org.mitre.icarus.cps.app.widgets.phase_2.experiment.report_panel.ReportComponentContainer.Alignment;
import org.mitre.icarus.cps.experiment_core.gui.IConditionComponent;
import org.mitre.icarus.cps.experiment_core.gui.JPanelConditionComponent;

/**
 *  Report/Action Input widget (Report title, sub-title, "Consider" area with datum buttons, interaction widget (probability panel, other panel, etc), command buttons area for DS integration)
	Report widgets may be in states active, activated, highlighted, activated+highlighted
 * 
 * @author CBONACETO
 *
 */
public class ReportComponent_Phase2 extends JPanelConditionComponent {
	
	private static final long serialVersionUID = 897528591168434911L;
	
	/** The layout */
	protected GridBagLayout gbl;	
	protected GridBagConstraints gbc;
	
	/** Datum list area */
	protected JPanel datumPanel;	
	
	/** The Datum label list panel */
	protected DatumListPanel datumList;

	/** Panel containing the interaction widget */
	protected JPanel contentPanel;
	
	/** The content panel layout */
	protected GridBagLayout contentPanelGbl;

	/** The primary interaction widget */
	protected IConditionComponent interactionComponent;
	
	/** The batch plot control panel (always above the interaction widget and the upper command button panel) */
	protected BatchPlotControlPanel batchPlotControlPanel;
	
	/** Upper command button area (above the interaction widget) */
	protected CommandButtonPanel upperCommandButtonPanel;
	
	/** Lower command button area (below the interaction widget) */
	protected CommandButtonPanel lowerCommandButtonPanel;	
	
	/** Vertical spacing between components */
	int verticalSpacing = 16;
	
	/** The components in the panel */
	protected ArrayList<Component> components;
	
	public ReportComponent_Phase2(String componentId) {		
		super(componentId);
		setLayout(gbl = new GridBagLayout());
		
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets.left = 3;
		gbc.insets.right = 3;
		//gbc.insets.top = verticalSpacing/2; //0
		
		components = new ArrayList<Component>(5);
		for(int i=0; i<4; i++) {
			components.add(null);
		}
		
		//Add the content panel
		contentPanel = new JPanel(contentPanelGbl = new GridBagLayout());
		contentPanel.setVisible(false);
		gbc.insets.top = verticalSpacing * 2;
		gbc.gridy = 3;
		components.set(3, contentPanel);
		add(contentPanel, gbc);
	}
	
	/** Create the datum panel */
	protected void createDatumPanel() {		
		if(datumPanel == null) {
			datumPanel = new JPanel(new GridBagLayout());
			datumPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
			datumPanel.setVisible(false);
			gbc.gridy = 0;
			gbc.insets.top = verticalSpacing;
			components.set(0, datumPanel);
			add(datumPanel, gbc);
			
			datumPanel.setBackground(Color.WHITE);			
			GridBagConstraints datumGbc = new GridBagConstraints();
			datumGbc.gridy = 0;
			datumGbc.weightx = 1;		
			datumGbc.anchor = GridBagConstraints.NORTH;
			datumGbc.fill = GridBagConstraints.HORIZONTAL;			
			datumGbc.insets.right = 3;
						
			datumList = new DatumListPanel();
			datumList.setBackground(Color.WHITE);
			datumGbc.gridy++;			
			datumGbc.weighty = 1;
			datumGbc.insets.top = 4;
			datumGbc.insets.bottom = 4;			
			datumGbc.insets.left = 3;
			datumPanel.add(datumList, datumGbc);			
		}
	}
	
	/** Create the batch plot control panel */
	protected void createBatchPlotControlPanel() {
		if(batchPlotControlPanel == null) {
			batchPlotControlPanel = new BatchPlotControlPanel();
			batchPlotControlPanel.setVisible(false);
			gbc.gridy = 1;
			gbc.insets.top = verticalSpacing;
			gbc.insets.bottom = verticalSpacing/2;
			components.set(1, batchPlotControlPanel);
			add(batchPlotControlPanel, gbc);
		}
	}
	
	/** Create the upper command button panel */
	protected void createUpperCommandButtonPanel() {
		if(upperCommandButtonPanel == null) {
			upperCommandButtonPanel = new CommandButtonPanel();
			gbc.gridy = 2;
			gbc.insets.top = verticalSpacing;
			components.set(2, upperCommandButtonPanel);
			add(upperCommandButtonPanel, gbc);
		}
	}
	
	/** Create the lower command button panel */
	protected void createLowerCommandButtonPanel() {
		if(lowerCommandButtonPanel == null) {
			lowerCommandButtonPanel = new CommandButtonPanel();			
			gbc.gridy = 4;
			gbc.insets.top = verticalSpacing;
			components.set(4, lowerCommandButtonPanel);
			add(lowerCommandButtonPanel, gbc);
		}
	}
	
	public boolean isDatumPanelVisible() {
		return datumPanel != null && datumPanel.isVisible();
	}
	
	public void setDatumPanelVisible(boolean visible) {
		if(visible && datumPanel == null) {
			createDatumPanel();
		}
		if(datumPanel != null && visible != datumPanel.isVisible()) {
			datumPanel.setVisible(visible);
			componentVisibilityChanged();
		}
	}
	
	/**
	 * @param datumItems
	 * @param datumValues
	 */
	public void setDatumPanelItems(List<DatumListItem> datumItems, List<String> datumValues) {
		LinkedList<DatumLabel> datumLabels = new LinkedList<DatumLabel>();
		if(datumItems != null) {
			int i = 0;
			for(DatumListItem datumItem : datumItems) {
				DatumLabel datumLabel = DatumLabelFactory.createDatumLabel(datumItem, false, 
						datumItem.isTitleLabel(), WidgetConstants_Phase2.CONSIDER_LIST_DATUM_VALUE_ALIGNMENT);
				if(datumLabel != null) {
					if(!datumItem.isTitleLabel()) {
						datumLabel.setIndentLevel(11);	
						if(datumValues != null && i < datumValues.size()) {						
							datumLabel.setDatumValue(datumValues.get(i));
						}
					}				
					datumLabels.add(datumLabel);
				}
				i++;
			}
		}
		setDatumPanelLabels(datumLabels);
	}
	
	/**
	 * @param datumLabels
	 */
	public void setDatumPanelLabels(List<DatumLabel> datumLabels) {
		if(datumPanel == null) {
			createDatumPanel();
		}
		datumList.setDatumLabels(datumLabels, true);
		if(datumList.isVisible()) {
			revalidate();
			repaint();
		}
	}
	
	public void clearDataumPanel() {
		if(datumList != null) {
			datumList.clearDataumLabels();
			if(datumPanel.isVisible()) {
				revalidate();
				repaint();
			}
		}
	}
	
	public boolean isBatchPlotControlPanelVisible() {
		return batchPlotControlPanel != null && batchPlotControlPanel.isVisible();
	}
	
	public void setBatchPlotControlPanelVisible(boolean visible) {
		if(visible && batchPlotControlPanel == null) {
			createBatchPlotControlPanel();
		}
		if(batchPlotControlPanel != null && visible != batchPlotControlPanel.isVisible()) {
			batchPlotControlPanel.setVisible(visible);
			componentVisibilityChanged();
		}
	}
	
	/**
	 * @param l
	 */
	public void addBatchPlotCommandButtonListener(ActionListener l) {
		if(batchPlotControlPanel == null) {
			createBatchPlotControlPanel();
		}
		batchPlotControlPanel.addCommandButtonListener(l);
	}
	
	/**
	 * @param l
	 */
	public void removeBatchPlotCommandButtonListener(ActionListener l) {
		if(batchPlotControlPanel != null) {
			batchPlotControlPanel.removeCommandButtonListener(l);
		}
	}
	
	/**
	 * @param batchPlotButtonEnabled
	 * @param message
	 */
	public void configureBatchPlotControlPanel(boolean batchPlotButtonEnabled, String message) {
		if(batchPlotControlPanel == null) {
			createBatchPlotControlPanel();
		}
		batchPlotControlPanel.showCreateBatchPlotButton(batchPlotButtonEnabled, message);
	}
	
	/**
	 * @param showPreviousOutcomeButton
	 * @param previousOutcomeButtonEnabled
	 * @param showNextOutcomeButton
	 * @param nextOutcomeButtonEnabled
	 * @param message
	 */
	public void configureBatchPlotControlPanel(boolean showPreviousOutcomeButton, 
			boolean previousOutcomeButtonEnabled, boolean showNextOutcomeButton, 
			boolean nextOutcomeButtonEnabled, String message) {
		if(batchPlotControlPanel == null) {
			createBatchPlotControlPanel();
		}
		batchPlotControlPanel.showDisplayOutcomeButtons(showPreviousOutcomeButton, 
				previousOutcomeButtonEnabled, showNextOutcomeButton, 
				nextOutcomeButtonEnabled, message);
	}
	
	/**
	 * @param row
	 * @param previousOutcomeButtonEnabled
	 * @param nextOutcomeButtonEnabled
	 * @param message
	 */
	public void configureBatchPlotControlPanel(boolean previousOutcomeButtonEnabled,
			boolean nextOutcomeButtonEnabled, String message) {
		if(batchPlotControlPanel == null) {
			createBatchPlotControlPanel();
		}
		batchPlotControlPanel.setPreviousOutcomeButtonEnabled(previousOutcomeButtonEnabled);
		batchPlotControlPanel.setNextOutcomeButtonEnabled(nextOutcomeButtonEnabled);
		batchPlotControlPanel.setMessage(message);
	}
	
	/**
	 * @param message
	 */
	public void configureBatchPlotControlPanel(String message) {
		if(batchPlotControlPanel == null) {
			createBatchPlotControlPanel();
		}
		batchPlotControlPanel.setMessage(message);
	}
	
	public boolean isUpperCommandButtonPanelVisible() {	
		return upperCommandButtonPanel != null && upperCommandButtonPanel.isVisible();
	}	

	public void setUpperCommandButtonPanelVisible(boolean visible) {
		if(visible && upperCommandButtonPanel == null) {
			createUpperCommandButtonPanel();
		}
		if(upperCommandButtonPanel != null && visible != upperCommandButtonPanel.isVisible()) {
			upperCommandButtonPanel.setVisible(visible);
			componentVisibilityChanged();
		}
	}
	
	public void setUpperCommandButtons(Collection<CommandButton> buttons, int numColumns) {
		if(upperCommandButtonPanel == null) {
			createUpperCommandButtonPanel();
		}
		upperCommandButtonPanel.setButtons(buttons, numColumns);
		if(upperCommandButtonPanel.isVisible()) {
			revalidate();
			repaint();
		}
	}
	
	public void clearUpperCommandButtons() {
		if(upperCommandButtonPanel != null) {
			upperCommandButtonPanel.clearButtons();
			if(upperCommandButtonPanel.isVisible()) {
				revalidate();
				repaint();
			}
		}
	}
	
	public boolean isLowerCommandButtonPanelVisible() {	
		return lowerCommandButtonPanel != null && lowerCommandButtonPanel.isVisible();
	}
	
	public void setLowerCommandButtonPanelVisible(boolean visible) {	
		if(visible && lowerCommandButtonPanel == null) {
			createLowerCommandButtonPanel();
		}
		if(lowerCommandButtonPanel != null && visible != lowerCommandButtonPanel.isVisible()) {
			lowerCommandButtonPanel.setVisible(visible);
			componentVisibilityChanged();
		}
	}
	
	public void setLowerCommandButtons(Collection<CommandButton> buttons, int numColumns) {
		if(lowerCommandButtonPanel == null) {
			createLowerCommandButtonPanel();
		}
		lowerCommandButtonPanel.setButtons(buttons, numColumns);
		if(lowerCommandButtonPanel.isVisible()) {
			revalidate();
			repaint();
		}
	}
	
	/**
	 * 
	 */
	public void clearLowerCommandButtons() {
		if(lowerCommandButtonPanel != null) {
			lowerCommandButtonPanel.clearButtons();
			if(lowerCommandButtonPanel.isVisible()) {
				revalidate();
				repaint();
			}
		}
	}
	
	/**
	 * @return
	 */
	public boolean isContentPanelVisible() {
		return contentPanel.isVisible();
	}
	
	/**
	 * @param visible
	 */
	public void setContentPanelVisible(boolean visible) {
		if(contentPanel.isVisible() != visible) {
			contentPanel.setVisible(visible);
			componentVisibilityChanged();
		}
	}
	
	/**
	 * @param interactionComponent
	 * @param horizontalAlignment
	 */
	public void setInteractionComponent(IConditionComponent interactionComponent, Alignment horizontalAlignment) {		
		this.interactionComponent = interactionComponent;
		contentPanel.removeAll();
		if(interactionComponent != null && interactionComponent.getComponent() != null) {
			contentPanel.add(interactionComponent.getComponent(),
				createInteractionComponentConstraints(horizontalAlignment));
		}
		if(contentPanel.isVisible()) {
			revalidate();
			repaint();
		}
	}
	
	/**
	 * @param horizontalAlignment
	 */
	public void setInteractionComponentHorizontalAlignment(Alignment horizontalAlignment) {
		if(interactionComponent != null && interactionComponent.getComponent() != null) {
			contentPanelGbl.setConstraints(interactionComponent.getComponent(), 
					createInteractionComponentConstraints(horizontalAlignment));
			contentPanel.revalidate();
		}		
	}
	
	protected GridBagConstraints createInteractionComponentConstraints(Alignment horizontalAlignment) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.insets = new Insets(3, 3, 3, 3);
		switch(horizontalAlignment) {
		case Center:
			gbc.anchor = GridBagConstraints.CENTER;
			break;
		case East:
			gbc.anchor = GridBagConstraints.EAST;
			break;
		case West:
			gbc.anchor = GridBagConstraints.WEST;
			break;
		case Stretch:
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.weightx = 1;
			break;
		default: 
			gbc.anchor = GridBagConstraints.CENTER;
		}
		return gbc;
	}
	
	/** Ensures that the last visible component has a weighty of 1 and the others have a weighty of 0. */
	protected void componentVisibilityChanged() {		
		boolean firstVisibleComponentFound = false;
		Component lastVisibleComponent = null;
		for(Component component : components) {
			if(component != null && component.isVisible()) {
				lastVisibleComponent = component;
				GridBagConstraints gbc = gbl.getConstraints(component);
				if(gbc != null) {
					if(component == contentPanel) {
						gbc.insets.top = firstVisibleComponentFound ? verticalSpacing * 2 : verticalSpacing/2;
					} else {
						gbc.insets.top = firstVisibleComponentFound ? verticalSpacing : verticalSpacing/2;
					}
					//gbc.insets.top = i > 0 ? 5 : 0;
					gbc.weighty = 0;
					gbl.setConstraints(component, gbc);
				}
				firstVisibleComponentFound = true;
			}
		}
		if(lastVisibleComponent != null) {
			GridBagConstraints gbc = gbl.getConstraints(lastVisibleComponent);
			if(gbc != null && gbc.weighty != 1) {
				gbc.weighty = 1;
				gbl.setConstraints(lastVisibleComponent, gbc);
			}
		}
		revalidate();
		repaint();
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		ReportComponent_Phase2 comp = new ReportComponent_Phase2("1");
		comp.setDatumPanelVisible(true);
		LinkedList<DatumLabel> labels = new LinkedList<DatumLabel>();
		DatumLabel dl = new DatumLabel(new DatumListItem("Consider:", null, 
				Color.black, false, true), false, true, 
				WidgetConstants_Phase2.CONSIDER_LIST_DATUM_VALUE_ALIGNMENT);
		labels.add(dl);
		dl = new DatumLabel(DatumListItemType.OSINT.getDatumListItem(), false, false,
				WidgetConstants_Phase2.CONSIDER_LIST_DATUM_VALUE_ALIGNMENT);
		dl.setIndentLevel(11);
		dl.setDatumValue("20%");
		dl.setChecked(true);
		//dl.setHighlighted(true);
		labels.add(dl);
		dl = new DatumLabel(DatumListItemType.IMINT.getDatumListItem(), false, false,
				WidgetConstants_Phase2.CONSIDER_LIST_DATUM_VALUE_ALIGNMENT);
		dl.setIndentLevel(11);
		dl.setDatumValue("4");
		labels.add(dl);
		comp.setDatumPanelLabels(labels);
		
		frame.getContentPane().add(comp);
		frame.pack();
		frame.setVisible(true);
	}
}