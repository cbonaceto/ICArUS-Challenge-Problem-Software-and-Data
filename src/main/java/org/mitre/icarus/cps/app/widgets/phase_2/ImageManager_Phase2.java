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
package org.mitre.icarus.cps.app.widgets.phase_2;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.mitre.icarus.cps.app.widgets.AbstractImageManager;
import org.mitre.icarus.cps.app.widgets.ImageManager;
import org.mitre.icarus.cps.app.widgets.map.MapConstants;
import org.mitre.icarus.cps.app.widgets.map.phase_2.MapConstants_Phase2;
import org.mitre.icarus.cps.app.widgets.map.phase_2.MutableRenderProperties;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.BlueLocationPlacemark;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.BlueLocationPlacemark.DisplayMode;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.BlueLocationPlacemark.DisplaySize;
import org.mitre.icarus.cps.assessment.score_computer.phase_2.ScoreComputer_Phase2.RedBluePayoff;
import org.mitre.icarus.cps.exam.phase_2.testing.PlayerType;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.RedAction.RedActionType;
import org.mitre.icarus.cps.feature_vector.phase_2.BlueLocation;
import org.mitre.icarus.cps.feature_vector.phase_2.coordinate.GeoCoordinate;

/**
 * Manages Phase 2-specific application images.
 * 
 * @author CBONACETO
 *
 */
public class ImageManager_Phase2 extends AbstractImageManager {
	
	/** Placemark-based icon size */
	public static final Dimension PLACEMARK_ICON_SIZE = 
		new Dimension(MapConstants.PLACEMARK_SIZE_PIXELS + 4, MapConstants.PLACEMARK_SIZE_PIXELS + 4);	
	
	/** Icon Types for Phase 2 */
	public static final String ROADS_LAYER_ICON = "ROADS_LAYER";
	public static final String RED_ATTACK_BLUE_WIN_ICON = "RED_ATTACK_BLUE_WIN";
	public static final String RED_ATTACK_RED_WIN_ICON = "RED_ATTACK_RED_WIN";
	public static final String RED_NOT_ATTACK_ICON = "RED_NOT_ATTACK";
	
	public static final String BLUEBOOK_ICON = "BLUEBOOK";
	public static final String PAYOFF_MATRIX_ICON = "PAYOFF_MATRIX_ICON";
	public static final String SIGINT_RELIABILITY_ICON = "SIGINT_RELIABILITY_ICON";	
	public static final String BATCH_PLOTS_ICON = "BATCH_PLOTS_ICON";
	public static final String PREVIOUS_OUTCOME_ICON = "PREVIOUS_OUTCOME_ICON";
	public static final String NEXT_OUTCOME_ICON = "NEXT_OUTCOME_ICON";
	/**************************/
	
	/** Initialize the images */
	static {
		createIcons();
	}
	
	private ImageManager_Phase2() {}
	
	/**
	 * Get the image icon for the given imageType.
	 */
	public static ImageIcon getImageIcon(String imageType) {
		return ImageManager.getImageIcon(imageType);
	}
	
	/**
	 * Get the image icon for the given imageType.
	 */
	public static Image getImage(String imageType) {
		return ImageManager.getImage(imageType);
	}
	
	/** Loads the images */
	private static void createIcons() {
		createLayerIcons();
		createCommandButtonIcons();
	}

	/** Create layer icons (used in the layer panel and legend) */
	private static void createLayerIcons() {
		try {
			MutableRenderProperties renderData = new MutableRenderProperties(null);
			renderData.setGeoTranslationsDisabled(true);

			//Create Roads layer icon
			try {
				ImageManager.addImage(ROADS_LAYER_ICON, 
					new ImageIcon(ImageIO.read(ImageManager.class.getClassLoader().getResourceAsStream("images/phase_2/roads_layer_icon.png"))));
			} catch (Exception e) {			
				e.printStackTrace();
			}
			
			//Create the Red attacked, Blue won, batch plot outcome icon
			int outcomeSize = MapConstants_Phase2.BLUE_OUTCOME_PLACEMARK_SIZE_PIXELS;
			int iconSize = outcomeSize + 4;
			BlueLocation location = new BlueLocation();
			location.setLocation(new GeoCoordinate((double)iconSize/2, (double)iconSize/2));
			BlueLocationPlacemark blueLocation = new BlueLocationPlacemark(location, null, false);
			blueLocation.setRedAction(RedActionType.Attack);
			RedBluePayoff redBluePayoff = new RedBluePayoff();
			redBluePayoff.setWinner(PlayerType.Blue);
			blueLocation.setRedBluePayoff(redBluePayoff);
			blueLocation.setDisplayMode(DisplayMode.AttackOutcomeMode);
			blueLocation.setDisplaySize(DisplaySize.BatchPlotSize);			
			renderData.setViewportBounds(new Rectangle(0, 0, iconSize, iconSize));			
			//System.out.println(blueLocation.getCenterGeoLocation());
			//System.out.println(renderData.getRenderProperties().geoPositionToWorldPixel(blueLocation.getCenterGeoLocation()));
			BufferedImage image = new BufferedImage(iconSize, iconSize, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2d = (Graphics2D)image.getGraphics();
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			blueLocation.render(g2d, renderData.getRenderProperties(), true);
			ImageManager.addImage(RED_ATTACK_BLUE_WIN_ICON, new ImageIcon(image));
			
			//Create the Red attacked, Red won, batch plot outcome icon
			redBluePayoff.setWinner(PlayerType.Red);
			blueLocation.setDisplayMode(DisplayMode.StandardMode);
			blueLocation.setDisplayMode(DisplayMode.AttackOutcomeMode);
			image = new BufferedImage(iconSize, iconSize, BufferedImage.TYPE_INT_ARGB);
			g2d = (Graphics2D)image.getGraphics();
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			blueLocation.render(g2d, renderData.getRenderProperties(), true);
			ImageManager.addImage(RED_ATTACK_RED_WIN_ICON, new ImageIcon(image));
			
			
			//Create the Red did not attack batch plot outcome icon
			//TODO: May want to fill the not attack icons white
			blueLocation.setRedAction(RedActionType.Do_Not_Attack);
			blueLocation.setDisplayMode(DisplayMode.StandardMode);
			blueLocation.setDisplayMode(DisplayMode.AttackOutcomeMode);
			image = new BufferedImage(iconSize, iconSize, BufferedImage.TYPE_INT_ARGB);
			g2d = (Graphics2D)image.getGraphics();
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			blueLocation.render(g2d, renderData.getRenderProperties(), true);
			ImageManager.addImage(RED_NOT_ATTACK_ICON, new ImageIcon(image));			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JLabel label = new JLabel();
		//label.setOpaque(true);
		//label.setBackground(Color.red);
		label.setIcon(ImageManager_Phase2.getImageIcon(ImageManager_Phase2.RED_NOT_ATTACK_ICON));
		frame.getContentPane().add(label);
		frame.pack();
		frame.setVisible(true);
	}
	
	/**
	 * Create icons for the BLUEBOOK, Payoff Matrix and SIGINT reliabilities.
	 */
	private static void createCommandButtonIcons() {		
		try {
			//Create the BLUEBOOK icon
			ImageManager.addImage(BLUEBOOK_ICON, 
				new ImageIcon(ImageIO.read(ImageManager.class.getClassLoader().getResourceAsStream(
						"images/phase_2/bluebook_icon.png"))));
			
			//Create the Payoff matrix icon
			ImageManager.addImage(PAYOFF_MATRIX_ICON, 
					new ImageIcon(ImageIO.read(ImageManager.class.getClassLoader().getResourceAsStream(
							"images/phase_2/payoff_matrix_icon.png"))));
			
			//Create the SIGINT reliability icon
			ImageManager.addImage(SIGINT_RELIABILITY_ICON, 
					new ImageIcon(ImageIO.read(ImageManager.class.getClassLoader().getResourceAsStream(
							"images/phase_2/sigint_reliability_icon.png"))));
			
			//Create the batch plots icon
			ImageManager.addImage(BATCH_PLOTS_ICON, 
					new ImageIcon(ImageIO.read(ImageManager.class.getClassLoader().getResourceAsStream(
							"images/phase_2/batch_plots_icon.png"))));
			
			//Create the display previous outcome icon
			ImageManager.addImage(PREVIOUS_OUTCOME_ICON, 
					new ImageIcon(ImageIO.read(ImageManager.class.getClassLoader().getResourceAsStream(
							"images/phase_2/previous_outcome_icon.png"))));
			
			//Create the display next outcome icon
			ImageManager.addImage(NEXT_OUTCOME_ICON, 
					new ImageIcon(ImageIO.read(ImageManager.class.getClassLoader().getResourceAsStream(
							"images/phase_2/next_outcome_icon.png"))));
		} catch (Exception e) {			
			e.printStackTrace();
		}	
	}
}