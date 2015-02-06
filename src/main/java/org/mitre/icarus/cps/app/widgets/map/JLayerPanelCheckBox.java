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
package org.mitre.icarus.cps.app.widgets.map;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author CBONACETO
 *
 */
public class JLayerPanelCheckBox extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	protected JCheckBox checkBox;
	
	protected JLabel icon;	

	public JLayerPanelCheckBox() {
		super(new GridBagLayout());
		this.checkBox = new JCheckBox();
		this.icon = new JLabel();
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(checkBox, gbc);
		gbc.gridx = 1;
		gbc.insets.left = 2;
		add(icon, gbc);
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		checkBox.setEnabled(enabled);
		icon.setEnabled(enabled);
	}

	@Override
	public void setBackground(Color bg) {
		super.setBackground(bg);
		if(checkBox != null) {
			checkBox.setBackground(bg);
		}
		if(icon != null) {
			icon.setBackground(bg);
		}
	}
	
	public String getLayerName() {
		return checkBox.getText();
	}
	
	public void setLayerName(String name) {	
		checkBox.setText(name);
	}

	public Icon getLayerIcon() {
		return icon.getIcon();
	}
	
	public void setLayerIcon(Icon icon) {
		this.icon.setIcon(icon);
	}		
}