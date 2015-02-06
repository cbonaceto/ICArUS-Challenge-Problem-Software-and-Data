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
package org.mitre.icarus.cps.app.widgets;

import java.awt.Image;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * Manages application images.
 * 
 * @author CBONACETO
 *
 */
public class ImageManager {
	
	/** Icon sizes */
	public static enum IconSize {Large, Small};	
		
	/** Icon Types */
	public static final String ICARUS_LOGO = "ICARUS_LOGO";
	public static final String MITRE_LOGO = "MITRE_LOGO";
	public static final String BACK_ICON = "BACK";
	public static final String BACK_DISABLED_ICON = "BACK_DISABLED";
	public static final String NEXT_ICON = "NEXT";
	public static final String NEXT_DISABLED_ICON = "NEXT_DISABLED";
	public static final String EXIT_ICON = "EXIT";
	public static final String HELP_ICON = "HELP";
	public static final String HOME_ICON = "HOME";
	public static final String INFORMATION_ICON = "INFORMATION";
	public static final String LOGIN_ICON = "LOGIN";
	public static final String LOGOUT_ICON = "LOGOUT";
	public static final String PLUS_ICON = "PLUS";
	public static final String MINUS_ICON = "MINUS";
	public static final String CONTROL_FAST_FORWARD_ICON = "FAST_FORWARD";
	public static final String CONTROL_PAUSE_ICON = "PAUSE";
	public static final String CONTROL_PLAY_ICON = "PLAY";
	public static final String CONTROL_REWIND_ICON = "REWIND";
	public static final String CONTROL_START_ICON = "START";
	public static final String CONTROL_STOP_ICON = "STOP";
	public static final String LOCKED_ICON = "LOCKED";
	public static final String UNLOCKED_ICON = "UNLOCKED";	
	protected static final Set<String> reservedNames = new HashSet<String>();
	static {
		reservedNames.add(ICARUS_LOGO);
		reservedNames.add(MITRE_LOGO);	
		reservedNames.add(BACK_ICON);
		reservedNames.add(BACK_DISABLED_ICON);
		reservedNames.add(NEXT_ICON);
		reservedNames.add(EXIT_ICON);
		reservedNames.add(HELP_ICON);
		reservedNames.add(HOME_ICON);
		reservedNames.add(INFORMATION_ICON);
		reservedNames.add(LOGIN_ICON);
		reservedNames.add(LOGOUT_ICON);
		reservedNames.add(PLUS_ICON);
		reservedNames.add(MINUS_ICON);
		reservedNames.add(CONTROL_FAST_FORWARD_ICON);
		reservedNames.add(CONTROL_PAUSE_ICON);
		reservedNames.add(CONTROL_PLAY_ICON);
		reservedNames.add(CONTROL_REWIND_ICON);
		reservedNames.add(CONTROL_START_ICON);
		reservedNames.add(CONTROL_STOP_ICON);
		reservedNames.add(LOCKED_ICON);
		reservedNames.add(UNLOCKED_ICON);
	}
	/*****/
	
	/** Icon Images */
	private HashMap<String, ImageIcon> images;	
	
	/** Singleton instance of the Image Manager */
	private static final ImageManager imageManager = new ImageManager();
	
	private ImageManager() {
		images = new HashMap<String, ImageIcon>();
		createIcons();
	}
	
	/**
	 * Get the singleton instance of the image manager.
	 */
	public static ImageManager getImageManager() {
		return imageManager;
	}
	
	/**
	 * Get the image icon for the given imageType.
	 */
	public static ImageIcon getImageIcon(String imageType) {
		return imageManager.images.get(imageType);
	}
	
	/**
	 * Get the image icon for the given imageType.
	 */
	public static Image getImage(String imageType) {
		ImageIcon img = imageManager.images.get(imageType);
		if(img != null) {
			return img.getImage();
		}
		return null;
	}	
	
	public static void addImage(String imageType, ImageIcon image) {
		if(reservedNames.contains(imageType)) {
			throw new IllegalArgumentException("Cannot image with name " + imageType + ". Image already exists.");
		}
		imageManager.images.put(imageType, image);
	}
	
	/** Get the names of each image type */
	public String[] getImageTypes() {
		String[] arr = new String[reservedNames.size()];
		return reservedNames.toArray(arr);
	}		
	
	/** Creates the icons */
	private void createIcons() {		
		try {
			images.put(ICARUS_LOGO, new ImageIcon(
					ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/icarus_icon.png"))));
			images.put(MITRE_LOGO, new ImageIcon(
					ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/mitrelogo-blue.jpg"))));
			images.put(BACK_ICON, new ImageIcon(
					ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/back_arrow.png"))));
			images.put(BACK_DISABLED_ICON, new ImageIcon(
					ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/back_arrow_disabled.png"))));
			images.put(NEXT_ICON, new ImageIcon(
					ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/next_arrow.png"))));
			images.put(NEXT_DISABLED_ICON, new ImageIcon(
					ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/next_arrow_disabled.png"))));
			images.put(MINUS_ICON, new ImageIcon(
					ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/bullet_toggle_minus.png"))));
			images.put(PLUS_ICON, new ImageIcon(
					ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/bullet_toggle_plus.png"))));
			images.put(CONTROL_FAST_FORWARD_ICON, new ImageIcon( 
					ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/control_fastforward_blue.png"))));
			images.put(CONTROL_PAUSE_ICON, new ImageIcon( 
					ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/control_pause_blue.png"))));
			images.put(CONTROL_PLAY_ICON, new ImageIcon( 
					ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/control_play_blue.png"))));
			images.put(CONTROL_REWIND_ICON, new ImageIcon( 
					ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/control_rewind_blue.png"))));
			images.put(CONTROL_START_ICON, new ImageIcon( 
					ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/control_start_blue.png"))));
			images.put(CONTROL_STOP_ICON, new ImageIcon( 
					ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/control_stop_blue.png"))));
			images.put(EXIT_ICON, new ImageIcon( 
					ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/exit.png"))));
			images.put(LOGIN_ICON, new ImageIcon( 
					ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/log_in.png"))));
			images.put(LOGOUT_ICON, new ImageIcon( 
					ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/log_out.png"))));
			images.put(LOCKED_ICON, new ImageIcon( 
					ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/locked_icon.png"))));
			images.put(UNLOCKED_ICON, new ImageIcon( 
					ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/unlocked_icon.png"))));
			images.put(HELP_ICON, new ImageIcon( 
					ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/help.png"))));
			images.put(HOME_ICON, new ImageIcon( 
					ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/home.png"))));
			images.put(INFORMATION_ICON, new ImageIcon( 
					ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/information.png"))));
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
}