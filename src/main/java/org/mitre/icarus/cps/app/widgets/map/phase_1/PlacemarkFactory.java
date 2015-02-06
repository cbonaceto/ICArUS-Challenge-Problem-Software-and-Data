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
package org.mitre.icarus.cps.app.widgets.map.phase_1;

import java.awt.Color;
import java.awt.Image;

import org.mitre.icarus.cps.app.widgets.WidgetConstants;
import org.mitre.icarus.cps.app.widgets.ImageManager.IconSize;
import org.mitre.icarus.cps.app.widgets.map.phase_1.objects.AttackLocationPlacemark;
import org.mitre.icarus.cps.app.widgets.map.phase_1.objects.GroupCenterPlacemark;
import org.mitre.icarus.cps.app.widgets.phase_1.ColorManager_Phase1;
import org.mitre.icarus.cps.app.widgets.phase_1.ImageManager_Phase1;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupAttack;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupCenter;

public class PlacemarkFactory {		

	public static enum AttackLocationType {LOCATION, GROUP_ATTACK};	

	public static AttackLocationPlacemark createAttackLocationPlacemark(GroupAttack attackLocation, 
			AttackLocationType attackLocationType) {
		return createAttackLocationPlacemark(attackLocation, attackLocationType, false);		
	}
	
	public static AttackLocationPlacemark createAttackLocationPlacemark(GroupAttack attackLocation, 
			AttackLocationType attackLocationType, boolean highlighted) {		
		String locationName = "?";
		Image markerIcon = null;
		Color markerColor = MapConstants_Phase1.PLACEMARK_MARKER_COLOR;
		switch(attackLocationType) {
		case LOCATION:
			if(attackLocation.getLocation() != null && attackLocation.getLocation().getLocationId() != null) {
				locationName = attackLocation.getLocation().getLocationId();
			}			
			break;
		case GROUP_ATTACK:
			if(attackLocation.getGroup() != null) {
				locationName = attackLocation.getGroup().toString(); 
				markerColor = ColorManager_Phase1.getGroupCenterColor(attackLocation.getGroup());
				if(WidgetConstants.USE_GROUP_SYMBOLS) {
					markerIcon = ImageManager_Phase1.getGroupSymbolImage(attackLocation.getGroup(), IconSize.Small);
				}
			}
			else if(attackLocation.getLocation() != null && attackLocation.getLocation().getLocationId() != null) {
				locationName = attackLocation.getLocation().getLocationId();
			}
			break;
		}		
		return new AttackLocationPlacemark(attackLocation, locationName, highlighted, markerColor, markerIcon);
	}
	
	public static GroupCenterPlacemark createGroupCenterPlacemark(GroupCenter groupCenter) {
		return createGroupCenterPlacemark(groupCenter, false);
	}
	
	public static GroupCenterPlacemark createGroupCenterPlacemark(GroupCenter groupCenter, boolean editable) {
		Image markerIcon = null;
		if(WidgetConstants.USE_GROUP_SYMBOLS) {
			markerIcon = ImageManager_Phase1.getGroupSymbolImage(groupCenter.getGroup(), IconSize.Large);
		}
		GroupCenterPlacemark groupCenterPlacemark = new GroupCenterPlacemark(groupCenter, markerIcon);
		if(WidgetConstants.USE_GROUP_SYMBOLS) {
			groupCenterPlacemark.setBackgroundColor(Color.WHITE);
			groupCenterPlacemark.setBorderLineWidth(2f);
			groupCenterPlacemark.setBorderColor(ColorManager_Phase1.getGroupCenterColor(groupCenter.getGroup()));
		}
		groupCenterPlacemark.setEditable(editable);
		return groupCenterPlacemark;
	}
}