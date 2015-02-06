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
package org.mitre.icarus.cps.feature_vector.phase_1;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

import org.mitre.icarus.cps.app.widgets.phase_1.ColorManager_Phase1;

/**
 * Defines all SOCINT regions by defining the group for each grid square. This is the
 * format that is currently used to represent SOCINT regions.
 * 
 * @author CBONACETO
 *
 */
public class SocintOverlay {
	
	/** The socint pts. */
	EnumMap<GroupType, ArrayList<GridLocation2D>> socintPts;
	
	public SocintOverlay() {
		socintPts = new EnumMap<GroupType, ArrayList<GridLocation2D>>(GroupType.class);	
	}
	
	public Collection<GroupType> getGroups() {
		if(socintPts != null) {
			return socintPts.keySet();
		}
		return null;
	}

	public EnumMap<GroupType, ArrayList<GridLocation2D>> getSocintPts() {
		return socintPts;
	}

	public void setSocintPts(EnumMap<GroupType, ArrayList<GridLocation2D>> socintPts) {
		this.socintPts = socintPts;
	}

	public Image createSocintOverlayImage(GridSize gridSize) {
		BufferedImage image = new BufferedImage(gridSize.gridWidth, gridSize.gridHeight, BufferedImage.TYPE_INT_ARGB);
		if(socintPts != null && !socintPts.isEmpty()) {
			for(Map.Entry<GroupType, ArrayList<GridLocation2D>> socintEntry : socintPts.entrySet()) {
				int color = ColorManager_Phase1.getGroupRegionColor(socintEntry.getKey()).getRGB();
				if(socintEntry.getValue() != null) {
					for(GridLocation2D point : socintEntry.getValue()) {
						//image.setRGB(point.x.intValue()-1, gridSize.gridHeight - point.y.intValue(), color);
						int x = point.x.intValue();
						if(x < 0) { x = 0; }
						else if(x >= gridSize.gridWidth) { x = gridSize.gridWidth - 1;}
						int y = gridSize.gridHeight - point.y.intValue() - 1;
						if(y < 0) { y = 0; }
						else if(y >= gridSize.gridHeight) { y = gridSize.gridHeight - 1; }
						image.setRGB(x, y, color);
					}
				}
			}
		}		
		return image;
	}
}