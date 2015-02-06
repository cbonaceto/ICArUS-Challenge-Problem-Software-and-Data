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
package org.mitre.icarus.cps.app.widgets.map.phase_05;

import java.awt.Graphics2D;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;


/**
 * Layer containing sectors.  Sectors just render their bounds and possibly their name.
 * A world can only contain one SectorLayer.
 * 
 * @author CBONACETO
 *
 */
public class SectorLayer extends Layer implements Iterable<Sector> {
	
	/** Sectors mapped by their sector ID */
	private HashMap<Integer, Sector> sectors = new HashMap<Integer, Sector>();

	public static final int SECTOR_LAYER_ID = 100;
	
	public SectorLayer() {
		super(SECTOR_LAYER_ID);
		setName("Sectors");
	}

	@Override
	public LayerType getLayerType() {
		return LayerType.Sectors;
	}

	@Override
	public void draw(Graphics2D g, RenderData r) {}

	@Override
	public Collection<Sector> getChildren() {
		return sectors.values();
	}	
	
	public void addSector(Sector sector) {
		sectors.put(sector.getSectorId(), sector);
	}
	
	public Sector getSector(Integer sectorId) {
		return sectors.get(sectorId);
	}
	
	public int getNumSectors() {
		return sectors.size();
	}
	
	public boolean containsSector(Integer sectorId) {
		return sectors.containsKey(sectorId);
	}

	@Override
	public Iterator<Sector> iterator() {
		return sectors.values().iterator();
	}
}
