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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Arrays;
import java.util.Collection;

import javax.swing.SwingConstants;

import org.mitre.icarus.cps.app.util.ProbabilityUtils;
import org.mitre.icarus.cps.app.widgets.WidgetConstants;
import org.mitre.icarus.cps.app.widgets.phase_2.ColorManager_Phase2;
import org.mitre.icarus.cps.app.widgets.phase_2.WidgetConstants_Phase2;
import org.mitre.icarus.cps.app.widgets.probability_entry.ProbabilityEntryConstants;
import org.mitre.icarus.cps.exam.phase_2.testing.bluebook.PayoffMatrix;
import org.mitre.icarus.cps.exam.phase_2.testing.bluebook.RedTactic;
import org.mitre.icarus.cps.exam.phase_2.testing.bluebook.RedTacticParameters;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.BlueAction.BlueActionType;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.RedAction.RedActionType;
import org.mitre.icarus.cps.feature_vector.phase_2.int_datum.ImintDatum;
import org.mitre.icarus.cps.feature_vector.phase_2.int_datum.SigintReliability;

/**
 * Contains static methods for created different quad chart panels.
 * 
 * @author CBONACETO
 *
 */
public class QuadChartPanelFactory {
	
	public static final Font TITLE_FONT = WidgetConstants.FONT_PANEL_TITLE;
	
	public static final Font HEADING_FONT = WidgetConstants_Phase2.FONT_DATUM_LABEL;
	
	public static final Font CELL_FONT = ProbabilityEntryConstants.FONT_PROBABILITY;
	
	public static final int DEFAULT_CELL_WIDTH = 100;
	
	private QuadChartPanelFactory() {}
	
	/**
	 * @param redTactics
	 * @return
	 */
	public static BlueBookPanel createBlueBookPanel(Collection<RedTactic> redTactics, 
			boolean attackProbabilitiesUnknown) {
		return new BlueBookPanel(redTactics, attackProbabilitiesUnknown);
	}
	
	/**
	 * @param title
	 * @param redTactic
	 * @param setAttackProbabilities
	 * @param editable
	 * @return
	 */
	public static RedTacticParametersPanel createRedTacticParametersPanel(String title, 
			RedTacticParameters redTactic, Integer min_U, Integer max_U,
			boolean setAttackProbabilities, boolean attackProbabilitiesUnknown,
			boolean editable) {		
		RedTacticParametersPanel rtp = createRedTacticParametersPanel(title, 
				redTactic != null ? redTactic.getHigh_P_Threshold() : RedTacticParameters.DEFAULT_HIGH_P_THRESHOLD, 
				redTactic != null ? redTactic.getLarge_U_Threshold() : RedTacticParameters.DEFAULT_LARGE_U_THRESHOLD, 
				min_U != null ? min_U : ImintDatum.MIN_U, max_U != null ? max_U : ImintDatum.MAX_U, 
				attackProbabilitiesUnknown, editable);
		if(setAttackProbabilities && redTactic != null) {
			rtp.setAttackProbabilities_Decimal(redTactic.getAttackProbabilities());
		}
		return rtp;
	}
	
	/**
	 * @param title
	 * @param high_P_Threshold
	 * @param large_U_Threshold
	 * @param min_U
	 * @param max_U
	 * @param editable
	 * @return
	 */
	public static RedTacticParametersPanel createRedTacticParametersPanel(String title, 
			Double high_P_Threshold, Integer large_U_Threshold, Integer min_U, Integer max_U,
			boolean attackProbabilitiesUnknown, boolean editable) {
		RedTacticParametersPanel rtp = new RedTacticParametersPanel(title, high_P_Threshold, 
				large_U_Threshold, min_U, max_U, attackProbabilitiesUnknown);
		rtp.setEditable(editable);
		return rtp;
	}
	
	/**
	 * @param sigintReliability
	 * @return
	 */
	public static QuadChartPanel<JLabelQuadChartComponent> createSigintReliabilityPanel(
			SigintReliability sigintReliability) {
		QuadChartPanel<JLabelQuadChartComponent> qcp = new QuadChartPanel<JLabelQuadChartComponent>(
			Arrays.asList(
				new JLabelQuadChartComponent(
						ProbabilityUtils.convertDecimalProbToPercentProb(sigintReliability.getChatterLikelihood_attack()).toString() + "%"), 
				new JLabelQuadChartComponent(
						ProbabilityUtils.convertDecimalProbToPercentProb(sigintReliability.getChatterLikelihood_noAttack()).toString() + "%"),
				new JLabelQuadChartComponent(
						ProbabilityUtils.convertDecimalProbToPercentProb(sigintReliability.getSilenceLikelihood_attack()).toString() + "%"), 
				new JLabelQuadChartComponent(
						ProbabilityUtils.convertDecimalProbToPercentProb(sigintReliability.getSilenceLikelihood_noAttack()).toString() + "%")));
		qcp.setCellPreferredSize(new Dimension(DEFAULT_CELL_WIDTH, DEFAULT_CELL_WIDTH));
		qcp.setTitle("Likelihood of Signal");
		qcp.setTitleFont(TITLE_FONT);
		qcp.setTitleForeground(ColorManager_Phase2.getColor(ColorManager_Phase2.SIGINT));
		qcp.setCellFont(CELL_FONT);
		qcp.setRowHeadings(Arrays.asList(
				"<html><center>Signal =<br>Chatter</center></html>", 
				"<html><center>Signal =<br>Silence</center></html>"));
		qcp.setColumnHeadings(Arrays.asList("Attack", "Not Attack"));
		qcp.setHeadingsFont(HEADING_FONT);
		qcp.setCellForeground(ColorManager_Phase2.getColor(ColorManager_Phase2.SIGINT));
		qcp.setCellBackground(ColorManager_Phase2.getColor(ColorManager_Phase2.SIGINT_RELIABILITY_TABLE));
		qcp.setBackground(Color.white);
		qcp.setRowHeadingsBackground(Color.white);
		qcp.setColumnHeadingsBackground(Color.white);		
		return qcp;
	}
	
	/**
	 * @param payoffMatrix
	 * @return
	 */
	public static QuadChartPanel<JLabelQuadChartComponent> createPayoffMatrixPanel(
			PayoffMatrix payoffMatrix) {
		Color bluePlayerColor = ColorManager_Phase2.getColor(ColorManager_Phase2.BLUE_PLAYER);
		Color redPlayerColor = ColorManager_Phase2.getColor(ColorManager_Phase2.RED_PLAYER);
		QuadChartPanel<JLabelQuadChartComponent> qcp = new QuadChartPanel<JLabelQuadChartComponent>(
				Arrays.asList(
					new JLabelQuadChartComponent(
						createPayoffString(payoffMatrix, BlueActionType.Do_Not_Divert, RedActionType.Attack,
								bluePlayerColor, redPlayerColor)), 
					new JLabelQuadChartComponent(
						createPayoffString(payoffMatrix, BlueActionType.Do_Not_Divert, RedActionType.Do_Not_Attack,
								bluePlayerColor, redPlayerColor)),
					new JLabelQuadChartComponent(
						createPayoffString(payoffMatrix, BlueActionType.Divert, RedActionType.Attack,
								bluePlayerColor, redPlayerColor)), 
					new JLabelQuadChartComponent(
						createPayoffString(payoffMatrix, BlueActionType.Divert, RedActionType.Do_Not_Attack,
								bluePlayerColor, redPlayerColor))),
			SwingConstants.TOP, SwingConstants.LEFT);
		qcp.setCellPreferredSize(new Dimension(DEFAULT_CELL_WIDTH, DEFAULT_CELL_WIDTH));
		qcp.setTitleVisible(false);
		qcp.setCellFont(CELL_FONT);
		qcp.setRowHeadings(Arrays.asList("Not Divert", "Divert"));
		qcp.setColumnHeadings(Arrays.asList("Attack", "Not Attack"));
		qcp.setHeadingsFont(HEADING_FONT);
		qcp.setRowHeadingsForeground(bluePlayerColor);
		qcp.setColumnHeadingsForeground(redPlayerColor);			
		//qcp.setCellBackground(ColorManager_Phase2.getColor(ColorManager_Phase2.SIGINT_RELIABILITY_TABLE));
		qcp.setBackground(Color.white);
		qcp.setRowHeadingsBackground(ColorManager_Phase2.getColor(ColorManager_Phase2.BLUE_ACTION_CELL));
		qcp.setColumnHeadingsBackground(ColorManager_Phase2.getColor(ColorManager_Phase2.RED_ACTION_CELL));
		qcp.setOuterBorderVisible(true);
		qcp.setCellOuterBorderVisible(false);
		qcp.setHeadingBordersVisible(true);			
		return qcp;
	}
	
	/**
	 * @param payoffMatrix
	 * @param blueAction
	 * @param redAction
	 * @return
	 */
	protected static String createPayoffString(PayoffMatrix payoffMatrix,
			BlueActionType blueAction, RedActionType redAction,
			Color bluePlayerColor, Color redPlayerColor) {
		StringBuilder sb = new StringBuilder("<html><center>");
		int index = payoffMatrix.getPayoffIndex(blueAction, redAction);
		Double bluePayoff = payoffMatrix.getBluePayoffs().get(index);
		Double redPayoff = payoffMatrix.getRedPayoffs().get(index);		
		if(redPayoff == null || redPayoff.isNaN()) {
			//Showdown, payoff is +U or -U
			sb.append(createFontColorString(bluePlayerColor));
			sb.append("+U ");
			sb.append("</font>");
			sb.append(createFontColorString(redPlayerColor));
			sb.append("(-U)");
			sb.append("</font>");
			sb.append("<br>or<br>");
			sb.append(createFontColorString(bluePlayerColor));
			sb.append("-U ");
			sb.append("</font>");
			sb.append(createFontColorString(redPlayerColor));
			sb.append("(+U)");
			sb.append("</font>");
		} else {
			sb.append(createFontColorString(bluePlayerColor));
			if(bluePayoff >= 1) {
				sb.append("+");
			}
			sb.append(Integer.toString(bluePayoff.intValue()));
			sb.append("</font>");
			sb.append(createFontColorString(redPlayerColor));
			sb.append(" (");
			if(redPayoff >= 1) {
				sb.append("+");
			}
			sb.append(Integer.toString(redPayoff.intValue()));
			sb.append(")");
			sb.append("</font>");
		}
		sb.append("</center></html>");
		return sb.toString();
	}
	
	protected static String createFontColorString(Color color) {
		StringBuilder sb = new StringBuilder("<font color=rgb(");
		sb.append(Integer.toString(color.getRed()) + ",");
		sb.append(Integer.toString(color.getGreen()) + ",");
		sb.append(Integer.toString(color.getBlue()) + ")>");		
		return sb.toString();
	}
}