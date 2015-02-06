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
package org.mitre.icarus.cps.feature_vector.phase_1.parser;

import java.util.ArrayList;

import org.mitre.icarus.cps.feature_vector.phase_1.GridLocation2D;

/**
 * Functions for finding a shortest path.
 * 
 * @author Lily Wong
 *
 */
public class SortingUtils {

	public static double getDist(GridLocation2D a, GridLocation2D b) {
		return Math.sqrt( Math.pow(a.getX() - b.getX(),2) + Math.pow(a.getY() - b.getY(), 2));
	}

	public static double dist(ArrayList<GridLocation2D> a, int i)  {
		return getDist(a.get(i), a.get(i-1));
	}

	protected static final double square(double d) {
		return d * d;
	}
	
	public static void swap(ArrayList<GridLocation2D> a, int i, int n) {
		GridLocation2D temp = a.get(i);
		a.set(i, a.get(n));
		a.set(n, temp);
	}
	
	private static int findClosest(ArrayList<GridLocation2D> a, int n) {
		double minDist = Double.MAX_VALUE;
		int minPoint = n;
		for(int i = n; i < a.size(); i++) {
			double tempDist = getDist(a.get(n-1),a.get(i));
			if(tempDist < minDist) {
				minDist = tempDist;
				minPoint = i;
			}
		}
		return minPoint;
	}
	
	// swaps array in place
	public static void sortShortestPath(ArrayList<GridLocation2D> a) {
		for(int i = 1; i < a.size(); i++) {
			swap(a,i,findClosest(a,i));
		}
	}
}