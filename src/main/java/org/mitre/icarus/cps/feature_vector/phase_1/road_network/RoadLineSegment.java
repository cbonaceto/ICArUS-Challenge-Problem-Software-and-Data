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
import org.mitre.icarus.cps.feature_vector.phase_1.parser.SortingUtils;

/**
 * Represents a line segment of a road.
 * 
 * @author Lily Wong
 *
 */
public class RoadLineSegment {

	/** The road id. */
	protected String roadId;
	
	/** The point a. */
	protected GridLocation2D pointA;
	
	/** The point b. */
	protected GridLocation2D pointB;
	
	protected GridSize gridSize;
	
	/** The length of the line segment in XY grid units. */
	private double lengthInUnits;
	
	/** The length of the line segment in miles. */
	private double lengthInMiles;
	
	/** properties used by algorithm and display */
	private Double slope;
	private Double intercept;
	private boolean isVertical;
	
	/**
	 * Instantiates a new road line segment.
	 *
	 * @param roadId the road id
	 * @param pointA the point a
	 * @param pointB the point b
	 */
	public RoadLineSegment(String roadId, GridLocation2D pointA, GridLocation2D pointB, GridSize gridSize) {
		this.roadId = roadId;
		this.pointA = pointA;
		this.pointB = pointB;
		this.gridSize = gridSize;
		if(pointA != null && pointB != null) { //if check added by Craig
			this.lengthInUnits = SortingUtils.getDist(pointA, pointB);
			if(gridSize != null) {
				this.lengthInMiles = this.lengthInUnits * gridSize.getMilesPerGridUnit();	
			}
			
			// set line segment calculations
			if (pointA.getX().equals(pointB.getX())) {
				this.slope = null;
				this.intercept = null;
				isVertical = true;
			} else {
				this.slope = (pointB.getY()-pointA.getY()) / (pointB.getX()-pointA.getX());
				this.intercept = pointA.getY() - (this.slope * pointA.getX());
				isVertical = false;
			}
			
		} else {
			System.err.println("Warning in RoadLineSegmant constructor: pointA or pointB is null");
		}
	}

	public double getSlope() {
		return slope.doubleValue();
	}
	
	public double getIntercept() {
		return intercept.doubleValue();
	}
	
	public boolean isVertical() {
		return isVertical;
	}
	
	public double yAt(int x) {
		return (this.slope*x) + this.intercept;
	}
	
	/**
	 * Gets the road id.
	 *
	 * @return the road id
	 */
	public String getRoadId() {
		return roadId;
	}
	
	/**
	 * Gets the point a.
	 *
	 * @return the point a
	 */
	public GridLocation2D getPointA() {
		return pointA;
	}

	/**
	 * Gets the point b.
	 *
	 * @return the point b
	 */
	public GridLocation2D getPointB() {
		return pointB;
	}

	/**
	 * Gets the length.
	 *
	 * @return the length
	 */
	public double getLengthInUnits() {
		return lengthInUnits;
	}

	/**
	 * Sets the length in units, also setting the length in miles.
	 *
	 * @param length the new length in units
	 */
	public void setLengthInUnits(double length) {
		this.lengthInUnits = length;
		if(gridSize != null) {
			this.lengthInMiles = this.lengthInUnits * gridSize.getMilesPerGridUnit();
		}
	}

	/**
	 * Gets the length in miles.
	 *
	 * @return the length in miles
	 */
	public double getLengthInMiles() {
		return lengthInMiles;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RoadLineSegment [roadId=" + roadId + ", pointA=" + pointA
				+ ", pointB=" + pointB + ", length=" + lengthInUnits + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pointA == null) ? 0 : pointA.hashCode());
		result = prime * result + ((pointB == null) ? 0 : pointB.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RoadLineSegment other = (RoadLineSegment) obj;
		if (pointA == null) {
			if (other.pointA != null)
				return false;
		} else if (!pointA.equals(other.pointA))
			return false;
		if (pointB == null) {
			if (other.pointB != null)
				return false;
		} else if (!pointB.equals(other.pointB))
			return false;
		return true;
	}
}