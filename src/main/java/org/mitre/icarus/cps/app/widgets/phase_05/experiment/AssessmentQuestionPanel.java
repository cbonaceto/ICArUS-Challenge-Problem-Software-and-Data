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
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;


import org.mitre.icarus.cps.app.widgets.WidgetConstants;
import org.mitre.icarus.cps.app.widgets.phase_05.ImageManager;
import org.mitre.icarus.cps.app.widgets.probability_entry.ProbabilityEntryConstants;
import org.mitre.icarus.cps.app.widgets.probability_entry.distinct_bars.ProbabilityBoxContainer;
import org.mitre.icarus.cps.app.widgets.probability_entry.distinct_bars.BoxContainer.BoxOrientation;
import org.mitre.icarus.cps.app.widgets.probability_entry.distinct_bars.SettingsBoxWithDecorators.EditControlType;
import org.mitre.icarus.cps.experiment_core.gui.JPanelConditionComponent;

/**
 * GUI for assessment questions and joint probability questions.
 * 
 * @author CBONACETO
 *
 */
public class AssessmentQuestionPanel extends JPanelConditionComponent  {

	private static final long serialVersionUID = 1L;
	
	/** Probability box container */
	private ProbabilityBoxContainer probabilityBoxes;

	public AssessmentQuestionPanel(String componentId, 
			String questionText,
			List<ImageIcon> evidenceImages,
			BoxOrientation boxOrientation,
			ArrayList<String> boxTitles,
			int numBoxes,
			boolean autoNormalize,
			boolean showSum) {
		
		super(componentId);
		setLayout(new GridBagLayout());
		setBorder(WidgetConstants.DEFAULT_BORDER);
	
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		
		//TODO: Add question text
		
		if(evidenceImages != null && !evidenceImages.isEmpty()) {
			JPanel imagePanel = new JPanel(new GridBagLayout());			
			gbc.gridx = 0;
			for(ImageIcon image : evidenceImages) {
				JLabel imageLabel = new JLabel(image);
				imageLabel.setOpaque(true);
				imageLabel.setBackground(Color.white);
				imageLabel.setBorder(BorderFactory.createLineBorder(Color.black));
				Dimension size = imageLabel.getPreferredSize();
				if(size.width < ImageManager.EvidenceImageSize.width + 2) {
					size.width = ImageManager.EvidenceImageSize.width + 2;
				}
				if(size.height < ImageManager.EvidenceImageSize.height + 2) {
					size.height = ImageManager.EvidenceImageSize.height + 2;
				}				
				imageLabel.setPreferredSize(size);
				imagePanel.add(imageLabel, gbc);
				gbc.insets.left = 5;
				gbc.gridx++;
			}
			add(imagePanel, gbc);
			gbc.insets.top = 15;
			gbc.insets.right = 0;
			gbc.gridy++;			
		}
		
		probabilityBoxes = new ProbabilityBoxContainer(
				boxOrientation,				
				ProbabilityEntryConstants.BOX_SIZE,
				numBoxes,
				EditControlType.Spinner, true, true, showSum, 
				boxTitles);		
		probabilityBoxes.setTopTitleVisible(true);
		probabilityBoxes.setTopTitle("your judgment");
		probabilityBoxes.setAutoNormalize(autoNormalize);
	
		add(probabilityBoxes, gbc);
	}

	public ProbabilityBoxContainer getProbabilityBoxes() {
		return probabilityBoxes;
	}
}
