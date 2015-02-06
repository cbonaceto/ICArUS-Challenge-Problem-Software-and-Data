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
package org.mitre.icarus.cps.app.widgets.phase_1;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.mitre.icarus.cps.feature_vector.phase_1.GroupType;

/**
 * Manages colors for the Phase 1 CPD.
 * 
 * @author CBONACETO
 *
 */
public class ColorManager_Phase1 {
	
	/** Singleton instance of the ColorManager */
	private static final ColorManager_Phase1 colorManager = new ColorManager_Phase1();
	
	/** Background color string constant */
	public static final String BACKGROUND = "background";
	
	/** Contains group center colors for each group */	
	private EnumMap<GroupType, Color> groupCenterColors;
	
	/** Contains group region colors for each group */	
	private EnumMap<GroupType, Color> groupRegionColors;
	
	/** Contains other application colors for the string constants defined above */
	private Map<String, Color> colors;
	
	private ColorManager_Phase1() {
		groupCenterColors = new EnumMap<GroupType, Color>(GroupType.class);
		groupCenterColors.put(GroupType.X, Color.black);
		groupCenterColors.put(GroupType.O, Color.black);
		groupCenterColors.put(GroupType.Unknown, Color.black);
		groupCenterColors.put(GroupType.A, new Color(0, 176, 172));
		//groupCenterColors.put(GroupType.A, new Color(0, 137, 134));		
		groupCenterColors.put(GroupType.B, new Color(187, 9, 9));		
		groupCenterColors.put(GroupType.C, new Color(184, 136, 0));
		//groupCenterColors.put(GroupType.D, new Color(13, 13, 13));
		groupCenterColors.put(GroupType.D, new Color(125, 125, 125));
		//groupCenterColors.put(GroupType.D, Color.black);
		
		groupRegionColors = new EnumMap<GroupType, Color>(GroupType.class);
		groupRegionColors.put(GroupType.A, new Color(155, 255, 253));
		//groupRegionColors.put(GroupType.A, new Color(85, 233, 233));
		groupRegionColors.put(GroupType.B, new Color(250, 160, 160));
		//groupRegionColors.put(GroupType.B, new Color(251, 187, 187));
		groupRegionColors.put(GroupType.C, new Color(255, 220, 121));
		//groupRegionColors.put(GroupType.C, new Color(255, 221, 125));
		groupRegionColors.put(GroupType.D, new Color(176, 176, 176));
		//groupRegionColors.put(GroupType.D, new Color(217, 217, 217));
		
		colors = new HashMap<String, Color>(1);
		colors.put(BACKGROUND, Color.WHITE);		
	}
	
	public static ColorManager_Phase1 getColorManager() {
		return colorManager;
	}
	
	public static Color getGroupCenterColor(GroupType group) {
		if(group == null) {
			return Color.black;
		}
		return colorManager.groupCenterColors.get(group);
	}
	
	public static void setGroupCenterColor(GroupType group, Color color) {
		colorManager.groupCenterColors.put(group, color);
	}
	
	public static Color getGroupRegionColor(GroupType group) {
		if(group == null) {
			return Color.black;
		}
		return colorManager.groupRegionColors.get(group);
	}
	
	public static void setGroupRegionColor(GroupType group, Color color) {
		colorManager.groupRegionColors.put(group, color);
	}	
	
	public static Color getColor(String key) {
		return colorManager.colors.get(key);
	}

	public static void setColor(String key, Color color) {
		colorManager.colors.put(key, color);
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
