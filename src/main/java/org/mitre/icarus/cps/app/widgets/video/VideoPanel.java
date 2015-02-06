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

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.CanvasVideoSurface;

/**
 * Panel for displaying a video using the VLCJ library.
 * 
 * @author Eric Kappotis
 *
 */
public class VideoPanel extends JPanel {
	
	private static final long serialVersionUID = 8319747901864362894L;
	
	private final MediaPlayerFactory mediaPlayerFactory;
	
	private final EmbeddedMediaPlayer mediaPlayer;
	
	private final CanvasVideoSurface videoSurface;
	
	private final Canvas canvas;
	
	private JLabel messageLabel;
	
	//private String videoLocation;	
	//private Dimension preferredSize;
	
	private Window parent;
	
	public VideoPanel(Window parent) {
		super(new GridBagLayout());
		this.parent = parent;
		setBackground(Color.black);
		
		if(parent != null) {
			parent.addWindowListener(new WindowAdapter() {			
				@Override
				public void windowClosed(WindowEvent arg0) {
					disposeVideoResources();
				}

				@Override
				public void windowClosing(WindowEvent event) {
					disposeVideoResources();
				}			
			});	
		}
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		
		messageLabel = new JLabel();
		messageLabel.setFont(messageLabel.getFont().deriveFont(Font.BOLD, 14));
		messageLabel.setPreferredSize(new Dimension(640, 480));
		messageLabel.setOpaque(true);
		messageLabel.setForeground(Color.red);
		messageLabel.setBackground(Color.black);
		messageLabel.setHorizontalAlignment(JLabel.CENTER);
		//messageLabel.setVerticalAlignment(JLabel.CENTER);
		constraints.fill = GridBagConstraints.HORIZONTAL;
		messageLabel.setVisible(true);
		add(messageLabel, constraints);
		
		canvas = new Canvas();
	    canvas.setBackground(Color.black);	    
	    
		mediaPlayerFactory = new MediaPlayerFactory(new String[] {"--no-video-title-show"});
		mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer();
		videoSurface = mediaPlayerFactory.newVideoSurface(canvas);
		
		mediaPlayer.setVideoSurface(videoSurface);		
		
		constraints.gridy = 1;
		constraints.fill = GridBagConstraints.BOTH;
		add(canvas, constraints);		
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#getPreferredSize()
	 */
	/*@Override
	public Dimension getPreferredSize() {
		if(preferredSize != null) {
			return preferredSize;
		}
		Dimension size = mediaPlayer.getVideoDimension();
		if(size != null) {
			System.out.println(size);
			return size;
		}
		System.out.println("(640, 480)");
		return new Dimension(640, 480);
	}*/

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setPreferredSize(java.awt.Dimension)
	 */
	/*@Override
	public void setPreferredSize(Dimension preferredSize) {
		this.preferredSize = preferredSize;
	}*/
	
	public void playVideo(URL videoUrl) {
		playVideo(videoUrl.getFile());
	}
	
	public void playVideo(final String videoFile) {
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				//Stop previous video
				if(mediaPlayer.isPlaying()) {
					mediaPlayer.stop();
				}
				
				//Wait 5 seconds for the canvas to be displayed
				long endTime = System.currentTimeMillis() + 5000;
				while(!canvas.isDisplayable()) {
					if(System.currentTimeMillis() > endTime) {
						messageLabel.setText("<html>Error, could not play video:<br>" + videoFile + "</html>");
						messageLabel.setVisible(true);
						canvas.setPreferredSize(new Dimension(0, 0));
						revalidate();
						if(parent != null) {
							parent.pack();
						}
						throw new IllegalStateException("The canvas containing the video could not be displayed.");
					}
					try {
						Thread.sleep(25);
					}
					catch (InterruptedException interruptedException) {}
				}				
				
				messageLabel.setText("Loading Video...");
				messageLabel.setVisible(true);
				canvas.setPreferredSize(new Dimension(0, 0));
				revalidate();
				if(parent != null) {
					parent.pack();
				}		
				
				mediaPlayer.playMedia(videoFile);				
				
				//Wait 5 seconds to for the video to start playing
				endTime = System.currentTimeMillis() + 5000;
				while(mediaPlayer.getVideoDimension() == null) {
					if(System.currentTimeMillis() > endTime) {
						messageLabel.setText("<html>Error, could not play video:<br>" + videoFile + "</html>");
						throw new IllegalStateException("Could not play video.");
					}
					try {
						Thread.sleep(25);
					}
					catch (InterruptedException interruptedException) {}
				}
				
				messageLabel.setVisible(false);
				canvas.setPreferredSize(mediaPlayer.getVideoDimension());				
				revalidate();
				if(parent != null) {
					parent.pack();
				}
				
				return null;
			}
			
		};
		worker.execute();
	}

	/*public void start() {
		mediaPlayer.playMedia(videoLocation);
	}*/
	
	public EmbeddedMediaPlayer getMediaPlayer() {
		return this.mediaPlayer;
	}
	
	public void disposeVideoResources() {
		//System.out.println("releasing resources");
		mediaPlayer.release();
        mediaPlayerFactory.release();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		
		final VideoPanel videoPanel = new VideoPanel(frame);			
		
		frame.getContentPane().add(videoPanel);
		videoPanel.playVideo("data/Phase_1_CPD/Pilot Experiment/tutorials/PilotStudy_TutorialVideo_Task1.mp4");
		
		frame.pack();
		frame.setVisible(true);
	}
}