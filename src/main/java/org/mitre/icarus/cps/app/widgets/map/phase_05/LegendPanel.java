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
package org.mitre.icarus.cps.app.widgets.map.phase_05;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Arrays;
import java.util.Collection;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.mitre.icarus.cps.app.widgets.phase_05.ImageManager;

/**
 * Panel with the scene legend.
 * 
 * @author CBONACETO
 *
 */
public class LegendPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private Collection<LegendItem> legendItems;
	
	protected int indent = 5;
	
	/** Create legend panel with given legend items */	
	public LegendPanel(Collection<LegendItem> legendItems) {
		GridBagLayout gbl = new GridBagLayout();
		setLayout(gbl);
		setBackground(Color.WHITE);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;		
		gbc.anchor = GridBagConstraints.NORTHWEST;
		
		for(LegendItem legendItem : legendItems) {
			if(gbc.gridy > 0) {
				gbc.insets.top = 12;
			}
			createLegendElements(legendItem, 0, gbc);
			gbc.gridy++;
		}
		
		if(getComponents() != null && getComponents().length > 0) {
			Component lastComponent = getComponent(getComponents().length-1);
			GridBagConstraints constraints = gbl.getConstraints(lastComponent);
			constraints.weighty = 1;
			gbl.setConstraints(lastComponent, constraints);
		}
	}
	
	private void createLegendElements(LegendItem legendItem, int indentLevel, GridBagConstraints gbc) {
		JLabel label = new JLabel(legendItem.name);
		if(legendItem.icon != null) {
			label.setIcon(legendItem.icon);
		}
		gbc.insets.left = indentLevel * indent;
		add(label, gbc);
		if(legendItem.getChildren() != null && !legendItem.getChildren().isEmpty()) {
			gbc.insets.top = 3;
			for(LegendItem child : legendItem.getChildren()) {
				gbc.gridy++;
				createLegendElements(child, indentLevel+1, gbc);				
			}
		}
	}
	
	public Collection<LegendItem> getLegendItems() {
		return legendItems;
	}

	/** Create standard legend panel */
	public static LegendPanel createDefaultLegendPanel() {
		LegendItem imintItem = new LegendItem("IMINT Objects", null);
		imintItem.setChildren(Arrays.asList(
				new LegendItem("Building", ImageManager.getImageIcon(ImageManager.BUILDING_ICON)),
				new LegendItem("Rooftop Hardware", ImageManager.getImageIcon(ImageManager.ROOFTOP_HARDWARE_ICON)),
				new LegendItem("Water", ImageManager.getImageIcon(ImageManager.WATER_ICON))));
		
		LegendItem sigintItem = new LegendItem("SIGINT Objects", null);
		sigintItem.setChildren(Arrays.asList(
				new LegendItem("SIGINT Hit", ImageManager.getImageIcon(ImageManager.SIGINT_HIT_ICON))));
		
		LegendItem masintItem = new LegendItem("MASINT Objects", null);
		masintItem.setChildren(Arrays.asList(
				new LegendItem("Green Fungus Hit", ImageManager.getImageIcon(ImageManager.MASINT1_HIT_ICON)),
				new LegendItem("Red Fungus Hit", ImageManager.getImageIcon(ImageManager.MASINT2_HIT_ICON))));
		/*masintItem.setChildren(Arrays.asList(
				new LegendItem("Chemical 1 Hit", ImageManager.getImageIcon(ImageManager.MASINT1_HIT_ICON)),
				new LegendItem("Chemical 2 Hit", ImageManager.getImageIcon(ImageManager.MASINT2_HIT_ICON))));*/
				
		return new LegendPanel(Arrays.asList(imintItem, sigintItem, masintItem));
	}

	public static class LegendItem {
		
		private String name;
		
		private Icon icon;
		
		public LegendItem() {}
		
		public LegendItem(String name, Icon icon) {
			this.name = name;
			this.icon = icon;
		}
		
		private Collection<LegendItem> children;
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Icon getIcon() {
			return icon;
		}
		public void setIcon(Icon icon) {
			this.icon = icon;
		}
		public Collection<LegendItem> getChildren() {
			return children;
		}
		public void setChildren(Collection<LegendItem> children) {
			this.children = children;
		}
	}
}