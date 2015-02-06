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
package org.mitre.icarus.cps.app.widgets.map.phase_1.objects;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.mitre.icarus.cps.app.widgets.map.LineStyle;
import org.mitre.icarus.cps.app.widgets.map.phase_1.MapConstants_Phase1;
import org.mitre.icarus.cps.app.widgets.map.phase_1.RenderProperties;
import org.mitre.icarus.cps.feature_vector.phase_1.GridLocation2D;
import org.mitre.icarus.cps.feature_vector.phase_1.Road;
import org.mitre.icarus.cps.feature_vector.phase_1.road_network.BezierCurve;

/**
 * @author CBONACETO
 *
 */
public class RoadMapObject extends AbstractMapObject_Phase1 {
	
	/** The road */
	protected final Road road;
	
	/** The road color */
	protected Color roadColor = MapConstants_Phase1.ROAD_COLOR;
	
	/** The road line style */
	protected LineStyle roadLineStyle;	
	
	/** The default road stroke to use  */
	protected static final BasicStroke defaultStroke = new BasicStroke(MapConstants_Phase1.ROAD_STROKE_WIDTH, 
			BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	
	protected Path2D roadPath;	
	
	public RoadMapObject(Road road) {
		this.road = road;
		roadPath = new Path2D.Double();
		objectBoundsChanged = true;
	}
	
	@Override
	public String getName() {
		if(road != null) {
			return road.getId();
		}
		return null;
	}

	@Override
	public String getId() {
		if(road != null) {
			return road.getId();
		}
		return null;
	}	

	public Road getRoad() {
		return road;
	}
	
	public void roadChanged() {
		objectBoundsChanged = true;
	}

	/**
	 * @return the roadColor
	 */
	public Color getRoadColor() {
		return roadColor;
	}

	/**
	 * @param roadColor the roadColor to set
	 */
	public void setRoadColor(Color roadColor) {
		this.roadColor = roadColor;
	}

	public LineStyle getRoadLineStyle() {
		return roadLineStyle;
	}

	public void setRoadLineStyle(LineStyle roadLineStyle) {
		this.roadLineStyle = roadLineStyle;
	}

	/**
	 * @return the renderShape
	 */
	public Shape getRenderShape() {
		return roadPath;
	}	

	@Override
	public boolean contains(Point point) {
		return contains(point.x, point.y);
	}

	@Override
	public boolean contains(Point2D point) {
		return contains(point.getX(), point.getY());
	}

	@Override
	public boolean contains(double x, double y) {
		if(roadPath != null) {
			return roadPath.contains(x, y);
		}
		return false;
	}

	@Override
	public void render(Graphics2D g2d, RenderProperties renderData, boolean renderPropertiesChanged) {		
		Stroke origStroke = g2d.getStroke();
		Color origColor = g2d.getColor();
		
		if(objectBoundsChanged || renderPropertiesChanged || roadPath.getCurrentPoint() == null) {
			roadPath.reset();	
			
			ArrayList<GridLocation2D> vertices = road.getVertices();
			GridLocation2D[] firstCtrlPts = null;
			GridLocation2D[] secondCtrlPts = null;
			int pointCounter = 0;
			
			if(MapConstants_Phase1.PRUNE_ROADS && road.getSmoothPoints() != null && !road.getSmoothPoints().isEmpty()) {
				vertices = road.getSmoothPoints();
			}
			if(vertices == null || vertices.isEmpty()) {
				return;
			}
			if(MapConstants_Phase1.SMOOTH_ROADS) {
				BezierCurve curve = new BezierCurve(vertices);
				firstCtrlPts = curve.getFirstControlPoints();
				secondCtrlPts = curve.getSecondControlPoints();
				//GridLocation2D[] midCtrlPts = curve.getMidControlPoints();
			}			
			for(GridLocation2D vertex : vertices) {
				Point2D vertexPoint = renderData.translateToPixel(vertex);			
				if(pointCounter == 0) {
					//Go to the first road point
					roadPath.moveTo(vertexPoint.getX(), vertexPoint.getY());
				}
				else {					
					if (MapConstants_Phase1.SMOOTH_ROADS) { // curve from point to point
						if ( pointCounter < road.getVertices().size() ) {
							// Bezier Curve: 2 control points
							Point2D firstCtrlPt = renderData.translateToPixel(firstCtrlPts[pointCounter-1]);		
							Point2D secondCtrlPt = renderData.translateToPixel(secondCtrlPts[pointCounter-1]);		
							roadPath.curveTo(   firstCtrlPt.getX(), firstCtrlPt.getY(),
												secondCtrlPt.getX(), secondCtrlPt.getY(),
												vertexPoint.getX(), vertexPoint.getY());
							
							// QuadCurve: use 1 control point
							//Point2D midCtrlPt = renderData.translateToPixel(midCtrlPts[pointCounter]);
							//roadPath.quadTo(midCtrlPt.getX(), midCtrlPt.getY(), vertexPoint.getX(), vertexPoint.getY());
						}
					}
					else {// straight line from point to point
						roadPath.lineTo(vertexPoint.getX(), vertexPoint.getY());
					}
				}
				pointCounter++;
			}
		}
		
		//Set the alpha transparency
		Composite origComposite = null;
		if(transparency < 1.f) {
			origComposite = g2d.getComposite();
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency));
		}
		
		//Render the road path object
		g2d.setColor(roadColor);
		if(roadLineStyle != null) {
			g2d.setStroke(roadLineStyle.getLineStroke());
		} else {
			g2d.setStroke(defaultStroke); 
		}
		g2d.draw(roadPath);		
		
		g2d.setStroke(origStroke);
		g2d.setColor(origColor);
		if(origComposite != null) {
			g2d.setComposite(origComposite);
		}
		objectBoundsChanged = false;
	}

	@Override
	public void setMouseOverState(boolean mouseOver, MouseEvent event) {
	}
}