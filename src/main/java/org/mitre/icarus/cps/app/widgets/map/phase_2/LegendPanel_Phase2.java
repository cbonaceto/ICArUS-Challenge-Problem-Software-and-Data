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

import java.util.ArrayList;
import java.util.List;

import org.mitre.icarus.cps.app.widgets.map.LegendPanel;
import org.mitre.icarus.cps.app.widgets.phase_2.ImageManager_Phase2;

/**
 * A specialized legend panel for the Phase 2 Challenge Problem format.
 * 
 * @author CBONACETO
 *
 */
public class LegendPanel_Phase2 extends LegendPanel {
	
	private static final long serialVersionUID = -585304875230898451L;	
	
	/** The area of interest legend item */
	protected ParentLegendItem aoiItem;
	
	/** The blue region legend item */
	protected ParentLegendItem blueRegionItem;
	
	/** The blue locations legend item */
	protected ParentLegendItem blueLocationsItem;
	
	/** Batch plot legend item */
	protected ParentLegendItem batchPlotItem;
	
	/** The OSINT legend item */
	protected ParentLegendItem osintItem;
	
	/** The IMINT legend item */
	protected ParentLegendItem imintItem;
	
	/** The SIGINT legend item */
	protected ParentLegendItem sigintItem;
	
	public LegendPanel_Phase2() {
		aoiItem = new ParentLegendItem("Buildings, Terrain, Roads", ImageManager_Phase2.getImageIcon(ImageManager_Phase2.ROADS_LAYER_ICON));
		aoiItem.setVisible(false);
		
		blueRegionItem = new ParentLegendItem("Blue Region", null);
		blueRegionItem.setVisible(false);
		
		blueLocationsItem = new ParentLegendItem("Blue Locations", null);
		blueLocationsItem.setVisible(false);
		
		batchPlotItem = new ParentLegendItem("Trial Outcome", null);
		List<LegendItem> outcomes = new ArrayList<LegendItem>(3);
		outcomes.add(new LegendItem("Red attacked (Blue won)", 
				ImageManager_Phase2.getImageIcon(ImageManager_Phase2.RED_ATTACK_BLUE_WIN_ICON)));
		outcomes.add(new LegendItem("Red attacked (Red won)",
				ImageManager_Phase2.getImageIcon(ImageManager_Phase2.RED_ATTACK_RED_WIN_ICON)));
		outcomes.add(new LegendItem("Red did not attack",
				ImageManager_Phase2.getImageIcon(ImageManager_Phase2.RED_NOT_ATTACK_ICON)));
		batchPlotItem.setChildren(outcomes);
		batchPlotItem.setVisible(false);
		
		osintItem = new ParentLegendItem("Vulnerability (OSINT)", null);
		osintItem.setVisible(false);
		
		imintItem = new ParentLegendItem("Opportunity (IMINT)", null);
		imintItem.setVisible(false);
		
		sigintItem = new ParentLegendItem("Activity (SIGINT)", null);
		sigintItem.setVisible(false);
	}
	
	public void setAoiItemVisible(boolean visible) {
		setLegendItemVisible(aoiItem, visible);
	}	
	
	public void setAoiItemName(String name) {
		aoiItem.setName(name);
		legendTree.repaint();
	}
	
	public void setBlueRegionItemVisible(boolean visible) {		
		setLegendItemVisible(blueRegionItem, visible);
	}
	
	public void setBlueLocationsItemVisible(boolean visible) {		
		setLegendItemVisible(blueLocationsItem, visible);
	}
	
	public void setBatchPlotItemVisible(boolean visible) {		
		setLegendItemVisible(batchPlotItem, visible);
	}
	
	public void setOsintItemVisible(boolean visible) {		
		setLegendItemVisible(osintItem, visible);
	}
	
	public void setImintItemVisible(boolean visible) {		
		setLegendItemVisible(imintItem, visible);
	}
	
	public void setSigintItemVisible(boolean visible) {		
		setLegendItemVisible(sigintItem, visible);
	}
	
	protected void setLegendItemVisible(ParentLegendItem legendItem, boolean visible) {
		if(legendItem != null && visible != legendItem.isVisible()) {
			if(visible) {
				addLegendItem(legendItem);
			}
			else {
				removeLegendItem(legendItem);
			}
		}
	}
}