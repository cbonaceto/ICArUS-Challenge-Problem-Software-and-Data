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

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;

import org.mitre.icarus.cps.app.widgets.map.phase_1.MapConstants_Phase1;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupAttack;

/**
 * @author CBONACETO
 *
 */
public class AttackLocationPlacemark extends Phase1FeaturePlacemark<GroupAttack> {
	
	public AttackLocationPlacemark(GroupAttack attackLocation, String name) {
		this(attackLocation, name, false, Color.BLACK, null);		
	}
	
	public AttackLocationPlacemark(GroupAttack attackLocation, String name, boolean highlighted,
			Color markerColor, Image groupIcon) {
		super(attackLocation);
		this.name = name;
		id = name;
		showName= (groupIcon == null);
		markerIcon = groupIcon;
		markerShape = PlacemarkShape.Square;
		borderColor = markerColor;
		foregroundColor = markerColor;
		backgroundColor = Color.white;
		this.highlighted = highlighted;
		if(highlighted) {
			setBorderLineWidth(2.f);
			setTextFont(MapConstants_Phase1.PLACEMARK_FONT.deriveFont(Font.BOLD));
		}
		else {
			setBorderLineWidth(1.f);
			setTextFont(MapConstants_Phase1.PLACEMARK_FONT);
		}
	}	
}