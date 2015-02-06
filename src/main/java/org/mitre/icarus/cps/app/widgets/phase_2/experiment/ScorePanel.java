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

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.NumberFormat;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.mitre.icarus.cps.app.widgets.phase_2.ColorManager_Phase2;
import org.mitre.icarus.cps.app.widgets.phase_2.WidgetConstants_Phase2;
import org.mitre.icarus.cps.exam.phase_2.testing.PlayerType;

/**
 * Panel that shows the Blue and Red player scores.
 * 
 * @author CBONACETO
 *
 */
public class ScorePanel extends JPanel {	
	private static final long serialVersionUID = -8372413549752332783L;
	
	/** Blue player trial score label */
	protected JLabel blueTrialScoreLabel;
	
	/** Red player trial score label */
	protected JLabel redTrialScoreLabel;	
	
	/** Blue player mission score label */
	protected JLabel blueMissionScoreLabel;
	
	/** Red player mission score label */
	protected JLabel redMissionScoreLabel;
	
	/** Score string formatter */
	protected static NumberFormat numberFormat;
	
	/** The number of decimal places to show in the score */
	protected static int scoreDecimalPlaces = 0;
	
	public ScorePanel() {
		this(true, true);
	}

	public ScorePanel(boolean showTrialScore, boolean showMissionScore) {
		super(new GridBagLayout());
		if(numberFormat == null) {
			numberFormat = NumberFormat.getNumberInstance();
			numberFormat.setMaximumFractionDigits(scoreDecimalPlaces);
			numberFormat.setMinimumFractionDigits(scoreDecimalPlaces);
		}
		setBackground(Color.WHITE);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(3, 0, 3, 0);
		
		blueTrialScoreLabel = createScoreLabel(PlayerType.Blue);
		redTrialScoreLabel = createScoreLabel(PlayerType.Red);
		blueMissionScoreLabel = createScoreLabel(PlayerType.Blue);
		redMissionScoreLabel = createScoreLabel(PlayerType.Red);
		
		if(showTrialScore) {
			JLabel trialLabel = new JLabel("Score on the Trial:");
			trialLabel.setFont(WidgetConstants_Phase2.FONT_SCORE_BANNER);
			gbc.insets.left = 2;
			gbc.insets.right = 10;
			add(trialLabel, gbc);
			gbc.gridx++;
			gbc.insets.left = 0;
			gbc.insets.right = 6;
			add(blueTrialScoreLabel, gbc);
			gbc.gridx++;
			gbc.insets.right = showMissionScore ? 44 : 2;
			add(redTrialScoreLabel, gbc);
			gbc.gridx++;
		}
		
		if(showMissionScore) {
			JLabel missionLabel = new JLabel("Score on the Mission:");
			missionLabel.setFont(WidgetConstants_Phase2.FONT_SCORE_BANNER);
			gbc.insets.left = 0;
			gbc.insets.right = 10;
			add(missionLabel, gbc);
			gbc.gridx++;
			gbc.insets.left = 0;
			gbc.insets.right = 6;
			add(blueMissionScoreLabel, gbc);
			gbc.gridx++;
			gbc.insets.right = 2;
			add(redMissionScoreLabel, gbc);
		}	
	}
	
	public static void setScoreDecimalPlaces(int scoreDecimalPlaces) {
		if(numberFormat == null || scoreDecimalPlaces != ScorePanel.scoreDecimalPlaces) {
			numberFormat = NumberFormat.getNumberInstance();
			numberFormat.setMaximumFractionDigits(scoreDecimalPlaces);
			numberFormat.setMinimumFractionDigits(scoreDecimalPlaces);
		}
	}
	
	/**
	 * @param playerType
	 * @return
	 */
	protected JLabel createScoreLabel(PlayerType playerType) {
		JLabel label = new JLabel();
		label = new JLabel(playerType == PlayerType.Blue ? "Blue = -999" : "Red = -999");		
		label.setFont(WidgetConstants_Phase2.FONT_SCORE_BANNER);
		label.setPreferredSize(label.getPreferredSize());
		label.setText(playerType == PlayerType.Blue ? "Blue:" : "Red:");
		label.setForeground(playerType == PlayerType.Blue ? ColorManager_Phase2.getColor(ColorManager_Phase2.BLUE_PLAYER) :
			ColorManager_Phase2.getColor(ColorManager_Phase2.RED_PLAYER));
		return label;
	}
	
	public void setBlueTrialScore(Double blueScore) {
		if(blueTrialScoreLabel != null) {
			blueTrialScoreLabel.setText(blueScore == null ? "" : "Blue = " + formatScore(blueScore));
		}
	}
	
	public void setRedTrialScore(Double redScore) {
		if(redTrialScoreLabel != null) {
			redTrialScoreLabel.setText(redScore == null ? "" : "Red = " + formatScore(redScore));
		}
	}
	
	public void setBlueMissionScore(Double blueScore) {
		if(blueMissionScoreLabel != null) {
			blueMissionScoreLabel.setText(blueScore == null ? "" : "Blue = " + formatScore(blueScore));
		}
	}
	
	public void setRedMissionScore(Double redScore) {
		if(redMissionScoreLabel != null) { 
			redMissionScoreLabel.setText(redScore == null ? "" : "Red = " + formatScore(redScore));
		}
	}
	
	/**
	 * @param score
	 * @return
	 */
	public static String formatScore(Double score) {
		return score == null ? "" : numberFormat.format(score);
	}

	@Override
	public void setFont(Font font) {
		super.setFont(font);
		setScoreLabelFont(blueTrialScoreLabel, font, PlayerType.Blue);
		setScoreLabelFont(redTrialScoreLabel, font, PlayerType.Red);
		setScoreLabelFont(blueMissionScoreLabel, font, PlayerType.Blue);
		setScoreLabelFont(redMissionScoreLabel, font, PlayerType.Red);
	}
	
	protected void setScoreLabelFont(JLabel label, Font font, PlayerType playerType) {
		if(label != null) {
			String text = label.getText();
			label.setFont(font);
			label.setText(playerType == PlayerType.Blue ? "Blue = -999" : "Red = -999");
			label.setPreferredSize(label.getPreferredSize());
			label.setText(text);
		}
	}
}