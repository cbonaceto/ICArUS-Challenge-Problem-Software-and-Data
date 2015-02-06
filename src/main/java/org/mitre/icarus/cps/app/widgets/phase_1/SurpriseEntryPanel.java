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
package org.mitre.icarus.cps.app.widgets.phase_1;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.mitre.icarus.cps.app.widgets.basic.LikertScale;
import org.mitre.icarus.cps.app.widgets.basic.MultiLineLabel;
import org.mitre.icarus.cps.app.widgets.probability_entry.ProbabilityEntryConstants;
import org.mitre.icarus.cps.app.widgets.renderers.TextRenderer;
import org.mitre.icarus.cps.experiment_core.gui.IConditionComponent;

/**
 * Panel for entering surprise values. 
 * 
 * @author CBONACETO
 *
 */
public class SurpriseEntryPanel extends LikertScale implements IConditionComponent {
	
	private static final long serialVersionUID = 1L;
	
	protected String componentId;
	
	/** The minimum surprise setting */
	protected final int minSurprise;
	
	/** The maximum surprise setting */
	protected final int maxSurprise;
	
	/** The surprise increment */
	protected final int surpriseIncrement;
	
	/** The top title label */
	protected JLabel titleLabel;
	
	/** The default labels for each surprise setting **/
	protected static final String minSurpriseLabel = "Not At All Surprised";
	
	protected static final String minPlusOneSurpriseLabel = "";
	
	protected static final String minPlusTwoSurpriseLabel = "";
	
	protected static final String minPlusThreeSurpriseLabel = "";
	
	protected static final String middleSurpriseLabel = "";
	
	protected static final String maxMinusThreeSurpriseLabel = "";
	
	protected static final String maxMinusTwoSurpriseLabel = "";
	
	protected static final String maxMinusOneSurpriseLabel = "";
	
	protected static final String maxSurpriseLabel = "Extremely Surprised";
	
	public SurpriseEntryPanel(int minSurprise, int maxSurprise, int surpriseIncrement) {
		//super(createEmptyLabels(minSurprise, maxSurprise, surpriseIncrement));
		super(createSurpriseLabels(minSurprise, maxSurprise, surpriseIncrement), false);
		this.minSurprise = minSurprise;
		this.maxSurprise = maxSurprise;
		this.surpriseIncrement = surpriseIncrement;
		
		//Create the GUI
		createGUI();
	}	
	
	protected void createGUI() {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		
		JPanel buttonPanel = new JPanel(new GridBagLayout());
		add(buttonPanel, constraints);		
		
		JPanel labelPanel = new JPanel(new GridBagLayout());
		constraints.gridy++;
		add(labelPanel, constraints);
		constraints.fill = GridBagConstraints.NONE;
		
		Font font = new JRadioButton().getFont();
		JLabel sampleLabel = new JLabel("Extremely");
		sampleLabel.setFont(font);
		int labelWidth = sampleLabel.getPreferredSize().width + 3;
		
		for(int x = 0; x < labels.length; x++) {
			JRadioButton currRadioButton = new JRadioButton();
			options.add(currRadioButton);
			buttonGroup.add(currRadioButton);
			
			constraints.gridx = x;
			constraints.gridy = 0;
			constraints.insets.left = 0;
			constraints.insets.right = 0;
			if(x ==0) {
				constraints.insets.left = labelWidth/2;
			}
			else if(x == labels.length - 1) {				
				constraints.insets.right = labelWidth/2;
			}
			buttonPanel.add(currRadioButton, constraints);					
				
			MultiLineLabel currLineLabel = new MultiLineLabel(labels[x], TextRenderer.TextJustification.Center, font);		
			if(labels[x] == null || labels[x].isEmpty()) {
				currLineLabel.setPreferredSize(new Dimension(10, currLineLabel.getPreferredSize().height));
			}
			else {
				currLineLabel.setPreferredSize(new Dimension(labelWidth, currLineLabel.getPreferredSize().height));
			}
			multiLabels.add(currLineLabel);
			constraints.gridx = x;
			constraints.gridy = 0;
			constraints.insets.left = 0;
			constraints.insets.right = 0;
			labelPanel.add(currLineLabel, constraints);			
		}
	}
	
	public boolean isTitleVisible() {
		return titleLabel != null && titleLabel.isVisible();
	}
	
	public void setTitleVisible(boolean visible) {
		if(visible != this.isTitleVisible()) {
			if(visible && titleLabel == null) {
				titleLabel = new JLabel();
				titleLabel.setHorizontalAlignment(JLabel.CENTER);
				titleLabel.setFont(ProbabilityEntryConstants.FONT_TITLE);
				GridBagConstraints gbc = new GridBagConstraints();
				gbc.gridx = 0;	
				gbc.gridy = 0;
				gbc.insets.bottom = 7;				
				gbc.anchor = GridBagConstraints.CENTER;			
				add(titleLabel, gbc);
			}
			titleLabel.setVisible(visible);
			revalidate();
			repaint();
		}
	}
	
	public String getTitleText() {
		if(titleLabel != null) {
			return titleLabel.getText();
		}
		return null;
	}
	
	public void setTitleText(String text) {
		if(titleLabel != null) {
			titleLabel.setText(text);
		}
	}
	
	public void setTitleFont(Font font) {
		if(titleLabel != null) {
			titleLabel.setFont(font);
			if(titleLabel.isVisible()) {
				revalidate();
			}
		}
	}
	
	public int getMinSurprise() {
		return minSurprise;
	}

	public int getMaxSurprise() {
		return maxSurprise;
	}

	public int getSurpriseIncrement() {
		return surpriseIncrement;
	}

	@Override
	public String getComponentId() {
		return componentId;
	}

	@Override
	public void setComponentId(String id) {
		this.componentId = id;
	}

	@Override
	public JComponent getComponent() {
		return this;
	}	
	
	protected static String[] createEmptyLabels(int minSurprise, int maxSurprise, int surpriseIncrement) {
		String[] surpriseLabels = new String[(maxSurprise-minSurprise)/surpriseIncrement + 1];
		for(int i=0; i<surpriseLabels.length; i++) {
			surpriseLabels[i] = "";
		}
		return surpriseLabels;
	}
	
	protected static String[] createSurpriseLabels(int minSurprise, int maxSurprise, int surpriseIncrement) {
		if(minSurprise >= 0 && minSurprise < maxSurprise && surpriseIncrement <= (maxSurprise-minSurprise)) {
			String[] surpriseLabels = new String[(maxSurprise-minSurprise)/surpriseIncrement + 1];
			int neutralSurprise = (maxSurprise-minSurprise)/2;
			int surprise = minSurprise;
			for(int i=0; i<surpriseLabels.length; i++) {
				String label = null;
				if(surprise == minSurprise) {
					label = minSurpriseLabel;
				}
				else if(surprise == maxSurprise) {
					label = maxSurpriseLabel;
				}
				else if(surprise == neutralSurprise) {
					label = middleSurpriseLabel;
				}
				else {
					if(surprise == minSurprise + 1 && surprise < neutralSurprise) {
						label = minPlusOneSurpriseLabel;
					}
					else if(surprise == minSurprise + 2  && surprise < neutralSurprise) {
						label = minPlusTwoSurpriseLabel;
					}
					else if(surprise == minSurprise + 3  && surprise < neutralSurprise) {
						label = minPlusThreeSurpriseLabel;
					}
					else if(surprise == maxSurprise - 1) {
						label = maxMinusOneSurpriseLabel;
					}
					else if(surprise == maxSurprise - 2) {
						label = maxMinusTwoSurpriseLabel;
					}
					else if(surprise == maxSurprise - 3) {
						label = maxMinusThreeSurpriseLabel;
					}
					else {
						label = "";
					}
				}
				surpriseLabels[i] = label;
				surprise += surpriseIncrement;
			}
			return surpriseLabels;
		}
		return new String[] {""};
	}	
}