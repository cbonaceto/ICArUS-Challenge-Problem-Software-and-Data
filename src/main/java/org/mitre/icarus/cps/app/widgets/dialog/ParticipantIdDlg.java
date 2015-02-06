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
package org.mitre.icarus.cps.app.widgets.dialog;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import javax.swing.JDialog;

import org.mitre.icarus.cps.app.experiment.IcarusSubjectData;
import org.mitre.icarus.cps.web.model.Site;

/**
 * Wraps ParticipantIDPanel in a JDialog.  Call to showDialog returns and IcarusSubjectData instance with
 * the subject ID and site that was entered, or null if dialog closed or cancelled.
 * 
 * @author CBONACETO
 *
 */
public class ParticipantIdDlg {
	
	private String id = null;
	
	private Site site = null;
	
	protected ParticipantIdDlg() {}
	
	/**
	 * @param parent
	 * @param dialogTitle
	 * @param sites
	 * @param okButtonText
	 * @param showCancelButton
	 * @return
	 */
	public static IcarusSubjectData showDialog(Component parent, String dialogTitle,
			Collection<Site> sites, Site currentSite, String okButtonText, 
			boolean showCancelButton) {
		
		final ParticipantIdDlg idDlg = new ParticipantIdDlg();
		final JDialog dlg;
		if(parent instanceof Window) {
			dlg = new JDialog((Window)parent);
		} else {
			dlg = new JDialog();	
		}		
		dlg.setResizable(false);
		dlg.setTitle(dialogTitle);
		dlg.setModal(true);
		
		final ParticipantIdPanel panel = new ParticipantIdPanel(sites, okButtonText, showCancelButton);
		if(currentSite != null) {
			panel.setSite(currentSite);
		}
		panel.addOkButtonActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				idDlg.id = panel.getSubjectId();
				idDlg.site = panel.getSite();
				dlg.dispose();
			}
		});
		panel.addCancelButtonActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dlg.dispose();
			}
		});
		
		dlg.getContentPane().add(panel);
		dlg.pack();
		dlg.setLocationRelativeTo(parent);
		dlg.setVisible(true);
		
		if(idDlg.id != null) {
			return new IcarusSubjectData(idDlg.id, idDlg.site, 0);	
		}
		return null;
	}
}