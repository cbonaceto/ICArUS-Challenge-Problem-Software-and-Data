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

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Creates a JFrame and adds a VideoPlayer to it.
 * 
 * @author CBONACETO
 *
 */
public class VideoWindow extends JFrame {

	private static final long serialVersionUID = -2637564007882718216L;
	
	//protected JLabel titleLabel;
	
	protected VideoPlayer videoPlayer;
	
	public VideoWindow() {
		this(null);
	}
	
	public VideoWindow(Component parent) {
		JPanel contentPanel = new JPanel(new GridBagLayout());
		contentPanel.setBackground(Color.black);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		/*titleLabel = new JLabel("Now Playing: ");
		titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 14));
		titleLabel.setForeground(Color.white);
		gbc.insets.left = 10;
		gbc.insets.top = 10;
		gbc.insets.bottom = 10;
		contentPanel.add(titleLabel, gbc);*/
		
		//if(parent == null) {
		videoPlayer = new VideoPlayer(this);
		//}
		//else {
			//videoPlayer = new VideoPlayer(parent);
		//}
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets.left = 0;
		gbc.insets.top = 0;
		gbc.insets.bottom = 0;		
		contentPanel.add(videoPlayer, gbc);
		
		setContentPane(contentPanel);
	}
	
	public void setVideoTitle(String title) {
		setTitle("Now Playing: " + title);
	}
	
	public void playVideo(URL videoUrl) {
		videoPlayer.playVideo(videoUrl);
	}
	
	public void playVideo(final String videoFile) {
		videoPlayer.playVideo(videoFile);
	}	
	
	public void disposeVideoResources() {
		videoPlayer.disposeVideoResources();
	}
}