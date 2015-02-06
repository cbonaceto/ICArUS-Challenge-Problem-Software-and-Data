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
import java.util.List;

import org.mitre.icarus.cps.app.widgets.phase_05.ColorManager;

/**
 * A building/water feature.
 * 
 * @author Jing Hu
 *
 */
public class Building extends Feature {
	
	private LayoutMode layoutMode = LayoutMode.LIST;
	
	private int[][] layout;
	private int[][] accessories;
	private int type;
	
	public int[][] getLayout() {
		return layout;
	}
	
	public void setLayout(int[][] layout) {
		this.layout = layout;
	}
	
	public int[][] getAccessories() {
		return accessories;
	}
	
	public void setAccessories(int[][] accessories) {
		this.accessories = accessories;
	}
	
	@Override
	public void draw(Graphics2D g, RenderData r) {
		if (type < 10) {
			g.setColor(ColorManager.get(ColorManager.BUILDING));
		} else {
			g.setColor(ColorManager.get(ColorManager.WATER));
		}
		
		//System.out.println(layoutMode + ", " + r);
		layoutMode.render(g, r.tileWidth, r.tileHeight, layout);
		if (accessories != null) {
			layoutMode.drawAccessories(g, r.tileWidth, r.tileHeight, accessories);
		}
	}

	@Override
	public List<? extends Feature> getChildren() {
		return null;
	}
	
	public LayoutMode getLayoutMode() {
		return layoutMode;
	}
	
	public void setLayoutMode(LayoutMode layoutMode) {
		this.layoutMode = layoutMode;
	}
	
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}
}
