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
package org.mitre.icarus.cps.app.widgets.map.phase_2;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.mitre.icarus.cps.app.widgets.map.phase_2.layers.ILayer_Phase2;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.IMapObject_Phase2;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.annotation.IAnnotatableMapObject;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.annotation.InformationBalloon;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.annotation.ToolTipRenderer;

/**
 * Manages tool tip information balloons.
 * 
 * @author CBONACETO
 *
 */
public class MapToolTipManager {

	/** The current tool tip information balloon(s) being displayed */
	private LinkedHashMap<IMapObject_Phase2, InformationBalloon<IMapObject_Phase2>> toolTipBalloons;
	
	private Set<IMapObject_Phase2> conflictObjects;
	
	/** Whether multiple tool tips can be displayed at once */
	private boolean multipleToolTipsEnabled = true;	
	
	
	public MapToolTipManager() {
		toolTipBalloons = new LinkedHashMap<IMapObject_Phase2, InformationBalloon<IMapObject_Phase2>>();
		conflictObjects = new HashSet<IMapObject_Phase2>();
	}
	
	public boolean isMultipleToolTipsEnabled() {
		return multipleToolTipsEnabled;
	}
	
	public boolean setMultipleToolTipsEnabled(boolean multipleToolTipsEnabled) {
		if(this.multipleToolTipsEnabled != multipleToolTipsEnabled) {
			this.multipleToolTipsEnabled = multipleToolTipsEnabled;
			if(!multipleToolTipsEnabled && toolTipBalloons.size() > 1) {
				removeAllToolTips();
				return true;
			}
		}
		return false;
	}

	public void addToolTip(IAnnotatableMapObject parentMapObject) {
		if(!multipleToolTipsEnabled) {
			toolTipBalloons.clear();
		}
		if(!toolTipBalloons.containsKey(parentMapObject)) {
			InformationBalloon<IMapObject_Phase2> toolTipBalloon = new InformationBalloon<IMapObject_Phase2>(
					parentMapObject, parentMapObject, new ToolTipRenderer(parentMapObject));
			conflictObjects.add(parentMapObject);
			conflictObjects.add(toolTipBalloon);
			//toolTipBalloon.setAutoAdjustOrientation(true, conflictObjects);
			toolTipBalloons.put(parentMapObject, toolTipBalloon);
		}
	}
	
	public void removeAllToolTips() {
		toolTipBalloons.clear();
		conflictObjects.clear();
	}
	
	public void removeToolTip(IMapObject_Phase2 parentMapObject) {
		if(toolTipBalloons.containsKey(parentMapObject)) {
			InformationBalloon<IMapObject_Phase2> toolTipBalloon = toolTipBalloons.get(parentMapObject);
			conflictObjects.remove(toolTipBalloon);
			conflictObjects.remove(parentMapObject);
			toolTipBalloons.remove(parentMapObject);			
		}		
	}
	
	public void removeToolTips(Collection<IMapObject_Phase2> parentMapObjects) {
		if(parentMapObjects != null && !parentMapObjects.isEmpty()) {
			for(IMapObject_Phase2 parentMapObject : parentMapObjects) {
				removeToolTip(parentMapObject);
			}
		}
	}
	
	public boolean isToolTipPresent(IMapObject_Phase2 mapObject) {
		return toolTipBalloons.containsKey(mapObject);
	}
	
	public int size() {
		return toolTipBalloons.size();
	}
	
	public InformationBalloon<IMapObject_Phase2> getFirstToolTipAtLocation(Point2D location,
			boolean justCheckCloseButton) {
		if(!toolTipBalloons.isEmpty()) {
			for(InformationBalloon<IMapObject_Phase2> currentToolTipBalloon : toolTipBalloons.values()) {
				if(justCheckCloseButton) {
					if(currentToolTipBalloon.closeButtonContainsPixelLocation(
							location.getX(), location.getY())) {
						return currentToolTipBalloon;
					}
				} else {
					if(currentToolTipBalloon.containsPixelLocation(location)) {
						return currentToolTipBalloon;
					}
				}
			}
		} 
		return null;
	}
	
	public List<InformationBalloon<IMapObject_Phase2>> getAllToolTipsAtLocation(Point2D location,
			boolean justCheckCloseButton) {		
		if(!toolTipBalloons.isEmpty()) {
			List<InformationBalloon<IMapObject_Phase2>> toolTips = 
					new LinkedList<InformationBalloon<IMapObject_Phase2>>();
			for(InformationBalloon<IMapObject_Phase2> toolTipBalloon : toolTipBalloons.values()) {
				if(justCheckCloseButton) {
					if(toolTipBalloon.closeButtonContainsPixelLocation(
							location.getX(), location.getY())) {
						toolTips.add(toolTipBalloon);
					}
				} else {
					if(toolTipBalloon.containsPixelLocation(location)) {
						toolTips.add(toolTipBalloon);
					}
				}
			}
			return !toolTips.isEmpty() ? toolTips : null;
		} else {
			return null;
		}
	}	
	
	public void renderToolTips(Graphics2D g, RenderProperties renderProperties) {
		if(!toolTipBalloons.isEmpty()) {
			for(InformationBalloon<IMapObject_Phase2> toolTipBalloon : toolTipBalloons.values()) {
				//if(toolTipsEnabled && currentToolTipBalloon != null && currentToolTipBalloon.getParent() != null) {
				if(toolTipBalloon.getParent() != null) {
					ILayer_Phase2<? extends IMapObject_Phase2, ? extends MapPanel_Phase2> layer = 
							toolTipBalloon.getParent().getLayer();
					if(layer != null && layer.isVisible()) {
						toolTipBalloon.render(g, renderProperties, true);
					}
				}
			}
		}		
	}
}