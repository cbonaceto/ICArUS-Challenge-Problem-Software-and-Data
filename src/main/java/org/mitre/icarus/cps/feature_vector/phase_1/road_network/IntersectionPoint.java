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
package org.mitre.icarus.cps.feature_vector.phase_1.road_network;

import org.mitre.icarus.cps.feature_vector.phase_1.GridLocation2D;
import org.mitre.icarus.cps.feature_vector.phase_1.GridSize;

/**
 * Represents an intersection point.
 * @author Lily Wong
 *
 */
public class IntersectionPoint extends GridLocation2D {
	
	/** The road a. */
	private String roadA;
	
	/** The road b. */
	private String roadB;
	
	/**
	 * Instantiates a new intersection point.
	 *
	 * @param roadA the road a
	 * @param roadB the road b
	 * @param x the x
	 * @param y the y
	 */
	public IntersectionPoint(String roadA, String roadB, double x, double y, GridSize gridSize) {
		this.roadA = roadA;
		this.roadB = roadB;
		this.x = x;
		this.y = y;
		this.gridSize = gridSize;
	}
	
	public IntersectionPoint(GridLocation2D pt) {
		this.roadA = pt.getLocationId();
		this.roadB = pt.getLocationId();
		this.x = pt.x;
		this.y = pt.y;
		this.gridSize = pt.gridSize;
	}
	
	public void swapEndPointLabels() {
		String temp = roadA;
		roadA = roadB;
		roadB = temp;
	}
	
	/**
	 * Gets the road a.
	 *
	 * @return the road a
	 */
	public String getRoadA() { return roadA; }
	
	/**
	 * Gets the road b.
	 *
	 * @return the road b
	 */
	public String getRoadB() { return roadB; }
	
	public GridLocation2D toGridLocation2D() {
		return new GridLocation2D(null,null,x,y,gridSize);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "IntersectionPoint [roadA=" + roadA + ", roadB=" + roadB
				+ ", x=" + x + ", y=" + y + "]";
	}

	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		if ( obj instanceof IntersectionPoint ) {
			IntersectionPoint o = (IntersectionPoint) obj;
			result = (this.x.doubleValue() == o.x.doubleValue() && this.y.doubleValue() == o.y.doubleValue())
					&& ( this.getRoadA().equals(o.getRoadA()) && this.getRoadB().equals(o.getRoadB()));
		}
		else if (obj instanceof GridLocation2D) {
			GridLocation2D o = (GridLocation2D) obj;
			result = (this.x.doubleValue() == o.x.doubleValue() && this.y.doubleValue() == o.y.doubleValue());
		} 
		return result;
	}

	@Override
	public int hashCode() {
//		int stringHash = (41 * (41 + roadA.hashCode()) +  roadB.hashCode());
//		return super.hashCode() * stringHash;
		return super.hashCode();
	}
}