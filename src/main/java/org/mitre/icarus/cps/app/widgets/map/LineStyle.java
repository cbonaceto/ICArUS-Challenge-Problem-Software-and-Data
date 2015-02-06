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
package org.mitre.icarus.cps.app.widgets.map;

import java.awt.BasicStroke;

/**
 * @author CBONACETO
 *
 */
public class LineStyle {
	
	public static final int CAP_BUTT = BasicStroke.CAP_BUTT;
	
	public static final int CAP_ROUND = BasicStroke.CAP_ROUND;
	
	public static final int CAP_SQUARE = BasicStroke.CAP_SQUARE;
	
	public static final int JOIN_BEVEL = BasicStroke.JOIN_BEVEL;
	
	public static final int JOIN_MITER = BasicStroke.JOIN_MITER;
	
	public static final int JOIN_ROUND = BasicStroke.JOIN_ROUND;
	
	float[] dashArray;
	
	float dashPhase;
	
	int endCap = BasicStroke.CAP_SQUARE;
	
	int lineJoin = BasicStroke.JOIN_MITER;
	
	float lineWidth = 1.f;
	
	float miterLimit = 10.f;
	
	public LineStyle() {}
	
	public LineStyle(float lineWidth) {
		this.lineWidth = lineWidth;
	}
	
	public LineStyle(float lineWidth, int cap, int join) {
		this.lineWidth = lineWidth;
		this.endCap = cap;
		this.lineJoin = join;
	}	
	
	public LineStyle(float lineWidth, int cap, int join, float miterLimit) {
		this.lineWidth = lineWidth;
		this.endCap = cap;
		this.lineJoin = join;
		this.miterLimit = miterLimit;
	}

	public LineStyle(float lineWidth, int cap, int join, float miterLimit, float[] dash, float dashPhase) {
		this.lineWidth = lineWidth;
		this.endCap = cap;
		this.lineJoin = join;
		this.miterLimit = miterLimit;
		this.dashArray = dash;
		this.dashPhase = dashPhase;
	}
	
	public float[] getDashArray() {
		return dashArray;
	}

	public void setDashArray(float[] dashArray) {
		this.dashArray = dashArray;
	}

	public float getDashPhase() {
		return dashPhase;
	}

	public void setDashPhase(float dashPhase) {
		this.dashPhase = dashPhase;
	}

	public int getEndCap() {
		return endCap;
	}

	public void setEndCap(int endCap) {
		this.endCap = endCap;
	}

	public int getLineJoin() {
		return lineJoin;
	}

	public void setLineJoin(int lineJoin) {
		this.lineJoin = lineJoin;
	}

	public float getLineWidth() {
		return lineWidth;
	}

	public void setLineWidth(float lineWidth) {
		this.lineWidth = lineWidth;
	}

	public float getMiterLimit() {
		return miterLimit;
	}

	public void setMiterLimit(float miterLimit) {
		this.miterLimit = miterLimit;
	}	
	
	public BasicStroke getLineStroke() {
		if(dashArray != null) {
			return new BasicStroke(lineWidth, endCap, lineJoin, miterLimit, dashArray, dashPhase);	
		}
		else {
			return new BasicStroke(lineWidth, endCap, lineJoin, miterLimit);
		}		
	}
}
