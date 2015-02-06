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

import java.util.ArrayList;

import org.mitre.icarus.cps.feature_vector.phase_1.GridLocation2D;

/**
 * Given a Road, calculates 2 control points between each of its vertices.
 * http://www.codeproject.com/Articles/31859/Draw-a-Smooth-Curve-through-a-Set-of-2D-Points-wit
 * 
 * @author Lily Wong
 *
 */
public class BezierCurve {
	
	// calculation inputs
	/** The road vertices. */
	GridLocation2D[] knots;
	
	/** knots.size()-1 */
	int n; 
	
	// calculation outputs
	/** The first control points. */
	GridLocation2D[] firstControlPoints;
	
	/** The second control points. */
	GridLocation2D[] secondControlPoints;
	
	/** Average of first and second control points, for Path2D.quadTo()  */
	GridLocation2D[] midControlPoints;
	
	/**
	 * Instantiates a new bezier curve.
	 *
	 * @param road the road
	 */
	public BezierCurve(ArrayList<GridLocation2D> vertices) {
		n = vertices.size()-1;
		//System.out.println("n=" + n);
		this.knots = new GridLocation2D[n+1];
		vertices.toArray(this.knots);
		calcControlPoints();
	}
	
	/**
	 * Calculates control points.
	 */
	private void calcControlPoints() {
		if (n == 1)
		{ // Special case: Bezier curve should be a straight line.
			firstControlPoints = new GridLocation2D[1];
			// 3P1 = 2P0 + P3
			firstControlPoints[0].x = (2 * knots[0].x + knots[1].x) / 3;
			firstControlPoints[0].y = (2 * knots[0].y + knots[1].y) / 3;

			secondControlPoints = new GridLocation2D[1];
			// P2 = 2P1 - P0
			secondControlPoints[0].x = 2 *
				firstControlPoints[0].x - knots[0].x;
			secondControlPoints[0].y = 2 *
				firstControlPoints[0].y - knots[0].y;
			return;
		}
		
		// Calculate first Bezier control points
		// Right hand side vector
		double[] rhs = new double[n];
		// Set right hand side X values
		for (int i = 1; i < n - 1; ++i)
			rhs[i] = 4 * knots[i].x + 2 * knots[i + 1].x;
		
		rhs[0] = knots[0].x + 2 * knots[1].x;
		rhs[n - 1] = (8 * knots[n - 1].x + knots[n].x) / 2.0;

		// Get first control points X-values
		double[] x = getFirstControlPoints(rhs);
		
		// Set right hand side Y values
		for (int i = 1; i < n - 1; ++i)
			rhs[i] = 4 * knots[i].y + 2 * knots[i + 1].y;
		rhs[0] = knots[0].y + 2 * knots[1].y;
		rhs[n - 1] = (8 * knots[n - 1].y + knots[n].y) / 2.0;
		// Get first control points Y-values
		double[] y = getFirstControlPoints(rhs);
		
		// Fill output arrays.
		firstControlPoints = new GridLocation2D[n];
		secondControlPoints = new GridLocation2D[n];
		midControlPoints = new GridLocation2D[n];
		for (int i = 0; i < n; ++i)
		{
			// First control point
			firstControlPoints[i] = new GridLocation2D(x[i], y[i]);
			// Second control point
			if (i < n - 1)
				secondControlPoints[i] = new GridLocation2D(2 * knots
					[i + 1].x - x[i + 1], 2 *
					knots[i + 1].y - y[i + 1]);
			else
				secondControlPoints[i] = new GridLocation2D((knots
					[n].x + x[n - 1]) / 2,
					(knots[n].y + y[n - 1]) / 2);
			
			midControlPoints[i] = new GridLocation2D(
					(firstControlPoints[i].x.doubleValue()+secondControlPoints[i].x.doubleValue())/2,
					(firstControlPoints[i].y.doubleValue()+secondControlPoints[i].y.doubleValue())/2);
		}
	}
	
	/**
	 * Calculates the first control points.
	 *
	 * @param rhs the rhs
	 * @return the first control points
	 */
	private static double[] getFirstControlPoints(double[] rhs)
	{
		int n = rhs.length;
		double[] x = new double[n]; // Solution vector.
		double[] tmp = new double[n]; // Temp workspace.

		double b = 2.0;
		x[0] = rhs[0] / b;
		for (int i = 1; i < n; i++) // Decomposition and forward substitution.
		{
			tmp[i] = 1 / b;
			b = (i < n - 1 ? 4.0 : 3.5) - tmp[i];
			x[i] = (rhs[i] - x[i - 1]) / b;
		}
		for (int i = 1; i < n; i++)
			x[n - i - 1] -= tmp[n - i] * x[n - i]; // Backsubstitution.

		return x;
	}

	/**
	 * Gets the first control points.
	 *
	 * @return the first control points
	 */
	public GridLocation2D[] getFirstControlPoints() {
		return firstControlPoints;
	}

	/**
	 * Gets the second control points.
	 *
	 * @return the second control points
	 */
	public GridLocation2D[] getSecondControlPoints() {
		return secondControlPoints;
	}

	/**
	 * Gets the mid control points.
	 *
	 * @return the mid control points
	 */
	public GridLocation2D[] getMidControlPoints() {
		return midControlPoints;
	}
}