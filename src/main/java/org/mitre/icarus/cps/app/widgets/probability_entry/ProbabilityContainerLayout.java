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
package org.mitre.icarus.cps.app.widgets.probability_entry;

import java.awt.GridBagConstraints;

/**
 * @author CBONACETO
 *
 */
public class ProbabilityContainerLayout {
	
	/** Layout types */
	public static enum ProbabilityContainerLayoutType {CROSS, HORIZONTAL, VERTICAL, GRID};
	
	/** Orientation constants */
	public static final int NORTH = 0; //North orientation
	public static final int SOUTH = 1; //South orientation
	public static final int EAST = 2; //East orientation
	public static final int WEST = 3; //West orientation
	public static final int CENTER = 4; //Center orientation
	
	protected final ProbabilityContainerLayoutType layoutType;
	
	protected final int numGridColumns;
	
	public ProbabilityContainerLayout(ProbabilityContainerLayoutType layoutType, int numGridColumns) {
		this.layoutType = layoutType;
		this.numGridColumns = numGridColumns;
	}

	public ProbabilityContainerLayoutType getLayoutType() {
		return layoutType;
	}

	public int getNumGridColumns() {
		return numGridColumns;
	}
	
	/**
	 * @param gbc
	 * @param componentIndex
	 * @param numComponents
	 * @return
	 */
	public GridBagConstraints updateConstraints(int componentIndex, int numComponents,
			 int verticalSpacing, int horizontalSpacing, GridBagConstraints gbc) {
		//TODO: May also want to set the insets
		if(gbc == null) {gbc = new GridBagConstraints();}
		switch(layoutType) {
		case CROSS:
			gbc.insets.left = 0; gbc.insets.right = 0;
			gbc.insets.top = 0; gbc.insets.bottom = 0;
			switch(componentIndex) {
			case NORTH:
				//North
				gbc.gridx = 1;
				gbc.gridy = 0;	
				gbc.anchor = GridBagConstraints.SOUTH;
				gbc.insets.bottom = verticalSpacing/2;
				break;
			case SOUTH:
				//South
				gbc.gridx = 1;
				gbc.gridy = 2;	
				gbc.anchor = GridBagConstraints.NORTH;
				gbc.insets.top = verticalSpacing/2;
				break;
			case EAST:
				//East
				gbc.gridx = 2;
				gbc.gridy = 1;
				gbc.anchor = GridBagConstraints.WEST;
				gbc.insets.left = horizontalSpacing;
				break;
			case WEST:
				//West
				gbc.gridx = 0;
				gbc.gridy = 1;
				gbc.anchor = GridBagConstraints.EAST;
				gbc.insets.right = horizontalSpacing;
				break;
			}
			break;
		case GRID:
			int numRows = Math.round(numComponents / (float)numGridColumns);
			gbc.gridy = componentIndex / numGridColumns;
			gbc.gridx = componentIndex - (gbc.gridy * numGridColumns);
			gbc.weightx = componentIndex == numComponents - 1 || gbc.gridx == numGridColumns - 1 ? 1 : 0;
			gbc.weighty = gbc.gridy == numRows - 1 ? 1 : 0;
			gbc.insets.top = gbc.gridy == 0 ? 0 : verticalSpacing;
			gbc.insets.bottom = 0;
			gbc.insets.left = gbc.gridx == 0 ? 0 : horizontalSpacing;
			gbc.insets.right = 0;
			break;
		case HORIZONTAL:
			gbc.gridx = componentIndex;
			gbc.gridy = 0;
			gbc.weightx = componentIndex == numComponents - 1 ? 1 : 0;
			gbc.insets.left = componentIndex == 0 ? 0 : horizontalSpacing;
			gbc.insets.right = 0;
			/*if(x > 0) {
						constraints.insets.left = 10;
					}*/
			break;
		case VERTICAL:
			gbc.gridx = 0;
			gbc.gridy = componentIndex;
			gbc.weighty = componentIndex == numComponents - 1 ? 1 : 0;
			gbc.insets.top = componentIndex == 0 ? 0 : verticalSpacing;
			gbc.insets.bottom = 0;
			break;
		default:
			break;
		}
		return gbc;
	}
}