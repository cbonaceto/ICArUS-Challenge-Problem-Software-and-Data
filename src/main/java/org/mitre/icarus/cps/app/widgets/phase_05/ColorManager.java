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
package org.mitre.icarus.cps.app.widgets.phase_05;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.*;

/**
 * Manages application colors.
 * 
 * @author CBONACETO
 *
 */
public class ColorManager {
	
	private static ColorManager colorManager = new ColorManager();
	public static final String BACKGROUND = "Background";
	public static final String SECTOR = "Sector";
	public static final String MASINT1 = "MASINT1";
	public static final String MASINT2 = "MASINT2";
	public static final String SIGINT = "SIGINT";
	public static final String BUILDING = "Building";
	public static final String ROOFTOP_HARDWARE = "Rooftop_Hardware";
	public static final String WATER = "Water";
	
	private Map<String, Color> colors;
	
	private ColorManager() {
		colors = new HashMap<String, Color>();
		colors.put(BACKGROUND, Color.WHITE);
		colors.put(SECTOR, Color.BLACK);
		colors.put(MASINT1, Color.RED);
		colors.put(MASINT2, Color.GREEN);
		colors.put(SIGINT, Color.MAGENTA);
		colors.put(BUILDING, new Color(140, 140, 140));
		colors.put(ROOFTOP_HARDWARE, Color.RED);
		colors.put(WATER, Color.CYAN);
	}
	
	public static ColorManager getColorManager() {
		return colorManager;
	}
	
	public static Color get(String key) {
		return getColorManager().colors.get(key);
	}

	public static void set(String key, Color color) {
		getColorManager().colors.put(key, color);
	}
	
	public JDialog createColorDialog() {
		final JDialog dialog = new JDialog();
		
		dialog.setLayout(new GridLayout(colors.size(), 2));
		for (final Entry<String, Color> entry : colors.entrySet()) {
			dialog.add(new JLabel(entry.getKey()));
			final JPanel panel = new JPanel();
			panel.setBackground(entry.getValue());
			panel.setOpaque(true);
			
			panel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					System.out.println("click");
					Color color = JColorChooser.showDialog(dialog, entry.getKey(), entry.getValue());
					
					if (color != null) {
						entry.setValue(color);
						panel.setBackground(color);
					}
				}
			});
			
			dialog.add(panel);
		}
		
		dialog.pack();
		
		return dialog;
		
	}
	
	public static void main(String[] args) {
		JDialog dialog = getColorManager().createColorDialog();
		dialog.setVisible(true);
	}
}