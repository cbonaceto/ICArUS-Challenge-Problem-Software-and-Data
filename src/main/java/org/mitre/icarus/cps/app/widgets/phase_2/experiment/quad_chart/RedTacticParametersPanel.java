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
package org.mitre.icarus.cps.app.widgets.phase_2.experiment.quad_chart;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import org.mitre.icarus.cps.app.util.ProbabilityUtils;
import org.mitre.icarus.cps.app.widgets.phase_2.ColorManager_Phase2;
import org.mitre.icarus.cps.app.widgets.probability_entry.IProbabilityEntryComponent;
import org.mitre.icarus.cps.app.widgets.probability_entry.ProbabilityEntryConstants;
import org.mitre.icarus.cps.app.widgets.probability_entry.text_entry.ProbabilityTextField;
import org.mitre.icarus.cps.exam.phase_2.testing.bluebook.RedTacticParameters;

/**
 * Panel for showing (Missions 1 - 6) or specifying (Missions 5 - 6) Red tactic parameters.
 * 
 * @author CBONACETO
 *
 */
public class RedTacticParametersPanel extends QuadChartPanel<ProbabilityQuadChartComponent> {

	private static final long serialVersionUID = -4497293404809136911L;
	
	private boolean editable = true;
	
	private final boolean attackProbabilitiesUnknown;
	
	public RedTacticParametersPanel(String title, Double high_P_Threshold, Integer large_U_Threshold,
			Integer min_U, Integer max_U, boolean attackProbabilitiesUnknown) {
		this(title, high_P_Threshold, large_U_Threshold, min_U, max_U, 
				attackProbabilitiesUnknown, QuadChartPanelFactory.DEFAULT_CELL_WIDTH);
	}
	
	public RedTacticParametersPanel(String title, Double high_P_Threshold, Integer large_U_Threshold,
			Integer min_U, Integer max_U, boolean attackProbabilitiesUnknown, int cellWidth) {
		super(createProbabilityEntryComponents(attackProbabilitiesUnknown));
		this.attackProbabilitiesUnknown = attackProbabilitiesUnknown;
		if(attackProbabilitiesUnknown) { editable = false; }
		setTitle(title);
		setColumnHeadings(createColumnHeadings(large_U_Threshold, min_U, max_U));
		setRowHeadings(createRowHeadings(high_P_Threshold));
		setCellBackground(ColorManager_Phase2.getColor(ColorManager_Phase2.BLUEBOOK_UNHIGHLIGHTED));//ColorManager_Phase2.BLUEBOOK));
		setTitleFont(QuadChartPanelFactory.TITLE_FONT);
		setHeadingsFont(QuadChartPanelFactory.HEADING_FONT);
		Dimension preferredSize = getCellPreferredSize();
		int width = preferredSize.width > preferredSize.height ? preferredSize.width : preferredSize.height;
		width += 20;
		width = width < cellWidth ? cellWidth : width;
		setCellPreferredSize(new Dimension(width, width));
	}
	
	/**
	 * @param redTactic
	 * @param min_U
	 * @param max_U
	 */
	public void setRedTacticParameters(RedTacticParameters redTactic, Integer min_U, Integer max_U) {
		if(redTactic != null) {
			setRedTacticParameters(redTactic.getHigh_P_Threshold(), 
					redTactic.getLarge_U_Threshold(), min_U, max_U);
		}
	}
	
	/**
	 * @param high_P_Threshold
	 * @param large_U_Threshold
	 * @param min_U
	 * @param max_U
	 */
	public void setRedTacticParameters(Double high_P_Threshold, Integer large_U_Threshold,
			Integer min_U, Integer max_U) {
		setColumnHeadings(createColumnHeadings(large_U_Threshold, min_U, max_U));
		setRowHeadings(createRowHeadings(high_P_Threshold));
	}
	
	protected static ArrayList<ProbabilityQuadChartComponent> createProbabilityEntryComponents(
			boolean attackProbabilitiesUnknown) {
		ArrayList<ProbabilityQuadChartComponent> probabilityComponents = 
				new ArrayList<ProbabilityQuadChartComponent>(4);
		for(int i=0; i<4; i++) {
			if(attackProbabilitiesUnknown) {
				probabilityComponents.add(new ProbabilityQuadChartComponent("?",
						ProbabilityEntryConstants.FONT_PROBABILITY));
			} else {
				IProbabilityEntryComponent component = new ProbabilityTextField(null, false, true, i, null);
				probabilityComponents.add(new ProbabilityQuadChartComponent(component));
			}
		}
		return probabilityComponents;		
	}
	
	protected List<String> createRowHeadings(Double high_P_Threshold) {
		ArrayList<String> rowHeadings = new ArrayList<String>(2);
		String p = ProbabilityUtils.convertDecimalProbToPercentProb(high_P_Threshold).toString();
		rowHeadings.add("P > " + p + "%");
		rowHeadings.add("P <= " + p + "%");
		return rowHeadings;
	}
	
	protected List<String> createColumnHeadings(Integer large_U_Threshold,
			Integer min_U, Integer max_U) {
		ArrayList<String> columnHeadings = new ArrayList<String>(2);
		ArrayList<Integer> small_U_values = new ArrayList<Integer>();
		ArrayList<Integer> large_U_values = new ArrayList<Integer>();
		for(int i = min_U; i <= max_U; i++) {
			if(i <= large_U_Threshold) {
				small_U_values.add(i);
			} else {
				large_U_values.add(i);
			}
		}
		columnHeadings.add(create_U_ColumnString(small_U_values));
		columnHeadings.add(create_U_ColumnString(large_U_values));
		return columnHeadings;
	}
	
	protected String create_U_ColumnString(List<Integer> uValues) {
		StringBuilder sb = new StringBuilder("U = ");
		int i = 0;
		for(Integer u : uValues) {
			if(i > 0) {
				if(uValues.size() == 2) {
					sb.append(" or ");
				} else {
					if(i == uValues.size() - 1) {
						sb.append(" or ");
					} else {
						sb.append(", ");
					}
				}
			}
			sb.append(u.toString());			
			i++;
		}
		return sb.toString();
	}
	
	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		if(!attackProbabilitiesUnknown && editable != this.editable) {
			this.editable = editable;
			for(ProbabilityQuadChartComponent component : cells) {
				component.getProbabilityEntryComponent().setEditable(editable);
			}
		}
	}
	
	public List<Integer> getAttackProbabilities() {
		return getAttackProbabilities(null);	
	}
	
	public List<Integer> getAttackProbabilities(List<Integer> attackProbabilities) {
		if(attackProbabilities == null || attackProbabilities.size() < 4) {
			attackProbabilities = new ArrayList<Integer>(4);
			for(int i=0; i<4; i++) {
				attackProbabilities.add(0);
			}
		}
		for(int i=0; i<4; i++) {			
			attackProbabilities.set(i, attackProbabilitiesUnknown ? null : 
					cells.get(i).getProbabilityEntryComponent().getDisplaySetting());
		}
		return attackProbabilities;	
	}
	
	public void setAttackProbabilities_Percent(List<Integer> attackProbabilities) {
		if(!attackProbabilitiesUnknown && 
			attackProbabilities != null && attackProbabilities.size() == 4) {
			int i=0;
			for(Integer prob : attackProbabilities) {
				cells.get(i).getProbabilityEntryComponent().setCurrentSetting(prob);
				i++;
			}
		}
	}
	
	public void setAttackProbabilities_Decimal(List<Double> attackProbabilities) {			 
		if(!attackProbabilitiesUnknown && 
				attackProbabilities != null && attackProbabilities.size() == 4) {
			int i=0;
			for(Double prob : attackProbabilities) {
				cells.get(i).getProbabilityEntryComponent().setCurrentSetting(
						ProbabilityUtils.convertDecimalProbToPercentProb(prob));
				i++;
			}
		}
	}
}