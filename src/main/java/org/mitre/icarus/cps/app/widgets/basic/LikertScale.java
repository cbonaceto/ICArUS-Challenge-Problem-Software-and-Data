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
package org.mitre.icarus.cps.app.widgets.basic;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.mitre.icarus.cps.app.widgets.renderers.TextRenderer;

/**
 * @author Eric Kappotis
 *
 */
public class LikertScale extends JPanel {
	
	private static final long serialVersionUID = 4525941081239263432L;
	
	protected ArrayList<MultiLineLabel> multiLabels;
	
	protected ArrayList<JRadioButton> options;	
	
	protected String[] labels;
	
	protected ButtonGroup buttonGroup;
	
	public LikertScale(String labels[]) {
		this(labels, true);
	}
	
	protected LikertScale(String labels[], boolean createGUI) {
		if(labels == null || labels.length == 0) {
			throw new IllegalArgumentException("Error creating Likert scale: labels cannot be empty");
		}		
		setLayout(new GridBagLayout());		
		
		buttonGroup = new ButtonGroup();		
		this.multiLabels = new ArrayList<MultiLineLabel>(labels.length);
		this.options = new ArrayList<JRadioButton>(labels.length);
		//this.optionPanels = new ArrayList<JPanel>(labels.length);
		this.labels = labels;
		
		//Create the GUI
		if(createGUI) {
			createGUI();
		}
	}
	
	protected void createGUI() {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.weightx = 1.0;
		
		for(int x = 0; x < labels.length; x++) {
			JRadioButton currRadioButton = new JRadioButton();
			this.options.add(currRadioButton);
			buttonGroup.add(currRadioButton);
			
			JPanel currOptionPanel = new JPanel();
			currOptionPanel.setLayout(new FlowLayout());
			currOptionPanel.add(currRadioButton);			
			//this.optionPanels.add(currOptionPanel);
			
			constraints.gridx = x;
			constraints.gridy = 0;
			constraints.weighty = 0.0;
			constraints.fill = GridBagConstraints.HORIZONTAL;
			this.add(currOptionPanel, constraints);
			
			constraints.gridx = x;
			constraints.gridy = 1;
			constraints.weighty = 1.0;
			constraints.fill = GridBagConstraints.BOTH;
			
			MultiLineLabel currLineLabel = new MultiLineLabel(labels[x], TextRenderer.TextJustification.Center,
					currRadioButton.getFont());

			this.multiLabels.add(currLineLabel);
			this.add(currLineLabel, constraints);			
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean enabled) {
		if(enabled != isEnabled()) {
			super.setEnabled(enabled);
			if(options != null && !options.isEmpty()) {
				for(JRadioButton button : options) {
					button.setEnabled(enabled);
				}
			}
		}
	}

	public boolean isButtonActionListenerPresent(ActionListener l) {
		if(options != null && !options.isEmpty()) {
			ActionListener[] listeners = options.get(0).getActionListeners();
			if(listeners != null) {
				for(ActionListener listener : listeners) {
					if(l == listener) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public void addButtonActionListener(ActionListener l) {
		if(options != null) {
			for(JRadioButton button : options) {
				button.addActionListener(l);
			}
		}
	}
	
	public void removeButtonActionListener(ActionListener l) {
		if(options != null) {
			for(JRadioButton button : options) {
				button.removeActionListener(l);
			}
		}
	}
	
	public void clearSelection() {
		for(JRadioButton currButton : options) {
			currButton.setSelected(false);
		}
		buttonGroup.clearSelection();
		
	}
	
	public int getSelectedIndex() {
		if(buttonGroup.getSelection() != null) {
			int x = 0;
			for(JRadioButton currButton : options) {
				if(currButton.isSelected()) {
					return x;
				}
				x++;
			}
		}
		return -1;
	}
	
	public void setSelectedIndex(int index) {
		clearSelection();
		if(index < options.size()) {
			options.get(index).setSelected(true);
		}
	}
	
	public String getSelectedLabel() {
		if(getSelectedIndex() != -1) {
			return labels[getSelectedIndex()];
		}
		return null;
	}

	@Override
	public void setFont(Font font) {
		super.setFont(font);
		if(options != null && !options.isEmpty()) {
			for(int i=0; i<options.size(); i++) {
				options.get(i).setFont(font);
				multiLabels.get(i).setFont(font);
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		String labels[] = {"Some Label", "", "Some other label"};
		
		LikertScale ls = new LikertScale(labels);
		frame.getContentPane().add(ls);
		
		frame.pack();
		frame.setVisible(true);
	}
}