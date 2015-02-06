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
package org.mitre.icarus.cps.app.widgets.phase_2.experiment.datum_list;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;

import org.mitre.icarus.cps.app.widgets.phase_2.WidgetConstants_Phase2;


/**
 * Panel containing a list of datum labels, either in list or tabular format. The datum labels may also have
 *  check boxes next to them that are rendered as active or inactive (to highlight the current datum item).
 * 
 * @author CBONACETO
 *
 */
public class DatumListPanel extends JPanel {	
	private static final long serialVersionUID = -2492448318209052284L;	
	
	/** The datum labels */
	protected List<DatumLabel> datumLabels;
	
	public DatumListPanel() {		
		super(new GridBagLayout());
	}
	
	/**
	 * @param datumLabel
	 * @param fillHorizontally
	 */
	public void addDatumLabel(DatumLabel datumLabel, boolean fillHorizontally) {
		setDatumLabels(Collections.singleton(datumLabel), fillHorizontally, true);
	}
	
	/**
	 * @param datumLabels
	 * @param fillHorizontally
	 */
	public void addDatumLabels(Collection<DatumLabel> datumLabels, boolean fillHorizontally) {
		setDatumLabels(datumLabels, fillHorizontally, true);
	}
	
	/**
	 * @param datumLabels
	 * @param fillHorizontally
	 */
	public void setDatumLabels(Collection<DatumLabel> datumLabels, boolean fillHorizontally) {
		setDatumLabels(datumLabels, fillHorizontally, false);
	}
	
	protected void setDatumLabels(Collection<DatumLabel> datumLabels, boolean fillHorizontally, 
			boolean append) {
		//this.datumLabels = datumLabels;
		if(this.datumLabels == null) {
			this.datumLabels = new LinkedList<DatumLabel>();		
		} else if(!append) {
			this.datumLabels.clear();
		}
		if(!append) {
			removeAll();
		}
		if(datumLabels != null && !datumLabels.isEmpty()) {
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx = 0;
			gbc.gridy = this.datumLabels.size();
			gbc.weightx = 1;
			gbc.insets.right = 6;
			gbc.anchor = GridBagConstraints.NORTHWEST;
			gbc.fill = fillHorizontally ? GridBagConstraints.HORIZONTAL : GridBagConstraints.NONE;
			int i = this.datumLabels.size();
			int numDatumLabels = this.datumLabels.size() + datumLabels.size();
			for(DatumLabel label : datumLabels) {
				this.datumLabels.add(label);
				label.setOpaque(isOpaque());
				label.setBackground(getBackground());
				gbc.insets.left = label.indentLevel;
				gbc.insets.top = i > 0 ? (label.isTitleLabel() ? WidgetConstants_Phase2.DATUM_LIST_VERTICAL_SPACER + 10 :
					WidgetConstants_Phase2.DATUM_LIST_VERTICAL_SPACER) : 0;
				gbc.weighty = i == numDatumLabels - 1 ? 1 : 0;
				add(label, gbc);
				i++;
				gbc.gridy++;							
			}
		}
		revalidate();
		repaint();
	}
	
	public void clearDataumLabels() {
		datumLabels = null;
		removeAll();
		revalidate();
		repaint();
	}

	@Override
	public void setOpaque(boolean isOpaque) {
		super.setOpaque(isOpaque);
		if(datumLabels != null && isOpaque != isOpaque()) {
			for(DatumLabel label : datumLabels) {
				label.setOpaque(isOpaque);
			}
		}
	}

	@Override
	public void setBackground(Color bg) {
		super.setBackground(bg);
		if(datumLabels != null) {
			for(DatumLabel label : datumLabels) {
				label.setBackground(bg);
			}
		}
	}
}