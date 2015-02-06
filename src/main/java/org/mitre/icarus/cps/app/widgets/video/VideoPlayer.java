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
package org.mitre.icarus.cps.app.widgets.video;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Panel with a VideoPanel and a VideoControlPanel that controls the VideoPanel.
 * 
 * @author Eric Kappotis
 *
 */
public class VideoPlayer extends JPanel {
	
	private static final long serialVersionUID = 5027941599939928105L;
	
	private final VideoPanel videoPanel;
	
	private final VideoControlPanel controlPanel;
	
	public VideoPlayer(Window parent) {	
		videoPanel = new VideoPanel(parent);
		
		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		
		this.add(videoPanel, constraints);
		
		constraints.gridx = 0;
		constraints.gridy = 1;
		//constraints.weightx = 0;
		constraints.weighty = 0;
		controlPanel = new VideoControlPanel(videoPanel.getMediaPlayer());
		
		this.add(controlPanel, constraints);
	}
	
	/**
	 * @return the controlPanel
	 */
	public VideoControlPanel getControlPanel() {
		return controlPanel;
	}

	/**
	 * @return the videoPanel
	 */
	public VideoPanel getVideoPanel() {
		return videoPanel;
	}
	
	public void playVideo(URL videoUrl) {
		videoPanel.playVideo(videoUrl);
	}
	
	public void playVideo(final String videoFile) {
		videoPanel.playVideo(videoFile);
	}
	
	public void disposeVideoResources() {
		videoPanel.disposeVideoResources();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		final VideoPlayer videoPlayer = new VideoPlayer(frame);		
		frame.getContentPane().add(videoPlayer);
		videoPlayer.playVideo("data/Phase_1_CPD/Pilot Experiment/tutorials/PilotStudy_TutorialVideo_Task1.mp4");
		
		frame.pack();
		frame.setVisible(true);	
	}
}
