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
package org.mitre.icarus.cps.app.widgets.ui_study;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.mitre.icarus.cps.app.experiment.ui_study.UIStudyConstants;

/**
 * @author CBONACETO
 *
 */
public class PercentsPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	/** Labels for each percent */
	protected ArrayList<LabelAndTitle> percentLabels;
	
	protected int numColumns;
	
	public PercentsPanel(int numPercents, int numColumns) {
		this(createDefaultTitles(numPercents), numColumns);
	}
	
	public PercentsPanel(List<String> titles, int numColumns) {
		super(new GridBagLayout());
		this.numColumns = numColumns;
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		//Create percent labels
		createPercentLabels(titles);
	}
	
	protected void createPercentLabels(List<String> titles) {
		//Create percent labels
		percentLabels = new ArrayList<LabelAndTitle>(titles.size());		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0; gbc.gridy = 0;
		//gbc.weightx = 1; gbc.weighty = 1;
		//gbc.fill = GridBagConstraints.BOTH;
		int columnCount = 1;
		for(String title : titles) {
			if(columnCount > numColumns) {
				columnCount = 1;
				gbc.gridy++;
				gbc.gridx = 0;
				gbc.insets.top = 10;
			} 
			if(columnCount > 1) {
				gbc.insets.left = 10;
			} else {
				gbc.insets.left = 0;
			}
			
			//Create the current percent label and title and add it to the layout
			LabelAndTitle percentPanel = new LabelAndTitle(title);
			percentLabels.add(percentPanel);
			add(percentPanel, gbc);
			
			gbc.gridx++;
			columnCount++;
		}
	}
	
	@Override
	public void setFont(Font font) {
		super.setFont(font);
		if(percentLabels != null) {
			for(LabelAndTitle percentLabel : percentLabels) {
				percentLabel.titleLabel.setFont(font);
			}
			revalidate();
		}
	}

	public void setNumPercents(int numPercents) {
		if(numPercents != percentLabels.size()) {
			setTitles(createDefaultTitles(numPercents));
		}
	}
	
	protected static List<String> createDefaultTitles(int numPercents) {
		ArrayList<String> titles = new ArrayList<String>(numPercents);
		for(int i=1; i<=numPercents; i++) {
			titles.add(Integer.toString(i));
		}
		return titles;
	}
	
	public void setTitles(List<String> titles) {
		if(titles.size() != percentLabels.size()) {
			removeAll();
			createPercentLabels(titles);
			revalidate();
		} else {
			int i = 0;
			for(String title : titles) {
				percentLabels.get(i).setTitle(title);
				i++;
			}
		}
	}
	
	public void setPercent(Integer percent, int panelIndex) {
		if(panelIndex < 0 || panelIndex >= percentLabels.size()) {
			throw new IllegalArgumentException("Panel index out of bounds");
		}
		percentLabels.get(panelIndex).setPercent(percent);
	}
	
	public void setPercents(List<Integer> percents) {
		int i = 0;
		for(Integer percent : percents) {
			percentLabels.get(i).setPercent(percent);
			i++;
		}
	}
	
	protected static class LabelAndTitle extends JPanel {
		private static final long serialVersionUID = 1L;
		
		protected JLabel percentLabel;
		
		protected JLabel titleLabel;
		
		public LabelAndTitle(String title) {
			this(title, null);
		}
		
		public LabelAndTitle(String title, Integer percent) {
			super(new GridBagLayout());			
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx = 0; gbc.gridy = 0;
			
			percentLabel = new JLabel();
			percentLabel.setHorizontalAlignment(JLabel.CENTER);
			percentLabel.setFont(UIStudyConstants.FONT_PERCENTAGES);
			percentLabel.setOpaque(true);
			percentLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			percentLabel.setBackground(Color.WHITE);
			percentLabel.setText("  100%  ");
			percentLabel.setPreferredSize(percentLabel.getPreferredSize());
			if(percent == null) {
				setPercent(0);
			} else {
				setPercent(percent);
			}
			add(percentLabel, gbc);
			
			titleLabel = new JLabel();
			titleLabel.setHorizontalAlignment(JLabel.CENTER);
			titleLabel.setFont(UIStudyConstants.FONT_PERCENTAGES);
			setTitle(title);
			gbc.gridy = 1;
			gbc.insets.top = 3;			
			add(titleLabel, gbc);
		}
		
		public void setTitle(String title) {
			titleLabel.setText(title);
		}
		
		public void setPercent(Integer percent) {
			percentLabel.setText(percent.toString() + "%");
		}
	}
}