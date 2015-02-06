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

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import org.mitre.icarus.cps.app.widgets.ImageManager;

import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;

/**
 * Panel with playback controls to control a video in a VideoPanel.
 * 
 * @author Eric Kappotis
 *
 */
public class VideoControlPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7815424134291524454L;
	
	private JButton restartButton;
	private JButton pauseButton;
	private JButton forwardButton;
	private JButton rewindButton;
	
	private JLabel timeLabel;
	
	private JSlider slider;
	private MediaPlayer mediaPlayer;
	
	private boolean forwarding;
	private boolean rewinding;
	private boolean sliderAccessed;
	private boolean paused = false;
	
	public VideoControlPanel(MediaPlayer mediaPlayer) {
		
		this.mediaPlayer = mediaPlayer;
		mediaPlayer.setVolume(200);
		
		mediaPlayer.addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
			/* (non-Javadoc)
			 * @see uk.co.caprica.vlcj.player.MediaPlayerEventAdapter#positionChanged(uk.co.caprica.vlcj.player.MediaPlayer, float)
			 */
			@Override
			public void positionChanged(MediaPlayer mediaPlayer,
					float newPosition) {
				
				if(!sliderAccessed) {
					//System.out.println("Should not be getting here");
					long percentComplete = Math.round((double)mediaPlayer.getTime() / (double)mediaPlayer.getLength() * 100.00);				
					slider.setValue((int)percentComplete);
				}
				
				String labelText = millisecondsToTimeStr((int)mediaPlayer.getTime()) + " / " + millisecondsToTimeStr((int)mediaPlayer.getLength());
				timeLabel.setText(labelText);
			}			
		});
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		
		JPanel timePanel = new JPanel();
		timePanel.setLayout(new FlowLayout());
		
		timeLabel = new JLabel("--:-- / --:--");
		timePanel.add(timeLabel);
		
		slider = new JSlider();
		slider.addMouseListener(new MouseAdapter() {

			/* (non-Javadoc)
			 * @see java.awt.event.MouseAdapter#mousePressed(java.awt.event.MouseEvent)
			 */
			@Override
			public void mousePressed(MouseEvent arg0) {
				sliderAccessed = true;
			}

			/* (non-Javadoc)
			 * @see java.awt.event.MouseAdapter#mouseReleased(java.awt.event.MouseEvent)
			 */
			@Override
			public void mouseReleased(MouseEvent event) {
				sliderAccessed = false;
				float percentage = (float)slider.getValue() / (float)slider.getMaximum();
				getMediaPlayer().setPosition(percentage);
			}			
		});
		
		timePanel.add(slider);
	
		this.add(timePanel, constraints);
		
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		
		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new FlowLayout());
		
		restartButton = new JButton("Restart");
		restartButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				getMediaPlayer().stop();
				getMediaPlayer().play();
				
				paused = false;
				pauseButton.setText("Pause");
				pauseButton.setIcon(ImageManager.getImageIcon(ImageManager.CONTROL_PAUSE_ICON));
			}			
		});
		
		pauseButton = new JButton("Pause");
		pauseButton.setIcon(ImageManager.getImageIcon(ImageManager.CONTROL_PAUSE_ICON));
		pauseButton.setPreferredSize(pauseButton.getPreferredSize());
		pauseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				getMediaPlayer().pause();
				
				paused = !paused;
				
				if(!paused) {
					pauseButton.setText("Pause");
					pauseButton.setIcon(ImageManager.getImageIcon(ImageManager.CONTROL_PAUSE_ICON));
				}
				else {
					pauseButton.setText("Play");
					pauseButton.setIcon(ImageManager.getImageIcon(ImageManager.CONTROL_PLAY_ICON));
				}
			}				
		});
		rewindButton = new JButton("Rewind");
		rewindButton.setIcon(ImageManager.getImageIcon(ImageManager.CONTROL_REWIND_ICON));
		rewindButton.addMouseListener(new MouseAdapter() {
			/* (non-Javadoc)
			 * @see java.awt.event.MouseAdapter#mousePressed(java.awt.event.MouseEvent)
			 */
			@Override
			public void mousePressed(MouseEvent event) {
				rewinding = true;
				
				SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
					@Override
					protected Void doInBackground() throws Exception {
						//mediaPlayer.pause();
						while(rewinding) {
							float updatedPosition = getMediaPlayer().getPosition() - .01f;
							if(updatedPosition < 0.0f) {
								updatedPosition = 0.0f;
							}
							getMediaPlayer().setPosition(updatedPosition);
							
							final int sliderPosition = Math.round(updatedPosition * 100);
							
							SwingUtilities.invokeLater(new Runnable() {
								@Override
								public void run() {
									slider.setValue(sliderPosition);
								}						
							});							
							Thread.sleep(25);
						}				
						return null;
					}
					
				};
				worker.execute();
			}

			/* (non-Javadoc)
			 * @see java.awt.event.MouseAdapter#mouseReleased(java.awt.event.MouseEvent)
			 */
			@Override
			public void mouseReleased(MouseEvent event) {
				rewinding = false;
			}			
		});
		forwardButton = new JButton("Forward");
		forwardButton.setIcon(ImageManager.getImageIcon(ImageManager.CONTROL_FAST_FORWARD_ICON));
		forwardButton.addMouseListener(new MouseAdapter() {
			/* (non-Javadoc)
			 * @see java.awt.event.MouseAdapter#mousePressed(java.awt.event.MouseEvent)
			 */
			@Override
			public void mousePressed(MouseEvent event) {
				forwarding = true;
				
				SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
					@Override
					protected Void doInBackground() throws Exception {
						//mediaPlayer.pause();
						while(forwarding) {
							float updatedPosition = getMediaPlayer().getPosition() + .01f;
							if(updatedPosition > 1.0f) {
								updatedPosition = 1.0f;
							}
							getMediaPlayer().setPosition(updatedPosition);
							
							final int sliderPosition = Math.round(updatedPosition * 100);
							
							SwingUtilities.invokeLater(new Runnable() {
								@Override
								public void run() {
									slider.setValue(sliderPosition);
								}						
							});
							
							Thread.sleep(25);
						}				
						return null;
					}
					
				};
				worker.execute();
			}

			/* (non-Javadoc)
			 * @see java.awt.event.MouseAdapter#mouseReleased(java.awt.event.MouseEvent)
			 */
			@Override
			public void mouseReleased(MouseEvent event) {
				forwarding = false;
			}
		});
		
		controlPanel.add(restartButton);
		controlPanel.add(pauseButton);
		controlPanel.add(rewindButton);
		controlPanel.add(forwardButton);
		
		this.add(controlPanel, constraints);
	}
	
	private String millisecondsToTimeStr(int milliseconds) {
		
		int minutes = milliseconds / 60000;
		
		int remainingMilliseconds = milliseconds % 60000;
		
		int seconds = remainingMilliseconds / 1000;
		
		String strMinutes = String.valueOf(minutes);
		if(minutes < 10) {
			strMinutes = "0" + strMinutes;
		}
		
		String strSeconds = String.valueOf(seconds);
		if(seconds < 10) {
			strSeconds = "0" + strSeconds;
		}
		
		return strMinutes + ":" + strSeconds;
	}

	/**
	 * @return the mediaPlayer
	 */
	public MediaPlayer getMediaPlayer() {
		return mediaPlayer;
	}	
}
