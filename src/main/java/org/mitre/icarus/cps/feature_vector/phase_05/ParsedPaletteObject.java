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
package org.mitre.icarus.cps.feature_vector.phase_05;

import java.util.List;

/**
 * Defines an object from an object palette.
 * 
 * @author Jing Hu
 *
 */
public class ParsedPaletteObject {
	
	private int[][] layout;
	
	private int width;
	private int height;
	
	public ParsedPaletteObject(List<int[]> layoutList) {
		layout = new int[layoutList.size()][];

		// find height and width
		height = 0;
		width = 0;
		for (int i = 0; i < layoutList.size(); i++) {
			layout[i] = layoutList.get(i);
			
			if (width < layout[i][0]) {
				width = layout[i][0];
			}
			if (height < layout[i][1]) {
				height = layout[i][1];
			}
		}
		
		// invert y
		for (int i = 0; i < layout.length; i++) {
			layout[i][1] = height - layout[i][1];
			layout[i][0]--;
		}
	}
	
	public int[][] getLayout() {
		return layout;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
}
