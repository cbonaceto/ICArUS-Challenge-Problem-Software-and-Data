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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Collection;

import javax.swing.JPanel;

/**
 * @author CBONACETO
 *
 */
public class CommandButtonPanel extends JPanel {
	private static final long serialVersionUID = -3865134397769338718L;
	
	/** The buttons */
	protected Collection<CommandButton> buttons;
	
	/** Horizontal spacing between buttons */
	protected int horizontalSpacing = 5;
	
	/** Vertical spacing between buttons */
	protected int verticalSpacing = 16;
	
	public CommandButtonPanel() {		
		super(new GridBagLayout());		
	}
	
	/**
	 * @param buttons
	 * @param numColumns
	 */
	public void setButtons(Collection<CommandButton> buttons, int numColumns) {
		this.buttons = buttons;
		removeAll();
		if(buttons != null && !buttons.isEmpty()) {
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx = 0;
			gbc.gridy = 0;
			int column = 0;
			for(CommandButton button : buttons) {
				gbc.insets.top = gbc.gridy > 0 ? verticalSpacing : 0;
				gbc.insets.left = column > 0 ? horizontalSpacing : 0;
				gbc.weighty = gbc.gridy == buttons.size() - 1 ? 1 : 0;
				add(button, gbc);
				column++;
				if(column == numColumns) {
					gbc.gridx = 0;
					gbc.gridy++;
					column = 0;
				} else {
					gbc.gridx++;
				}				
			}
		}
		revalidate();
		repaint();
	}
	
	public void clearButtons() {
		buttons = null;
		removeAll();
		revalidate();
		repaint();
	}
}
