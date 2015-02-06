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
package org.mitre.icarus.cps.app.widgets.phase_1.dialog;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import org.mitre.icarus.cps.feature_vector.phase_1.GridSize;

/**
 * Dialog to configure grid size settings.
 * 
 * @author CBONACETO
 *
 */
public class GridSizeDlg {
	
	protected GridSize gridSize;
	
	protected GridSizeDlg() {}
	
	public static GridSize showDialog(Component parent, String dialogTitle, GridSize currentGridSize) {		
		final GridSizeDlg gridSizeDlg = new GridSizeDlg();		
		
		final JDialog dlg;
		if(parent instanceof Window) {
			dlg = new JDialog((Window)parent);
		} else {
			dlg = new JDialog();
		}		
		dlg.setTitle(dialogTitle);
		dlg.setModal(true);
		dlg.setResizable(false);
		
		JPanel panel = new JPanel(new GridBagLayout());		
		GridBagConstraints gbc = new GridBagConstraints();
		final GridSizePanel gridSizePanel = new GridSizePanel(currentGridSize);
		gridSizePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		panel.add(gridSizePanel, gbc);
		
		JPanel buttonPanel = new JPanel();
		JButton okButton = new JButton("OK");
		buttonPanel.add(okButton);		
		JButton cancelButton = new JButton("Cancel");
		buttonPanel.add(cancelButton);
		gbc.gridy = 1;
		gbc.insets.top = 10;
		panel.add(buttonPanel, gbc);		
		
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(gridSizePanel.validateFields()) {
					gridSizeDlg.gridSize = gridSizePanel.getGridSize();
					dlg.dispose();
				}
				else {
					gridSizeDlg.gridSize = null;
				}
			}
		});
		
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gridSizeDlg.gridSize = null;
				dlg.dispose();
			}
		});
		
		dlg.getContentPane().add(panel);
		dlg.pack();
		dlg.setLocationRelativeTo(parent);
		dlg.setVisible(true);				
		
		return gridSizeDlg.gridSize;
	}
}